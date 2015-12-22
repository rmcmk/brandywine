package me.ryleykimmel.brandywine.parser;

import java.io.BufferedReader;
import java.nio.file.Files;

import com.google.gson.Gson;

/**
 * Represents a Json {@link ReaderParser}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com> @param <T> The parsed Json data.
 */
public abstract class JsonParser<T> extends ReaderParser<T> {

  /**
   * An instance of Gson, used to parse and provide Json data from the source.
   */
  protected final Gson gson = new Gson();

  /**
   * The type of the parsed Json data.
   */
  private final Class<T> type;

  /**
   * Constructs a new {@link JsonParser} with the specified path and data type.
   *
   * @param path The path to the source. @param type The type of the parsed Json data.
   */
  public JsonParser(String path, Class<T> type) {
    super(path);
    this.type = type;
  }

  @Override
  public final void parse() throws Exception {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
      T data = gson.fromJson(reader, type);
      parse(reader, data);
    }
  }

}
