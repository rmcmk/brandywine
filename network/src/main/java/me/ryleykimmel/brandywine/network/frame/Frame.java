package me.ryleykimmel.brandywine.network.frame;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;

/**
 * Represents a Frame.
 */
public final class Frame extends DefaultByteBufHolder {

  /**
   * The metadata for this Frame.
   */
  private final FrameMetadata metadata;

  /**
   * The length (readable bytes) of this Frame.
   */
  private final int length;

  /**
   * Constructs a new {@link Frame} with the FrameMetadata and payload.
   *
   * @param metadata The metadata for this Frame.
   * @param payload The payload of this Frame.
   */
  public Frame(FrameMetadata metadata, ByteBuf payload) {
    super(payload);
    this.metadata = metadata;
    this.length = payload.readableBytes();
  }

  /**
   * Constructs a new Frame with an empty payload.
   *
   * @param metadata The metadata for this Frame.
   */
  public Frame(FrameMetadata metadata) {
    this(metadata, Unpooled.EMPTY_BUFFER);
  }

  /**
   * Gets the metadata for this Frame.
   *
   * @return The metadata for this Frame.
   */
  public FrameMetadata getMetadata() {
    return metadata;
  }

  /**
   * Gets the opcode of this Frame.
   *
   * @return The opcode or identifier of this Frame.
   */
  public int getOpcode() {
    return metadata.getOpcode();
  }

  /**
   * Gets the length (readable bytes) of this Frame.
   *
   * @return The length of this Frame.
   */
  public int getLength() {
    return length;
  }

}
