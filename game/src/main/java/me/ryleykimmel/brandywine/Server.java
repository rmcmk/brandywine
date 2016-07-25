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
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import org.sql2o.Sql2o;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The core class of the Server.
 */
public final class Server {

  /**
   * The {@link ServiceSet} for this Server.
   */
  private final ServiceSet services = new ServiceSet();

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
  public void init(int port) {
    GamePulseHandler pulseHandler = new GamePulseHandler(services);
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
      ThreadFactoryUtil.create(pulseHandler).build());
    executor
      .scheduleAtFixedRate(pulseHandler, GamePulseHandler.PULSE_DELAY, GamePulseHandler.PULSE_DELAY,
        TimeUnit.MILLISECONDS);

    boolean epoll = Epoll.isAvailable();

    Class<? extends ServerChannel> serverChannel = epoll ?
                                                     EpollServerSocketChannel.class :
                                                     NioServerSocketChannel.class;

    EventLoopGroup parentGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    EventLoopGroup childGroup = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();

    bootstrap.channel(serverChannel).group(parentGroup, childGroup).bind(port);
  }

  /**
   * Gets a Service from its type.
   *
   * @param clazz The type of the Service, may not be {@code null}.
   * @return The instance of the Service, never {@code null}.
   */
  public <T extends Service> T getService(Class<T> clazz) {
    return services.get(clazz);
  }

  /**
   * Registers the specified Service.
   *
   * @param service The Service to register, may not be {@code null}.
   * @return This Server instance, for chaining.
   */
  public Server registerService(Service service) {
    services.register(service);
    return this;
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
    this.authenticationStrategy = Preconditions.checkNotNull(authenticationStrategy,
      "AuthenticationStrategy may not be null.");
    return this;
  }

  /**
   * Gets the FileSystem for this Server.
   *
   * @return The FileSystem for this Server.
   */
  public FileSystem getFileSystem() {
    return fileSystem.orElseThrow(
      () -> new UnsupportedOperationException("No FileSystem configured, use Server#setFileSystem to configure one."));
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
    return sql2o.orElseThrow(
      () -> new UnsupportedOperationException("No database provider configured, use Server#setSql2o to configure one."));
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
