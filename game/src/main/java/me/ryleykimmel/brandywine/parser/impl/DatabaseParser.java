package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;

import org.sql2o.Sql2o;

import com.moandjiezana.toml.Toml;

import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * Parses a configuration for this Server.
 */
public final class DatabaseParser extends TomlParser {

  /**
   * The context of the Server.
   */
  private final ServerContext context;

  /**
   * Constructs a new {@link DatabaseParser} with the specified path and ServerContext.
   *
   * @param path The path to the source.
   * @param context The context of the Server.
   */
  public DatabaseParser(String path, ServerContext context) {
    super(path);
    this.context = context;
  }

  @Override
  public void parse(Reader source, Toml data) {
    context.setDatabaseAddress(data.getString("url"));
    context.setDatabasePort(getInteger("port"));
    context.setDatabaseUsername(data.getString("username"));
    context.setDatabasePassword(data.getString("password"));
    context.setSql2o(new Sql2o(context.getDatabaseAddress(), context.getDatabaseUsername(),
        context.getDatabasePassword()));
  }

}
