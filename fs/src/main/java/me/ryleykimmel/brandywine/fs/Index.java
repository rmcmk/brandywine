package me.ryleykimmel.brandywine.fs;

import com.google.common.base.Preconditions;
import me.ryleykimmel.brandywine.common.Buffer;

/**
 * An {@link Index} points to a Sector in the FileSystem.
 */
public final class Index {

  /**
   * The amount of bytes required to decode an Index.
   */
  public static final int BYTES = Byte.BYTES * 6;

  /**
   * The first Sector in this Index.
   */
  private final int sector;

  /**
   * The size of the file.
   */
  private final int size;

  /**
   * Creates the index.
   * 
   * @param size The size of the file.
   * @param sector The first Sector in this Index.
   */
  public Index(int size, int sector) {
    this.size = size;
    this.sector = sector;
  }

  /**
   * Decodes the specified {@link Buffer} into an Index.
   * 
   * @param buffer The Buffer to decode the Index from.
   * @return The new Index, never {@code null}.
   */
  public static Index decode(Buffer buffer) {
    Preconditions.checkArgument(buffer.remaining() == BYTES,
        "Incorrect buffer length: " + buffer.remaining() + ", expected: " + BYTES);

    int size = buffer.getUnsignedTriByte();
    int sector = buffer.getUnsignedTriByte();

    return new Index(size, sector);
  }

  /**
   * Gets the first Sector in this Index.
   * 
   * @return The first Sector in this Index.
   */
  public int getSector() {
    return sector;
  }

  /**
   * Gets the size of the file.
   * 
   * @return The size of the file.
   */
  public int getSize() {
    return size;
  }

}
