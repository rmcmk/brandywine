package me.ryleykimmel.brandywine.network.frame;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.common.Assertions;
import me.ryleykimmel.brandywine.common.util.ByteBufUtil;
import org.apache.commons.lang3.builder.Builder;

/**
 * An implementation of a FrameBuffer which builds Frames from its contents.
 */
public final class FrameBuilder extends FrameBuffer implements Builder<Frame> {

  /**
   * The opcode of the Frame.
   */
  private final FrameMetadata metadata;

  /**
   * The ByteBufAllocator, used to allocate ByteBufs.
   */
  private final ByteBufAllocator allocator;

  /**
   * Constructs a new {@link FrameBuilder} with the specified FrameMetadata and ByteBufAllocator.
   *
   * @param metadata The metadata for the Frame.
   * @param allocator The ByteBufAllocator for allocating ByteBufs.
   */
  public FrameBuilder(FrameMetadata metadata, ByteBufAllocator allocator) {
    super(allocator.buffer());
    this.metadata = metadata;
    this.allocator = allocator;
  }

  /**
   * Constructs a new <strong>raw</strong> {@link FrameBuilder} with the specified ByteBufAllocator.
   * <p>
   * Raw FrameBuilders cannot be built as they represent no Frame and have no FrameMetadata.
   * </p>
   *
   * @param allocator The ByteBufAllocator for allocating ByteBufs.
   */
  public FrameBuilder(ByteBufAllocator allocator) {
    this(null, allocator);
  }

  @Override
  public Frame build() {
    Preconditions.checkNotNull(metadata, "Raw frame builders cannot be built.");
    checkByteAccess();
    return new Frame(metadata, buffer);
  }

  /**
   * Gets the length of this FrameBuilders backing ByteBuf.
   *
   * @return The length of the backing ByteBuf.
   */
  public int getLength() {
    checkByteAccess();
    return buffer.writerIndex();
  }

  /**
   * Writes a single number of the specified DataType in the specified DataOrder and performs the
   * specified DataTransformation if and only if this buffer is in {@link AccessMode#BYTE_ACCESS
   * byte access}.
   *
   * @param type The type of the number to write.
   * @param order The DataOrder to write the number in.
   * @param transformation The DataTransformation to perform on the number.
   * @param value The value of the number.
   */
  public void put(DataType type, DataOrder order, DataTransformation transformation, Number value) {
    checkByteAccess();

    long longValue = value.longValue();
    int length = type.getBytes();

    switch (order) {
      case LITTLE:
        for (int index = 0; index < length; index++) {
          writeAndTransform(transformation, longValue, index);
        }
        break;

      case BIG:
        for (int index = length - 1; index >= 0; index--) {
          writeAndTransform(transformation, longValue, index);
        }
        break;

      case MIDDLE:
        Preconditions.checkArgument(transformation == DataTransformation.NONE, "middle endian cannot be transformed");
        Preconditions.checkArgument(type == DataType.INT, "middle endian can only be used with an integer");

        buffer.writeByte((byte) (longValue >> 8));
        buffer.writeByte((byte) longValue);
        buffer.writeByte((byte) (longValue >> 24));
        buffer.writeByte((byte) (longValue >> 16));
        break;

      case INVERSED_MIDDLE:
        Preconditions.checkArgument(transformation == DataTransformation.NONE, "inversed middle endian cannot be transformed");
        Preconditions.checkArgument(type == DataType.INT, "inversed middle endian can only be used with an integer");

        buffer.writeByte((byte) (longValue >> 16));
        buffer.writeByte((byte) (longValue >> 24));
        buffer.writeByte((byte) longValue);
        buffer.writeByte((byte) (longValue >> 8));
        break;

      default:
        throw new UnsupportedOperationException(order + " is not supported!");
    }
  }

  private void writeAndTransform(DataTransformation transformation, long longValue, int byteIndex) {
    if (byteIndex == 0 && transformation != DataTransformation.NONE) {
      switch (transformation) {
        case ADD:
          buffer.writeByte((byte) (longValue + 128));
          break;

        case SUBTRACT:
          buffer.writeByte((byte) (128 - longValue));
          break;

        case NEGATE:
          buffer.writeByte((byte) -longValue);
          break;

        default:
          throw new UnsupportedOperationException(transformation + " is not supported!");
      }
    } else {
      buffer.writeByte((byte) (longValue >> byteIndex * 8));
    }
  }

  /**
   * Writes a single number of the specified DataType in the {@link DataOrder#BIG big data order}
   * and performs the specified DataTransformation if and only if this buffer is in
   * {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to write.
   * @param transformation The DataTransformation to perform on the number.
   * @param value The value of the number.
   */
  public void put(DataType type, DataTransformation transformation, Number value) {
    put(type, DataOrder.BIG, transformation, value);
  }

  /**
   * Writes a single number of the specified DataType in the {@link DataOrder#BIG big data order} if
   * and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to write.
   * @param value The value of the number.
   */
  public void put(DataType type, Number value) {
    put(type, DataTransformation.NONE, value);
  }

