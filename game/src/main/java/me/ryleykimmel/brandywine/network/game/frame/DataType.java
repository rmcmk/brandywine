package me.ryleykimmel.brandywine.network.game.frame;

/**
 * Represents primitive data types, used to indicate to a frame buffer what value to read or write.
 */
public enum DataType {

  /**
   * Represents a {@code byte}.
   */
  BYTE(Byte.BYTES),

  /**
   * Represents a {@code short}.
   */
  SHORT(Short.BYTES),

  /**
   * Represents an {@code int}.
   */
  INT(Integer.BYTES),

  /**
   * Represents a {@code long}.
   */
  LONG(Long.BYTES);

  /**
   * The amount of {@code byte}s a single data type consumes.
   */
  private final int bytes;

  /**
   * Constructs a new {@link DataType} with the specified amount of {@code byte}s it consumes.
   *
   * @param bytes The amount of {@code byte}s a signle data type consumes.
   */
  private DataType(int bytes) {
    this.bytes = bytes;
  }

  /**
   * Gets the amount of {@code byte}s this data type consumes.
   *
   * @return The amount of {@code byte}s this data type consumes.
   */
  public int getBytes() {
    return bytes;
  }

}
