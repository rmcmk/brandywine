package me.ryleykimmel.brandywine;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import com.google.common.base.MoreObjects;
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
import me.ryleykimmel.brandywine.common.util.ClassUtil;
import me.ryleykimmel.brandywine.common.util.TomlUtil;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.network.game.GameChannelInitializer;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;

/**
 * The core class of the Server.
 */
final class Server {

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  /**
   * The context of this Server.
   */
  private final ServerContext context = new ServerContext(this);

  /**
   * The FrameMetadataSet for this Server.
   */
  private final FrameMetadataSet frameMetadataSet = new FrameMetadataSet();

  /**
   * The name of this Server.
   */
  private String name = "Brandywine";

  /**
   * The game port this Server will listen on.
   */
  private int gamePort = 43594;

  /**
   * The maximum amount of connections per host.
   */
  private int connectionLimit = 1;

  /**
   * The address for the Servers database.
   */
  private String databaseAddress = "localhost";

  /**
   * The port the Servers database is listening on.
   */
  private int databasePort = 3306;

  /**
   * The username of the Servers database.
   */
  private String databaseUsername = "root";

  /**
   * The password of the Servers database.
   */
  private String databasePassword = "";

  /**
   * The AuthenticationStrategy used to authenticate upstream login requests.
   */
  private AuthenticationStrategy authenticationStrategy = AuthenticationService.DEFAULT_STRATEGY;

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
  private Server() {
    logger.info("{} is initializing!", name);
  }

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
   * Initializes this Server.
   * 
   * @throws Exception If some exception occurs.
   */
  public void init() throws Exception {
    initConfig();
    initServices();
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

      AuthenticationStrategy authenticationStrategy =
          (AuthenticationStrategy) clazz.getConstructor(ServerContext.class).newInstance(context);
      this.authenticationStrategy = authenticationStrategy;
    }

    toml = TomlUtil.read(data.resolve("database.toml"));

    databaseAddress = toml.getString("url");
    databasePort = toml.getLong("port").intValue();
    databaseUsername = toml.getString("username");
    databasePassword = toml.getString("password");
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

      Service service = (Service) clazz.getConstructor(ServerContext.class).newInstance(context);
      context.addService(service.getClass(), service);
    }
  }

  /**
   * Starts this Server.
   */
  public void start() {
    setup(gamePort, new GameChannelInitializer(context));

    GamePulseHandler handler = new GamePulseHandler(context);
    handler.init();

    logger.info("{} has started successfully!", name);
  }

  /**
   * Sets up a new {@link ServerChannel} from the specified host, port and
   * {@link ChannelInitializer}
   *
   * @param port The port to bind the ServerChannel to.
   * @param initializer A specialized {@link ChannelHandler} used to serve {@link Channel} requests.
   */
  public void setup(int port, ChannelInitializer<SocketChannel> initializer) {
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
   * Gets the game port this Server will listen on.
   *
   * @return The game port.
   */
  public int getGamePort() {
    return gamePort;
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
   * Gets the address for the Servers database.
   * 
   * @return The address for the Servers database.
   */
  public String getDatabaseAddress() {
    return databaseAddress;
  }

  /**
   * Gets the port the Servers database is listening on.
   * 
   * @return The port the Servers database is listening on.
   */
  public int getDatabasePort() {
    return databasePort;
  }

  /**
   * Gets the username of the Servers database.
   * 
   * @return The username of the Servers database.
   */
  public String getDatabaseUsername() {
    return databaseUsername;
  }

  /**
   * Gets the password of the Servers database.
   * 
   * @return The password of the Servers database.
   */
  public String getDatabasePassword() {
    return databasePassword;
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).add("gamePort", gamePort).toString();
  }

}
