package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.moandjiezana.toml.Toml;

import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.parser.Parser;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * Parses {@link Parser parsers} with a specific format from a {@link TomlParser}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ParserTomlParser extends TomlParser {

  /**
   * The context of the Server.
   */
  private final ServerContext context;

  /**
   * Constructs a new {@link ParserTomlParser} with the specified path and ServerContext.
   *
   * @param path The path to the source.
   * @param context The context of the Server.
   */
  public ParserTomlParser(String path, ServerContext context) {
    super(path);
    this.context = context;
  }

  /**
   * Finds and creates Parser instances from the specified Class.
   *
   * @param clazz The Class to find the Parser instance within.
   * @param path The path to the Parsers source file.
   * @return The created Parser instance.
   * @throws NoSuchMethodException If there was no constructor found for the specified parameters.
   * @throws InvocationTargetException If the Parsers constructor throws an exception.
   * @throws IllegalAccessException If the Parsers constructor object is enforcing Java language
   * access control and the underlying constructor is inaccessible.
   * @throws InstantiationException If the Parser is an abstract class.
   */
  private Parser<?, ?> find(Class<Parser<?, ?>> clazz, String path) throws InstantiationException,
      IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Constructor<?>[] constructors = clazz.getConstructors();

    for (Constructor<?> constructor : constructors) {
      Class<?>[] params = constructor.getParameterTypes();

      switch (params.length) {
        case 1:
          Class<?> parameter = path == null ? context.getClass() : path.getClass();
          Object argument = path == null ? context : path;

          return clazz.getConstructor(parameter).newInstance(argument);
        case 2:
          return clazz.getConstructor(String.class, ServerContext.class).newInstance(path, context);
      }
    }

    String className = StringUtil.simpleClassName(clazz);
    throw new NoSuchMethodException(
        "Unable to find suitable constructor, only " + className + "(String), " + className
            + "(ServerContext) or " + className + "(String, ServerContext) are permitted.");
  }

  @SuppressWarnings("unchecked")
  @Override
  public void parse(Reader source, Toml data) throws Exception {
    for (Toml toml : data.getTables("parser")) {
      String path = toml.getString("path");
      String name = toml.getString("name");

      Class<Parser<?, ?>> clazz = (Class<Parser<?, ?>>) Class.forName(name);
      Parser<?, ?> parser = find(clazz, path);

      context.addParser(clazz, parser);
      parser.parse();
    }
  }

}
