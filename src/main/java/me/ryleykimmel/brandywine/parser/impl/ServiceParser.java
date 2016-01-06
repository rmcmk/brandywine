package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;
import java.util.List;

import com.moandjiezana.toml.Toml;

import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.Service;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * Parses all of the Services.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ServiceParser extends TomlParser {

  /**
   * The context of the Server.
   */
  private final ServerContext context;

  /**
   * Constructs a new {@link ServiceParser} with the specified path and ServerContext.
   *
   * @param path The path to the source.
   * @param context The context of the Server.
   */
  public ServiceParser(String path, ServerContext context) {
    super(path);
    this.context = context;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void parse(Reader source, Toml data) throws Exception {
    List<String> services = data.getList("services");
    for (String service : services) {
      Class<Service> clazz = (Class<Service>) Class.forName(service);
      Service instance = clazz.getConstructor(ServerContext.class).newInstance(context);
      context.addService(clazz, instance);
    }
  }

}
