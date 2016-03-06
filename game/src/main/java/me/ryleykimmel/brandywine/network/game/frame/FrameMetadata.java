package me.ryleykimmel.brandywine.network.game.frame;

import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import me.ryleykimmel.brandywine.common.Assertions;

/**
 * Meta information configuration for Frames.
 */
public final class FrameMetadata {

  /**
   * The maximum allowed Frame opcode.
   */
  public static final int MAXIMUM_OPCODE = 256;

  /**
   * The length of a variable byte Frame.
   */
  public static final int VARIABLE_BYTE_LENGTH = -1;

  /**
   * The length of a variable short Frame.
   */
  public static final int VARIABLE_SHORT_LENGTH = -2;

  /**
   * The name of this Frame.
   */
  private final String name;

  /**
   * The opcode of the Frame.
   */
  private final int opcode;

  /**
   * The length of the Frame.
   */
  private final int length;

  /**
   * A flag denoting whether or not this Frame is ciphered.
   */
  private final boolean ciphered;

  /**
   * Constructs a new {@link FrameMetadata}.
   * 
   * @param name The name of this Frame.
   * @param opcode The identifier. Must be greater than zero and less than the
   * {@link FrameMetadata#MAXIMUM_OPCODE},
   * @param length The length of the Frame. Must be either greater than or equal to zero or one of:
   * {@link FrameMetadata#VARIABLE_BYTE_LENGTH} or {@link FrameMetadata#VARIABLE_SHORT_LENGTH}.
   * @param ciphered Whether or not this Frame is ciphered.
   */
  public FrameMetadata(String name, int opcode, int length, boolean ciphered) {
    Assertions.checkWithin(ciphered ? 0 : -1, MAXIMUM_OPCODE, opcode, "Invalid opcode: " + opcode);
    if (length < 0) {
      Preconditions.checkArgument(length == VARIABLE_BYTE_LENGTH || length == VARIABLE_SHORT_LENGTH,
          "Invalid length: " + length);
    }
    this.name = name;
    this.opcode = opcode;
    this.length = length;
    this.ciphered = ciphered;
  }

  /**
   * Gets the name of this Frame.
   * 
   * @return The name of this Frame.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the opcode of this Frame.
   * 
   * @return The opcode of this Frame.
   */
  public int getOpcode() {
    return opcode;
  }

  /**
   * Gets the length of this Frame.
   * 
   * @return The length of this Frame.
   */
  public int getLength() {
    return length;
  }

  /**
   * Gets whether or not this Frame is ciphered.
   * 
   * @return {@code true} if this Frame is ciphered.
   */
  public boolean isCiphered() {
    return ciphered;
  }

  /**
   * Tests whether or not this Frame is headless.
   * <p>
   * A headless Frame has a fixed length of <tt>0</tt> and <tt>-1</tt> as the opcode.
   * </p>
   * 
   * @return {@code true} iff this Frame is headless.
   */
  public boolean isHeadless() {
    return opcode == -1 && length == 0;
  }

  /**
   * Tests whether or not the represented Frame has a variable length.
   * 
   * @return {@code true} if this Frame has a variable length, otherwise {@code false}.
   */
  public boolean hasVariableLength() {
    return length == VARIABLE_BYTE_LENGTH || length == VARIABLE_SHORT_LENGTH;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", name).add("opcode", opcode)
        .add("length", length).add("ciphered", ciphered).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, opcode, length, ciphered);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof FrameMetadata) {
      FrameMetadata other = (FrameMetadata) obj;
      return name.equals(other.name) && other.opcode == opcode && other.length == length
          && other.ciphered == ciphered;
    }

    return false;
  }

}
