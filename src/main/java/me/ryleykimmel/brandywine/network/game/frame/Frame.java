package me.ryleykimmel.brandywine.network.game.frame;

import java.util.Objects;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;

/**
 * Represents a Frame.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Frame extends DefaultByteBufHolder {

  /**
   * The opcode or identifier of this Frame.
   */
  private final int opcode;

  /**
   * The type of this Frame.
   */
  private final FrameType type;

  /**
   * The length of this Frame.
   */
  private final int length;

  /**
   * Constructs a new {@link Frame} with the specified opcode, FrameType and payload.
   *
   * @param opcode The opcode or identifier of this Frame.
   * @param type The type of this Frame.
   * @param payload The payload of this Frame.
   */
  public Frame(int opcode, FrameType type, ByteBuf payload) {
    super(payload);
    this.opcode = opcode;
    this.type = type;
    this.length = payload.readableBytes();
  }

  /**
   * Constructs a new fixed, empty Frame for the specified opcode.
   * 
   * @param opcode The opcode or identifier of this Frame.
   */
  public Frame(int opcode) {
    this(opcode, FrameType.EMPTY, Unpooled.EMPTY_BUFFER);
  }

  /**
   * Gets the opcode of this Frame.
   *
   * @return The opcode or identifier of this Frame.
   */
  public int getOpcode() {
    return opcode;
  }

  /**
   * Gets the length (readable bytes) of this Frame.
   *
   * @return The length of this Frame.
   */
  public int getLength() {
    return length;
  }

  /**
   * Gets the type of this Frame.
   *
   * @return The type of this Frame.
   */
  public FrameType getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(opcode, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Frame) {
      Frame other = (Frame) obj;
      return opcode == other.opcode && type == other.type;
    }

    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("opcode", opcode).add("type", type).toString();
  }

}
