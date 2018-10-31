package me.ryleykimmel.brandywine;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.ryleykimmel.brandywine.common.util.ThreadFactoryUtil;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet;
import me.ryleykimmel.brandywine.game.message.GameMessageRegistrar;
import me.ryleykimmel.brandywine.game.message.LoginMessageRegistrar;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.network.ImmediateMessageReceivedListener;
import me.ryleykimmel.brandywine.network.ResponseCode;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec;
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec;
import me.ryleykimmel.brandywine.network.message.MessageHandler;
import me.ryleykimmel.brandywine.network.message.MessageReceivedListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Sql2o;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The core class of the Server.
 */
public final class Server {

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LogManager.getLogger(Server.class);

  /**
   * The entry point of this application.
   *
   * @param args The command-line arguments.
   */
  public static void main(String[] args) {
    World world = new World(new EventConsumerChainSet());
    Server server = new Server();
    GameService gameService = new GameService(world);
    FrameMetadataSet gameFrameMetadataSet = new GameMessageRegistrar(world).build();
    FrameMetadataSet loginFrameMetadataSet = new LoginMessageRegistrar(world).build();
    MessageReceivedListener immediateMessageReceivedListener = new ImmediateMessageReceivedListener();

    try {
      server.setFileSystem(FileSystem.create("data/fs/"));
      server.setSql2o(new Sql2o("jdbc:mysql://localhost/game_server", "root", ""));
      server.setAuthenticationStrategy(__ -> ResponseCode.STATUS_OK);
      world.registerService(gameService);
      world.registerService(new AuthenticationService(gameService, server.getAuthenticationStrategy()));
      server.initializer(new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel channel) {
          Session session = new Session(channel, loginFrameMetadataSet, immediateMessageReceivedListener);
          channel.pipeline().addLast("frame_codec", new FrameCodec(session)).
                  addLast("message_codec", new FrameMessageCodec(session)).
                  addLast("handler", new MessageHandler(session));
        }
      });
      server.init(world, 43594);
    } catch (IOException cause) {
      logger.error("Unexpected error while starting Brandywine!", cause);
    }
  }

  /**
   * The {@link ServerBootstrap} for this Server.
   */
  private final ServerBootstrap bootstrap = new ServerBootstrap();

  /**
   * The AuthenticationStrategy used to authenticate upstream login requests.
   */
  private AuthenticationStrategy authenticationStrategy = AuthenticationService.DEFAULT_STRATEGY;

  /**
   * This Servers database configuration.
   */
  private Optional<Sql2o> sql2o = Optional.empty();

  /**
   * The FileSystem for this Server.
   */
  private Optional<FileSystem> fileSystem = Optional.empty();

  /**
   * Initializes and binds this Server.
   *
   * @param port The port this Server listens on.
   */
  public void init(World world, int port) {
    GamePulseHandler pulseHandler = new GamePulseHandler(world.getServices());
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(ThreadFactoryUtil.create(pulseHandler).build());
    executor.scheduleAtFixedRate(pulseHandler, GamePulseHandler.PULSE_DELAY, GamePulseHandler.PULSE_DELAY, TimeUnit.MILLISECONDS);

    boolean epoll = Epoll.isAvailable();

    Class<? extends ServerChannel> serverChannel = epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class;

    EventLoopGroup parentGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    EventLoopGroup childGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();

    bootstrap.channel(serverChannel).group(parentGroup, childGroup).bind(port);
  }

  /**
   * Configures the {@link ChannelInitializer}, used to configure {@link Channel}s once they have
   * been registered in the event loop.
   *
   * @param initializer The ChannelInitializer to use, may not be {@code null}.
   */
  public void initializer(ChannelInitializer<SocketChannel> initializer) {
    bootstrap.childHandler(initializer);
  }

  /**
   * Gets the AuthenticationStrategy used by the Server.
   *
   * @return The AuthenticationStrategy used by the Server.
   */
  public AuthenticationStrategy getAuthenticationStrategy() {
    return authenticationStrategy;
  }

  /**
   * Configures the AuthenticationStrategy this Server will use.
   *
   * @param authenticationStrategy The AuthenticationStrategy, may not be {@code null}.
   * @return This Server instance, for chaining.
   */
  public Server setAuthenticationStrategy(AuthenticationStrategy authenticationStrategy) {
    this.authenticationStrategy = Preconditions.checkNotNull(authenticationStrategy, "AuthenticationStrategy may not be null.");
    return this;
  }

  /**
   * Gets the FileSystem for this Server.
   *
   * @return The FileSystem for this Server.
   */
  public FileSystem getFileSystem() {
    return fileSystem.orElseThrow(() -> new UnsupportedOperationException("No FileSystem configured, use Server#setFileSystem to configure one."));
  }

  /**
   * Configures the FileSystem for this Server.
   *
   * @param fileSystem The FileSystem, may not be {@code null}.
   * @return This Server instance, for chaining.
   */
  public Server setFileSystem(FileSystem fileSystem) {
    this.fileSystem = Optional.of(fileSystem);
    return this;
  }

  /**
   * Gets this Servers database configuration.
   *
   * @return This Servers database configuration.
   */
  public Sql2o getSql2o() {
    return sql2o.orElseThrow(() -> new UnsupportedOperationException("No database provider configured, use Server#setSql2o to configure one."));
  }

  /**
   * Configures the Sql2o database configuration for this Server.
   *
   * @param sql2o The Sql2o database configuration, may not be {@code null}.
   * @return This Server instance, for chaining.
   */
  public Server setSql2o(Sql2o sql2o) {
    this.sql2o = Optional.of(sql2o);
    return this;
  }

}
