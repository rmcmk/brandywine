package me.ryleykimmel.brandywine;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.moandjiezana.toml.Toml;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.common.util.ClassUtil;
import me.ryleykimmel.brandywine.common.util.ThreadFactoryUtil;
import me.ryleykimmel.brandywine.common.util.TomlUtil;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.network.game.GameChannelInitializer;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;

/**
 * The core class of the Server.
 */
final class Server {

  /**
   * The entry point of this application which implements the command line-interface.
   *
   * @param args The command-line arguments.
   */
  public static void main(String[] args) {
    try {
      Server server = new Server();
      server.init();
      server.start();
    } catch (Exception cause) {
      throw new StartupException("Error initializing server!", cause);
    }
  }

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  /**
   * A Map of all of the registered Services.
   */
  private final Map<Class<? extends Service>, Service> services = new HashMap<>();

  /**
   * The FrameMetadataSet for this Server.
   */
  private FrameMetadataSet frameMetadataSet;

  /**
   * The name of this Server.
   */
  private String name;

  /**
   * The game port this Server will listen on.
   */
  private int gamePort;

  /**
   * The maximum amount of connections per host.
   */
  private int connectionLimit;

  /**
   * The AuthenticationStrategy used to authenticate upstream login requests.
   */
  private AuthenticationStrategy authenticationStrategy;

  /**
   * This Servers database configuration.
   */
  private Sql2o sql2o;

  /**
   * The FileSystem for this Server.
   */
  private FileSystem fileSystem;

  /**
   * Constructs a new {@link Server}.
   */
  protected Server() {}

  /**
   * Initializes this Server.
   * 
   * @throws Exception If some exception occurs.
   */
  private void init() throws Exception {
    initConfig();
    initServices();
    initPulse();
  }

  /**
   * Initializes Server configurations
   * 
   * @throws Exception If some exception occurs.
   */
  private void initConfig() throws Exception {
    Path data = Paths.get("data");
    Toml toml = TomlUtil.read(data.resolve("config.toml"));

    name = toml.getString("name");
    gamePort = toml.getLong("game_port").intValue();
    fileSystem = FileSystem.create(toml.getString("fs_directory"));
    connectionLimit = toml.getLong("connection_limit").intValue();

    String path = toml.getString("authentication_strategy");

    if (path.equals("default")) {
      this.authenticationStrategy = AuthenticationService.DEFAULT_STRATEGY;
    } else {
      Class<?> clazz = Class.forName(path);

      if (!ClassUtil.hasInterface(clazz, AuthenticationStrategy.class)) {
        throw new StartupException(path + " is not an instance of AuthenticationStrategy.");
      }

      AuthenticationStrategy authenticationStrategy = (AuthenticationStrategy) clazz.newInstance();
      this.authenticationStrategy = authenticationStrategy;
    }

    toml = TomlUtil.read(data.resolve("database.toml"));

    String databaseAddress = toml.getString("url");
    String databaseUsername = toml.getString("username");
    String databasePassword = toml.getString("password");
    sql2o = new Sql2o(databaseAddress, databaseUsername, databasePassword);
  }

  /**
   * Initializes {@link Service}s.
   * 
   * @throws Exception If some exception occurs.
   */
  private void initServices() throws Exception {
    Path data = Paths.get("data");
    Toml toml = TomlUtil.read(data.resolve("services.toml"));

    List<String> paths = toml.getList("services");
    for (String path : paths) {
      Class<?> clazz = Class.forName(path);
      if (!clazz.getSuperclass().equals(Service.class)) {
        throw new StartupException(path + " is not an instance of Service.");
      }

      Service service = (Service) clazz.newInstance();
      services.put(service.getClass(), service);
    }
  }

  /**
   * Initializes the GamePulseHandler.
   */
  private void initPulse() {
    GamePulseHandler pulseHandler = new GamePulseHandler(services);
    ScheduledExecutorService executor =
        Executors.newSingleThreadScheduledExecutor(ThreadFactoryUtil.create(pulseHandler).build());
    executor.scheduleAtFixedRate(pulseHandler, GamePulseHandler.PULSE_DELAY,
        GamePulseHandler.PULSE_DELAY, TimeUnit.MILLISECONDS);
  }

  /**
   * Starts this Server.
   */
  private void start() {
    setup(gamePort, new GameChannelInitializer(getService(GameService.class), frameMetadataSet));
    logger.info("{} has started successfully!", name);
  }

  /**
   * Sets up a new {@link ServerChannel} from the specified host, port and
   * {@link ChannelInitializer}
   *
   * @param port The port to bind the ServerChannel to.
   * @param initializer A specialized {@link ChannelHandler} used to serve {@link Channel} requests.
   */
  private void setup(int port, ChannelInitializer<SocketChannel> initializer) {
    ServerBootstrap bootstrap = new ServerBootstrap();
    SocketAddress address = new InetSocketAddress(port);

    NioEventLoopGroup parentGroup = new NioEventLoopGroup();
    NioEventLoopGroup childGroup = new NioEventLoopGroup();

    logger.info("Setting up service on address {}...", address);

    bootstrap.group(parentGroup, childGroup);
    bootstrap.channel(NioServerSocketChannel.class);
    bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
    bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
    bootstrap.childHandler(initializer);
    bootstrap.bind(address);
  }

  /**
   * Gets a Service from its type.
   *
   * @param clazz The type of the Service, may not be {@code null}.
   * @return The instance of the Service, never {@code null}.
   */
  @SuppressWarnings("unchecked")
  public <T extends Service> T getService(Class<T> clazz) {
    Preconditions.checkNotNull(clazz, "Service type may not be null.");
    return (T) Preconditions.checkNotNull(services.get(clazz),
        "Service for: " + StringUtil.simpleClassName(clazz) + " does not exist.");
  }

  /**
   * Gets a shallow-copy of all {@link Service}s, as a {@link ImmutableSet}.
   *
   * @return The shallow ImmutableSet of Services.
   */
  public ImmutableSet<Service> getServices() {
    return ImmutableSet.copyOf(services.values());
  }

  /**
   * Gets the FrameMetadataSet for this Server.
   * 
   * @return The FrameMetadataSet for this Server.
   */
  public FrameMetadataSet getFrameMetadataSet() {
    return frameMetadataSet;
  }

  /**
   * Gets the name of this Server.
   *
   * @return The name of this Server.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the maximum amount of connections per host.
   * 
   * @return The maximum amount of connections per host.
   */
  public int getConnectionLimit() {
    return connectionLimit;
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
   * Gets the FileSystem for this Server.
   *
   * @return The FileSystem for this Server.
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Gets this Servers database configuration.
   * 
   * @return This Servers database configuration.
   */
  public Sql2o getSql2o() {
    return sql2o;
  }

}
