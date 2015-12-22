package me.ryleykimmel.brandywine.network.game.frame;

import com.google.common.base.Preconditions;

import me.ryleykimmel.brandywine.common.util.ByteBufUtil;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuffer.ReadingFrameBuffer;

/**
 * An implementation of a {@link ReadingFrameBuffer} which reads data from a Frames payload.
 *
 * @author Graham @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class FrameReader extends ReadingFrameBuffer {

  /**
   * Constructs a new {@link FrameReader} with the specified Frame.
   *
   * @param frame The Frame to read.
   */
  public FrameReader(Frame frame) {
    super(frame.content());
  }

  /**
   * Reads a single String if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte
   * access}.
   *
   * @return The read String.
   */
  public String getString() {
    checkByteAccess();
    return ByteBufUtil.readJString(buffer);
  }

  /**
   * Reads a single signed smart if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte
   * access}.
   *
   * @return The read smart.
   */
  public int getSignedSmart() {
    checkByteAccess();
    int peek = buffer.getUnsignedByte(buffer.readerIndex());
    int value = peek > Byte.MAX_VALUE ? buffer.readShort() - 49152 : buffer.readByte() - 64;
    return value;
  }

  /**
   * Reads a single unsigned smart if and only if this buffer is in {@link AccessMode#BYTE_ACCESS
   * byte access}.
   *
   * @return The read String.
   */
  public int getUnsignedSmart() {
    checkByteAccess();
    int peek = buffer.getUnsignedByte(buffer.readerIndex());
    int value = peek > Byte.MAX_VALUE ? buffer.readShort() + Short.MIN_VALUE : buffer.readByte();
    return value;
  }

  /**
   * Reads a single signed number of the specified DataType in the {@link DataOrder#BIG big}
   * DataOrder if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte access} .
   *
   * @param type The type of the number to read. @return The read number, signed.
   */
  public long getSigned(DataType type) {
    return getSigned(type, DataOrder.BIG, DataTransformation.NONE);
  }

  /**
   * Reads a single signed number of the specified DataType in the specified DataOrder if and only
   * if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param order The DataOrder to read the number
   * in. @return The read number, signed.
   */
  public long getSigned(DataType type, DataOrder order) {
    return getSigned(type, order, DataTransformation.NONE);
  }

  /**
   * Reads a single signed number of the specified DataType in the {@link DataOrder#BIG big}
   * DataOrder and performs the specified DataTransformation on the number if and only if this
   * buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param transformation The DataTransformation to
   * perform on the number. @return The read number, signed.
   */
  public long getSigned(DataType type, DataTransformation transformation) {
    return getSigned(type, DataOrder.BIG, transformation);
  }

  /**
   * Reads a single signed number of the specified DataType in the specified DataOrder and performs
   * the specified DataTransformation on the number if and only if this buffer is in {@link
   * AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param order The DataOrder to read the number
   * in. @param transformation The DataTransformation to perform on the number. @return The read
   * number, signed.
   */
  public long getSigned(DataType type, DataOrder order, DataTransformation transformation) {
    long longValue = get(type, order, transformation);
    if (type != DataType.LONG) {
      int max = (int) (Math.pow(2, type.getBytes() * 8 - 1) - 1);
      if (longValue > max) {
        longValue -= (max + 1) * 2;
      }
    }
    return longValue;
  }

  /**
   * Reads a single unsigned number of the specified DataType in the {@link DataOrder#BIG big}
   * DataOrder if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @return The read number, unsigned.
   */
  public long getUnsigned(DataType type) {
    return getUnsigned(type, DataOrder.BIG, DataTransformation.NONE);
  }

  /**
   * Reads a single unsigned number of the specified DataType in the specified DataOrder if and only
   * if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param order The DataOrder to read the number
   * in. @return The read number, unsigned.
   */
  public long getUnsigned(DataType type, DataOrder order) {
    return getUnsigned(type, order, DataTransformation.NONE);
  }

  /**
   * Reads a single unsigned number of the specified DataType in the {@link DataOrder#BIG big}
   * DataOrder and performs the specified DataTransformation on the number if and only if this
   * buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param transformation The DataTransformation to
   * perform on the number. @return The read number, unsigned.
   */
  public long getUnsigned(DataType type, DataTransformation transformation) {
    return getUnsigned(type, DataOrder.BIG, transformation);
  }

  /**
   * Reads a single unsigned number of the specified DataType in the specified DataOrder and
   * performs the specified DataTransformation on the number if and only if this buffer is in {@link
   * AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param order The DataOrder to read the number
   * in. @param transformation The DataTransformation to perform on the number. @return The read
   * number, unsigned.
   */
  public long getUnsigned(DataType type, DataOrder order, DataTransformation transformation) {
    Preconditions.checkArgument(type != DataType.LONG,
        "due to java restrictions, longs must be read as signed types");
    return get(type, order, transformation) & 0xFFFFFFFFFFFFFFFFL;
  }

  /**
   * Reads a single number of the specified DataType in the specified DataOrder and performs the
   * specified DataTransformation on the number if and only if this buffer is in {@link
   * AccessMode#BYTE_ACCESS byte access}.
   *
   * @param type The type of the number to read. @param order The DataOrder to read the number
   * in. @param transformation The DataTransformation to perform on the number. @return The read
   * number.
   */
  private long get(DataType type, DataOrder order, DataTransformation transformation) {
    checkByteAccess();

    long longValue = 0;
    int length = type.getBytes();

    switch (order) {
      case LITTLE:
        for (int i = 0; i < length; i++) {
          if (i == 0 && transformation != DataTransformation.NONE) {
            switch (transformation) {
              case ADD:
                longValue |= buffer.readByte() - 128 & 0xFFL;
                break;

              case SUBTRACT:
                longValue |= 128 - buffer.readByte() & 0xFFL;
                break;

              case NEGATE:
                longValue |= -buffer.readByte() & 0xFFL;
                break;

              default:
                throw new UnsupportedOperationException(transformation + " is not supported!");
            }
          } else {
            longValue |= (buffer.readByte() & 0xFFL) << i * 8;
          }
        }
        break;

      case BIG:
        for (int i = length - 1; i >= 0; i--) {
          if (i == 0 && transformation != DataTransformation.NONE) {
            switch (transformation) {
              case ADD:
                longValue |= buffer.readByte() - 128 & 0xFFL;
                break;

              case SUBTRACT:
                longValue |= 128 - buffer.readByte() & 0xFFL;
                break;

              case NEGATE:
                longValue |= -buffer.readByte() & 0xFFL;
                break;

              default:
                throw new UnsupportedOperationException(transformation + " is not supported!");
            }
          } else {
            longValue |= (buffer.readByte() & 0xFFL) << i * 8;
          }
        }
        break;

      case MIDDLE:
        Preconditions.checkArgument(transformation == DataTransformation.NONE,
            "middle endian cannot be transformed");
        Preconditions.checkArgument(type == DataType.INT,
            "middle endian can only be used with an integer");

        longValue |= (buffer.readByte() & 0xFF) << 8;
        longValue |= buffer.readByte() & 0xFF;
        longValue |= (buffer.readByte() & 0xFF) << 24;
        longValue |= (buffer.readByte() & 0xFF) << 16;
        break;

      case INVERSED_MIDDLE:
        Preconditions.checkArgument(transformation == DataTransformation.NONE,
            "inversed middle endian cannot be transformed");
        Preconditions.checkArgument(type == DataType.INT,
            "inversed middle endian can only be used with an integer");

        longValue |= (buffer.readByte() & 0xFF) << 16;
        longValue |= (buffer.readByte() & 0xFF) << 24;
        longValue |= buffer.readByte() & 0xFF;
        longValue |= (buffer.readByte() & 0xFF) << 8;
        break;

      default:
        throw new UnsupportedOperationException(order + " is not supported!");
    }

    return longValue;
  }

  /**
   * Reads the specified amount of bits if and only if this buffer is in {@link
   * AccessMode#BIT_ACCESS bit access}.
   *
   * @param amount The amount of bits to read. @return The value of the bits.
   */
  public int getBits(int amount) {
    Preconditions.checkArgument(amount >= 0 && amount <= 32,
        "Number of bits must be between 1 and 32 inclusive.");
    checkBitAccess();

    int bytePos = bitIndex >> 3;
    int bitOffset = 8 - (bitIndex & 7);
    int value = 0;
    bitIndex += amount;

    for (; amount > bitOffset; bitOffset = 8) {
      value += (buffer.getByte(bytePos++) & BIT_MASKS[bitOffset]) << amount - bitOffset;
      amount -= bitOffset;
    }

    if (amount == bitOffset) {
      value += buffer.getByte(bytePos) & BIT_MASKS[bitOffset];
    } else {
      value += buffer.getByte(bytePos) >> bitOffset - amount & BIT_MASKS[amount];
    }

    return value;
  }

  /**
   * Reads into the specified byte array if and only if this buffer is in {@link
   * AccessMode#BYTE_ACCESS byte access}.
   *
   * @param bytes The byte array to read into.
   */
  public void getBytes(byte[] bytes) {
    checkByteAccess();
    buffer.readBytes(bytes);
  }

  /**
   * Reads into the specified byte array and applies the specified data transformation if and only
   * if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param transformation The DataTransformation to perform on the bytes. @param bytes The byte
   * array to read into.
   */
  public void getBytes(DataTransformation transformation, byte[] bytes) {
    if (transformation == DataTransformation.NONE) {
      getBytes(bytes);
    } else {
      for (int i = 0; i < bytes.length; i++) {
        bytes[i] = (byte) getSigned(DataType.BYTE, transformation);
      }
    }
  }

  /**
   * Reads into the specified byte array, in reverse, if and only if this buffer is in {@link
   * AccessMode#BYTE_ACCESS byte access}.
   *
   * @param bytes The byte array to read into.
   */
  public void getBytesReverse(byte[] bytes) {
    checkByteAccess();
    for (int i = bytes.length - 1; i >= 0; i--) {
      bytes[i] = buffer.readByte();
    }
  }

  /**
   * Reads into the specified byte array, in reverse, and applies the specified data transformation
   * if and only if this buffer is in {@link AccessMode#BYTE_ACCESS byte access}.
   *
   * @param transformation The DataTransformation to perform on the bytes. @param bytes The byte
   * array to read into.
   */
  public void getBytesReverse(DataTransformation transformation, byte[] bytes) {
    if (transformation == DataTransformation.NONE) {
      getBytesReverse(bytes);
    } else {
      for (int i = bytes.length - 1; i >= 0; i--) {
        bytes[i] = (byte) getSigned(DataType.BYTE, transformation);
      }
    }
  }

}
