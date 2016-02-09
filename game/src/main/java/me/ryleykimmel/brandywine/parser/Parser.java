package me.ryleykimmel.brandywine.parser;

/**
 * Represents some Parser, parsing data from the specified source.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The data that was parsed.
 * @param <V> The source of the data.
 */
public interface Parser<T, V> {

  /**
   * Parses data from the specified source.
   *
   * @param source The source of the data.
   * @param data The parsed data.
   * @throws Exception If some error occurs.
   */
  void parse(V source, T data) throws Exception;

  /**
   * Parses data from the specified source.
   * <p>
   * This method is specific to the Parser implementation.
   * </p>
   *
   * @throws Exception If some error occurs.
   */
  void parse() throws Exception;

}
