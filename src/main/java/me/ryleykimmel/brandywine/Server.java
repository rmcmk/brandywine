package me.ryleykimmel.brandywine;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.network.game.GameChannelInitializer;
import me.ryleykimmel.brandywine.parser.TomlParser;
import me.ryleykimmel.brandywine.parser.impl.ParserTomlParser;

/**
 * The core class of the Server.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
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
	 * Special EventExecutorGroup which allows registering Channels that get processed for later selection during the event loop.
	 */
	private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

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
		} catch (Exception reason) {
			throw new StartupException("Error initializing server!", reason);
		}
	}

	/**
	 * Initializes this Server.
	 *
	 * @throws Exception If some Exception occurs during initialization.
	 */
	public void init() throws Exception {
		TomlParser config = new ParserTomlParser("data/parsers.toml", context);
		config.parse();
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
	 * Sets up a new {@link ServerChannel} from the specified host, port and {@link ChannelInitializer}
	 *
	 * @param port The port to bind the ServerChannel to.
	 * @param initializer A specialized {@link ChannelHandler} used to serve {@link Channel} requests.
	 */
	public void setup(int port, ChannelInitializer<SocketChannel> initializer) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		SocketAddress address = new InetSocketAddress(port);

		logger.info("Setting up service on address {}...", address);

		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.group(eventLoopGroup);
		bootstrap.childHandler(initializer);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.bind(address).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Gets the context of this Server.
	 *
	 * @return The context of this Server.
	 */
	public ServerContext getContext() {
		return context;
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
	 * Sets the name of this Server.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Sets the game port this Server will listen on.
	 *
	 * @param gamePort The game port to set.
	 */
	public void setGamePort(int gamePort) {
		this.gamePort = gamePort;
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
	 * Sets the maximum amount of connections per host.
	 * 
	 * @param connectionLimit The maximum amount of connections per host.
	 */
	public void setConnectionLimit(int connectionLimit) {
		this.connectionLimit = connectionLimit;
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
	 * Sets the address for the Servers database.
	 * 
	 * @param databaseAddress The address to set.
	 */
	public void setDatabaseAddress(String databaseAddress) {
		this.databaseAddress = databaseAddress;
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
	 * Sets the port the Servers database is listening on.
	 * 
	 * @param databasePort The port to set.
	 */
	public void setDatabasePort(int databasePort) {
		this.databasePort = databasePort;
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
	 * Sets the username of the Servers database.
	 * 
	 * @param databaseUsername The username of the Servers database.
	 */
	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
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
	 * Sets the password of the Servers database.
	 * 
	 * @param databasePassword The password of the Servers database.
	 */
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
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
	 * Sets the FileSystem for this Server.
	 *
	 * @param fileSystem The FileSystem to set.
	 */
	public void setFileSystem(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", name).add("gamePort", gamePort).toString();
	}

}