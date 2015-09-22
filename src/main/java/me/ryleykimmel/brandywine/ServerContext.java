package me.ryleykimmel.brandywine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.parser.Parser;

/**
 * A {@link ServerContext} is created along with the Server object. The primary difference is that a reference to the current context should be passed around within the server. The
 * Server should not be as it allows access to some methods which core and plugin code should not be able to access.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ServerContext {

	/**
	 * A Map of all of the registered Parsers.
	 */
	private final Map<Class<? extends Parser<?, ?>>, Parser<?, ?>> parsers = new HashMap<>();

	/**
	 * A Map of all of the registered Services.
	 */
	private final Map<Class<? extends Service>, Service> services = new HashMap<>();

	/**
	 * The Server this context represents.
	 */
	private final Server server;

	/**
	 * Constructs a new {@link ServerContext} with the specified Server.
	 *
	 * @param server The Server this context represents.
	 */
	public ServerContext(Server server) {
		this.server = server;
	}

	/**
	 * Gets a Service from its type.
	 *
	 * @param clazz The type of the Service.
	 * @return The instance of the Service.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(Class<T> clazz) {
		return (T) services.get(clazz);
	}

	/**
	 * Adds the specified Service to the map.
	 *
	 * @param clazz The type of the Service.
	 * @param service The instance of the Service.
	 */
	public <T extends Service> void addService(Class<T> clazz, T service) {
		services.put(clazz, service);
	}

	/**
	 * Gets a Parser from its type.
	 *
	 * @param clazz The type of the Parser.
	 * @return The instance of the Parser.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Parser<?, ?>> T getParser(Class<T> clazz) {
		return (T) parsers.get(clazz);
	}

	/**
	 * Adds the specified Parser to the map.
	 *
	 * @param clazz The type of the Parser.
	 * @param parser The Parser to add.
	 */
	public <T extends Parser<?, ?>> void addParser(Class<T> clazz, T parser) {
		parsers.put(clazz, parser);
	}

	/**
	 * Gets a Collection of all Services.
	 *
	 * @return A Collection of all Services.
	 */
	public Collection<Service> getServices() {
		return services.values();
	}

	/**
	 * Gets the name of this Server.
	 *
	 * @return The name of this Server.
	 */
	public String getName() {
		return server.getName();
	}

	/**
	 * Sets the name of this Server.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		server.setName(name);
	}

	/**
	 * Gets the game port this Server will listen on.
	 *
	 * @return The game port.
	 */
	public int getGamePort() {
		return server.getGamePort();
	}

	/**
	 * Sets the game port this Server will listen on.
	 *
	 * @param gamePort The game port to set.
	 */
	public void setGamePort(int gamePort) {
		server.setGamePort(gamePort);
	}

	/**
	 * Gets the maximum amount of connections per host.
	 * 
	 * @return The maximum amount of connections per host.
	 */
	public int getConnectionLimit() {
		return server.getConnectionLimit();
	}

	/**
	 * Sets the maximum amount of connections per host.
	 * 
	 * @param connectionLimit The maximum amount of connections per host.
	 */
	public void setConnectionLimit(int connectionLimit) {
		server.setConnectionLimit(connectionLimit);
	}

	/**
	 * Gets the address for the Servers database.
	 * 
	 * @return The address for the Servers database.
	 */
	public String getDatabaseAddress() {
		return server.getDatabaseAddress();
	}

	/**
	 * Sets the address for the Servers database.
	 * 
	 * @param databaseAddress The address to set.
	 */
	public void setDatabaseAddress(String databaseAddress) {
		server.setDatabaseAddress(databaseAddress);
	}

	/**
	 * Gets the port the Servers database is listening on.
	 * 
	 * @return The port the Servers database is listening on.
	 */
	public int getDatabasePort() {
		return server.getDatabasePort();
	}

	/**
	 * Sets the port the Servers database is listening on.
	 * 
	 * @param databasePort The port to set.
	 */
	public void setDatabasePort(int databasePort) {
		server.setDatabasePort(databasePort);
	}

	/**
	 * Gets the username of the Servers database.
	 * 
	 * @return The username of the Servers database.
	 */
	public String getDatabaseUsername() {
		return server.getDatabaseUsername();
	}

	/**
	 * Sets the username of the Servers database.
	 * 
	 * @param databaseUsername The username of the Servers database.
	 */
	public void setDatabaseUsername(String databaseUsername) {
		server.setDatabaseUsername(databaseUsername);
	}

	/**
	 * Gets the password of the Servers database.
	 * 
	 * @return The password of the Servers database.
	 */
	public String getDatabasePassword() {
		return server.getDatabasePassword();
	}

	/**
	 * Sets the password of the Servers database.
	 * 
	 * @param databasePassword The password of the Servers database.
	 */
	public void setDatabasePassword(String databasePassword) {
		server.setDatabasePassword(databasePassword);
	}

	/**
	 * Gets the FileSystem for this Server.
	 *
	 * @return The FileSystem for this Server.
	 */
	public FileSystem getFileSystem() {
		return server.getFileSystem();
	}

	/**
	 * Sets the FileSystem for this Server.
	 *
	 * @param fileSystem The FileSystem to set.
	 */
	public void setFileSystem(FileSystem fileSystem) {
		server.setFileSystem(fileSystem);
	}

}