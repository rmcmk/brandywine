package me.ryleykimmel.brandywine.fs;

import com.google.common.base.Preconditions;
import me.ryleykimmel.brandywine.common.Buffer;

/**
 * Represents a Sector within the FileSystem.
 */
public final class Sector {

  /**
   * The minimum amount of bytes required to decode a Sector.
   */
  public static final int BYTES = Byte.BYTES * 8;

  /**
   * The id of this Sector.
   */
  private final int id;

  /**
   * A pointer to a block of data within this Sector.
   */
  private final int block;

  /**
   * The next Sector id.
   */
  private final int next;

  /**
   * The Index this Sector is in.
   */
  private final int index;

  /**
   * Constructs a new {@link Sector} with the specified id, block, next id and index.
   *
   * @param id The id of this Sector.
   * @param block A pointer to a block of data within this Sector.
   * @param next The next Sector id.
   * @param index The Index this Sector is in.
   */
  public Sector(int id, int block, int next, int index) {
    this.id = id;
    this.block = block;
    this.next = next;
    this.index = index;
  }

  /**
   * Decodes the specified {@link Buffer} into a Sector.
   *
   * @param buffer The Buffer to decode the Sector from.
   * @return The new Sector, never {@code null}.
   */
  public static Sector decode(Buffer buffer) {
    Preconditions.checkArgument(buffer.remaining() >= BYTES,
      "Incorrect buffer length: " + buffer.remaining() + ", expected: " + BYTES);

    int id = buffer.getUnsignedShort();
    int block = buffer.getUnsignedShort();
    int next = buffer.getUnsignedTriByte();
    int index = buffer.getUnsignedByte();

    return new Sector(id, block, next, index);
  }

  /**
   * Gets the id of this Sector.
   *
   * @return The id of this Sector.
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the pointer to a block of data within this Sector.
   *
   * @return The pointer to a block of data within this Sector.
   */
  public int getBlock() {
    return block;
  }

  /**
   * Gets the next Sector id.
   *
   * @return The next Sector id.
   */
  public int getNext() {
    return next;
  }

  /**
   * Gets the Index this Sector is in.
   *
   * @return The Index this Sector is in.
   */
  public int getIndex() {
    return index;
  }

}
