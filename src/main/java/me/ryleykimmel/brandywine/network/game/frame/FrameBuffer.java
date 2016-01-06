package me.ryleykimmel.brandywine.network.game.frame;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;

/**
 * A wrapper for an I/O (reader-writer) {@link ByteBuf} for writing and reading Frames.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
abstract class FrameBuffer {

  /**
   * Represents the type of this FrameBuffer.
   *
   * @author Ryley Kimmel <ryley.kimmel@live.com>
   */
  private enum Type {

    /**
     * Indicates that this is an input or reader FrameBuffer.
     */
    READER,

    /**
     * Indicates that this is an output or writer FrameBuffer.
     */
    WRITER

  }

  /**
   * An array of bit masks. The element {@code n} is equal to {@code 2<sup>n</sup> - 1}.
   */
  protected static final int[] BIT_MASKS = {0x0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff,
      0x3ff, 0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff,
      0x1fffff, 0x3fffff, 0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff,
      0x1fffffff, 0x3fffffff, 0x7fffffff, -1};

  /**
   * The backing ByteBuf.
   */
  protected final ByteBuf buffer;

  /**
   * The type of this FrameBuffer.
   */
  protected final Type type;

  /**
   * The current AccessMode this FrameBuffer is in, default is {@link AccessMode#BYTE_ACCESS}.
   */
  protected AccessMode mode = AccessMode.BYTE_ACCESS;

  /**
   * The current bit index of this FrameBuffer.
   */
  protected int bitIndex;

  /**
   * Constructs a new {@link FrameBuffer} with the specified ByteBuf backing and Type.
   *
   * @param buffer The backing ByteBuf.
   * @param type The type of this FrameBuffer.
   */
  private FrameBuffer(ByteBuf buffer, Type type) {
    this.buffer = buffer;
    this.type = type;
  }

  /**
   * Switches this FrameBuffers AccessMode to {@link AccessMode#BYTE_ACCESS}.
   */
  public final void switchToByteAccess() {
    checkBitAccess();
    switch (type) {
      case WRITER:
        buffer.writerIndex((bitIndex + 7) >> 3);
        break;

      case READER:
        buffer.readerIndex((bitIndex + 7) >> 3);
        break;

      default:
        throw new IllegalArgumentException(type + " is unable to switch to byte access.");
    }

    mode = AccessMode.BYTE_ACCESS;
  }

  /**
   * Switches this FrameBuffers AccessMode to {@link AccessMode#BIT_ACCESS}.
   */
  public final void switchToBitAccess() {
    checkByteAccess();
    switch (type) {
      case WRITER:
        bitIndex = buffer.writerIndex() << 3;
        break;

      case READER:
        bitIndex = buffer.readerIndex() << 3;
        break;

      default:
        throw new IllegalArgumentException(type + " is unable to switch to bit access.");
    }

    mode = AccessMode.BIT_ACCESS;
  }

  /**
   * Returns the backing ByteBuf for this FrameBuffer.
   *
   * @return The backing ByteBuf for this FrameBuffer.
   */
  protected final ByteBuf getBuffer() {
    return buffer;
  }

  /**
   * Checks if this FrameBuffer is currently within {@link AccessMode#BYTE_ACCESS}, if not an
   * IllegalArgumentException is thrown.
   */
  protected final void checkByteAccess() {
    Preconditions.checkArgument(mode == AccessMode.BYTE_ACCESS,
        "For byte-based calls to work, the mode must be byte access");
  }

  /**
   * Checks if this FrameBuffer is currently within {@link AccessMode#BIT_ACCESS}, if not an
   * IllegalArgumentException is thrown.
   */
  protected final void checkBitAccess() {
    Preconditions.checkArgument(mode == AccessMode.BIT_ACCESS,
        "For bit-based calls to work, the mode must be bit access");
  }

  /**
   * An adapter implementation of an input or reading FrameBuffer.
   *
   * @author Ryley Kimmel <ryley.kimmel@live.com>
   */
  public static abstract class ReadingFrameBuffer extends FrameBuffer {

    /**
     * Constructs a new {@link ReadingFrameBuffer} with the specified backing ByteBuf.
     *
     * @param buffer The backing ByteBuf.
     */
    public ReadingFrameBuffer(ByteBuf buffer) {
      super(buffer, Type.READER);
    }

  }

  /**
   * An adapter implementation of an output or writing FrameBuffer.
   *
   * @author Ryley Kimmel <ryley.kimmel@live.com>
   */
  public static abstract class WritingFrameBuffer extends FrameBuffer {

    /**
     * Constructs a new {@link WritingFrameBuffer} with the specified backing ByteBuf.
     *
     * @param buffer The backing ByteBuf.
     */
    public WritingFrameBuffer(ByteBuf buffer) {
      super(buffer, Type.WRITER);
    }

  }

}
