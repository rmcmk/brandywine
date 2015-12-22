package me.ryleykimmel.brandywine.network.game.frame;

/**
 * Represents the access mode for frame buffers.
 *
 * @author Graham @author Ryley Kimmel <ryley.kimmel@live.com>
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
