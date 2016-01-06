package me.ryleykimmel.brandywine.parser;

import java.io.BufferedReader;
import java.nio.file.Files;

import com.google.common.base.Preconditions;
import com.moandjiezana.toml.Toml;

/**
 * Represents a Toml {@link ReaderParser}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class TomlParser extends ReaderParser<Toml> {

  /**
   * An instance of Toml, used to parse and provide Toml data from the source.
   */
  private final Toml toml = new Toml();

  /**
   * Constructs a new {@link TomlParser} with the specified path.
   *
   * @param path The path to the source.
   */
  public TomlParser(String path) {
    super(path);
  }

  @Override
  public final void parse() throws Exception {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      Toml data = toml.parse(reader);
      parse(reader, data);
    }
  }

  /**
   * A helper method used to get Toml 64-bit integers as 32-bit Java integers.
   *
   * @param toml The Toml instance to get the integer from.
   * @param key The Toml key of the integer.
   * @return The 32-bit Java integer.
   */
  public final int getInteger(Toml toml, String key) {
    Long value = Preconditions.checkNotNull(toml.getLong(key));
    return value.intValue();
  }

  /**
   * A helper method used to get Toml 64-bit integers as 32-bit Java integers.
   *
   * @param key The Toml key of the integer.
   * @return The 32-bit Java integer.
   */
  public final int getInteger(String key) {
    return getInteger(toml, key);
  }

}
