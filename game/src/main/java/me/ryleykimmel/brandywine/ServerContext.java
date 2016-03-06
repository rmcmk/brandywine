package me.ryleykimmel.brandywine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sql2o.Sql2o;

import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;

/**
 * A {@link ServerContext} is created along with the Server object. The primary difference is that a
 * reference to the current context should be passed around within the server. The Server should not
 * be as it allows access to some methods which core and plugin code should not be able to access.
 */
public final class ServerContext {

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
  public void addService(Class<? extends Service> clazz, Service service) {
    services.put(clazz, service);
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
   * Gets the FrameMetadataSet for this Server.
   * 
   * @return The FrameMetadataSet for this Server.
   */
  public FrameMetadataSet getFrameMetadataSet() {
    return server.getFrameMetadataSet();
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
   * Gets the game port this Server will listen on.
   *
   * @return The game port.
   */
  public int getGamePort() {
    return server.getGamePort();
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
   * Gets the address for the Servers database.
   * 
   * @return The address for the Servers database.
   */
  public String getDatabaseAddress() {
    return server.getDatabaseAddress();
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
   * Gets the username of the Servers database.
   * 
   * @return The username of the Servers database.
   */
  public String getDatabaseUsername() {
    return server.getDatabaseUsername();
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
   * Gets the FileSystem for this Server.
   *
   * @return The FileSystem for this Server.
   */
  public FileSystem getFileSystem() {
    return server.getFileSystem();
  }

  /**
   * Gets this Servers database configuration.
   * 
   * @return This Servers database configuration.
   */
  public Sql2o getSql2o() {
    return server.getSql2o();
  }

  /**
   * Gets the AuthenticationStrategy used by the Server.
   * 
   * @return The AuthenticationStrategy used by the Server.
   */
  public AuthenticationStrategy getAuthenticationStrategy() {
    return server.getAuthenticationStrategy();
  }

}
