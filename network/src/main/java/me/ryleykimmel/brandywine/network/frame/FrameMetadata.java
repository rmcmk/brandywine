package me.ryleykimmel.brandywine.network.frame;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import me.ryleykimmel.brandywine.common.Assertions;

import java.util.Objects;

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
   * The opcode of the Frame.
   */
  private final int opcode;

  /**
   * The length of the Frame.
   */
  private final int length;

  /**
   * Constructs a new {@link FrameMetadata}.
   *
   * @param opcode The identifier. Must be greater than zero and less than {@link FrameMetadata#MAXIMUM_OPCODE},
   * @param length The length of the Frame. Must be either greater than or equal to zero or one of: {@link FrameMetadata#VARIABLE_BYTE_LENGTH} or {@link FrameMetadata#VARIABLE_SHORT_LENGTH}.
   */
  public FrameMetadata(int opcode, int length) {
    Assertions.checkWithin(0, MAXIMUM_OPCODE, opcode, "Invalid opcode: " + opcode);
    if (length < 0) {
      Preconditions.checkArgument(length == VARIABLE_BYTE_LENGTH || length == VARIABLE_SHORT_LENGTH, "Invalid length: " + length);
    }
    this.opcode = opcode;
    this.length = length;
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
   * Tests whether or not the represented Frame has a variable length.
   *
   * @return {@code true} if this Frame has a variable length, otherwise {@code false}.
   */
  public boolean hasVariableLength() {
    return length == VARIABLE_BYTE_LENGTH || length == VARIABLE_SHORT_LENGTH;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("opcode", opcode).add("length", length).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(opcode, length);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof FrameMetadata) {
      FrameMetadata other = (FrameMetadata) obj;
      return other.opcode == opcode && other.length == length;
    }

    return false;
  }

}
