package me.ryleykimmel.brandywine.network.frame;

/**
 * Represents the access mode for frame buffers.
 */
public enum AccessMode {

  /**
   * Indicates that the FrameBuffer is currently writing or reading {@code byte}s.
   */
  BYTE_ACCESS,

  /**
   * Indicates that the FrameBuffer is currently writing or reading bits.
   */
  BIT_ACCESS

}