  /**
   * Writes a single number of the specified DataType in the specified DataOrder if and only if this
   * buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to write.
   * @param order The DataOrder to write the number in.
   * @param value The value of the number.
   */
  public void put(DataType type, DataOrder order, Number value) {
    put(type, order, DataTransformation.NONE, value);
  }

  /**
   * Writes the specified amount of bits with the specified value, if and only if this buffer is in
   * {@link AccessMode#BIT_ACCESS bit access}.
   *
   * @param amount The number of bits to write.
   * @param value The value.
   */
  public void putBits(int amount, int value) {
    Assertions.checkWithin(1, 32, amount, "Number of bits must be between 1 and 32 inclusive.");
    checkBitAccess();

    int bytePos = bitIndex >> 3;
    int bitOffset = 8 - (bitIndex & 7);
    bitIndex += amount;

    int requiredSpace = bytePos - buffer.writerIndex() + 1;
    requiredSpace += (amount + 7) / 8;
    buffer.ensureWritable(requiredSpace);

    for (; amount > bitOffset; bitOffset = 8) {
      int tmp = buffer.getByte(bytePos);
      tmp &= ~BIT_MASKS[bitOffset];
      tmp |= (value >> (amount - bitOffset)) & BIT_MASKS[bitOffset];
      buffer.setByte(bytePos++, tmp);
      amount -= bitOffset;
    }

    int tmp = buffer.getByte(bytePos);

    if (amount == bitOffset) {
      tmp &= ~BIT_MASKS[bitOffset];
      tmp |= value & BIT_MASKS[bitOffset];
    } else {
      tmp &= ~(BIT_MASKS[amount] << (bitOffset - amount));
      tmp |= (value & BIT_MASKS[amount]) << (bitOffset - amount);
    }

    buffer.setByte(bytePos, tmp);
  }

  /**
   * Writes the specified bytes if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte
   * access}.
   *
   * @param bytes The bytes to write.
   */
  public void putBytes(byte[] bytes) {
    checkByteAccess();
    buffer.writeBytes(bytes);
  }

  /**
   * Writes the bytes from the specified ByteBuf if and only if this buffer is in
   * {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param buf The ByteBuf to write from.
   */
  public void putBytes(ByteBuf buf) {
    checkByteAccess();
    buffer.writeBytes(buf);
  }

  /**
   * Writes all of the bytes from the specified FrameBuilder if and only if the specified
   * FrameBuilder and this FrameBuilder is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param builder The FrameBuilder to write from.
   */
  public void putBytes(FrameBuilder builder) {
    builder.checkByteAccess();
    putBytes(builder.getBuffer());
  }

  /**
   * Writes the specified bytes and performs the specified DataTransformation if and only if this
   * buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param transformation The DataTransformation to perform on the number.
   * @param bytes The bytes to write.
   */
  public void putBytes(DataTransformation transformation, byte[] bytes) {
    if (transformation == DataTransformation.NONE) {
      putBytes(bytes);
    } else {
      for (byte value : bytes) {
        put(DataType.BYTE, transformation, value);
      }
    }
  }

  /**
   * Writes the specified bytes, in reverse, if and only if this buffer is in
   * {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param bytes The bytes to write.
   */
  public void putBytesReverse(byte[] bytes) {
    checkByteAccess();
    for (int i = bytes.length - 1; i >= 0; i--) {
      buffer.writeByte(bytes[i]);
    }
  }

  /**
   * Writes the bytes from the specified ByteBuf, in reverse, if and only if this buffer is in
   * {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param buf The ByteBuf to write from.
   */
  public void putBytesReverse(ByteBuf buf) {
    byte[] bytes = new byte[buf.readableBytes()];
    buf.readBytes(bytes);
    putBytesReverse(bytes);
  }

  /**
   * Writes the specified bytes, in reverse and performs the specified DataTransformation if and
   * only if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param transformation The DataTransformation to perform on the bytes.
   * @param bytes The bytes to write.
   */
  public void putBytesReverse(DataTransformation transformation, byte[] bytes) {
    if (transformation == DataTransformation.NONE) {
      putBytesReverse(bytes);
    } else {
      for (int i = bytes.length - 1; i >= 0; i--) {
        put(DataType.BYTE, transformation, bytes[i]);
      }
    }
  }

  /**
   * Writes a single smart if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte
   * access}.
   *
   * @param value The value of the smart.
   */
  public void putSmart(int value) {
    checkByteAccess();
    if (value > Byte.MAX_VALUE) {
      buffer.writeShort(value);
    } else {
      buffer.writeByte(value);
    }
  }

  /**
   * Writes a single String if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte
   * access}.
   *
   * @param value The value of the String.
   */
  public void putString(String value) {
    checkByteAccess();
    ByteBufUtil.writeJString(buffer, value);
  }

  /**
   * Gets this FrameBuilders ByteBufAllocator.
   *
   * @return This FrameBuilders ByteBufAllocator.
   */
  public ByteBufAllocator allocator() {
    return allocator;
  }

}
