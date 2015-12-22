package me.ryleykimmel.brandywine.fs;

import com.google.common.base.Preconditions;

import me.ryleykimmel.brandywine.common.Buffer;

/**
 * An {@link Index} points to a file in the {@code main_file_cache.dat} file.
 * 
 * @author Graham
 */
public final class Index {

  /**
   * The amount of bytes required to store an index.
   */
  public static final int BYTES = Byte.BYTES * 6;

  /**
   * The first block of the file.
   */
  private final int block;

  /**
   * The size of the file.
   */
  private final int size;

  /**
   * Creates the index.
   * 
   * @param size The size of the file. @param block The first block of the file.
   */
  public Index(int size, int block) {
    this.size = size;
    this.block = block;
  }

  /**
   * Decodes the {@link Buffer} into an Index.
   * 
   * @param buffer The buffer. @return The index. @throws IllegalArgumentException If the buffer
   * length is invalid.
   */
  public static Index decode(Buffer buffer) {
    Preconditions.checkArgument(buffer.remaining() == BYTES,
        "Incorrect buffer length: " + buffer.remaining() + ", expected: " + BYTES);

    int size = buffer.getUnsignedTriByte();
    int block = buffer.getUnsignedTriByte();

    return new Index(size, block);
  }

  /**
   * Gets the first block of the file.
   * 
   * @return The first block of the file.
   */
  public int getBlock() {
    return block;
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
