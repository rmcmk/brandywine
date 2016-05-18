package me.ryleykimmel.brandywine.common;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;

/**
 * A wrapper for {@link ByteBuffer} that adds methods to read unsigned data types and specific
 * string types. All methods read and places values in big-endian format unless otherwise stated.
 */
public final class Buffer {

  /**
   * Allocates a new buffer.
   * 
   * @param bytes The capacity of the buffer.
   * @return The buffer.
   */
  public static Buffer allocate(int bytes) {
    Assertions.checkNonNegative(bytes, "Buffer capacity cannot be negative.");
    return new Buffer(bytes);
  }

  /**
   * Wraps the specified byte array in a new buffer.
   * 
   * @param bytes The byte array to wrap.
   * @return The buffer.
   */
  public static Buffer wrap(byte[] bytes) {
    return new Buffer(bytes);
  }

  /**
   * Wraps the specified {@link ByteBuffer} in a new buffer.
   * 
   * @param buffer The byte buffer.
   * @return The buffer.
   */
  public static Buffer wrap(ByteBuffer buffer) {
    return new Buffer(buffer);
  }

  /**
   * The byte buffer backing this buffer.
   */
  private final ByteBuffer buffer;

  /**
   * Creates The buffer.
   * 
   * @param bytes The byte array to wrap.
   */
  private Buffer(byte[] bytes) {
    this(ByteBuffer.wrap(bytes));
  }

  /**
   * Creates The buffer.
   * 
   * @param buffer The backing {@link ByteBuffer} .
   */
  private Buffer(ByteBuffer buffer) {
    this.buffer = buffer;
  }

  /**
   * Creates The buffer.
   * 
   * @param bytes The capacity of the buffer.
   */
  private Buffer(int bytes) {
    this(ByteBuffer.allocate(bytes));
  }

  /**
   * Gets the array of bytes backing this buffer.
   * 
   * @return The array of bytes.
   */
  public byte[] array() {
    return buffer.array();
  }

  /**
   * Returns a shallow copy of this buffer as a read-only buffer.
   * 
   * @return The read- only buffer.
   */
  public Buffer asReadOnlyBuffer() {
    return new Buffer(buffer.asReadOnlyBuffer());
  }

  /**
   * Gets the capacity of this buffer.
   * 
   * @return The capacity.
   */
  public int capacity() {
    return buffer.capacity();
  }

  /**
   * Clears this buffer.
   * 
   * @return This buffer, for chaining.
   */
  public Buffer clear() {
    buffer.clear();
    return this;
  }

  /**
   * Copies the data in this Buffer to a new Buffer.
   * 
   * @return The copied Buffer.
   */
  public Buffer copy() {
    ByteBuffer copy = ByteBuffer.allocate(buffer.limit());
    copy.put(buffer).flip();
    buffer.flip();
    return new Buffer(copy);
  }

  /**
   * Duplicates this buffer (see {@link ByteBuffer#duplicate}).
   * 
   * @return The duplicate buffer.
   */
  public Buffer duplicate() {
    return new Buffer(buffer.duplicate());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Buffer) {
      Buffer other = (Buffer) obj;
      return buffer.equals(other.buffer);
    }

    return false;
  }

  /**
   * Fills <strong>this </strong> Buffer with data from the specified Buffer. This method fills this
   * Buffer until it is full (i.e. {@code buffer.remaining = 0} ) and so the source Buffer must have
   * more bytes remaining than this Buffer. This method flips this Buffer after filling.
   * 
   * @param source The source Buffer.
   * @return This Buffer, for chaining.
   */
  public Buffer fill(Buffer source) {
    int remaining = remaining(), sourcePosition = source.position();

    Preconditions.checkArgument(remaining <= source.remaining(),
        "Source buffer must not have less remaining bytes than this buffer.");

    buffer.put(source.array(), sourcePosition, remaining);
    source.position(sourcePosition + remaining);
    return flip();
  }

  /**
   * Flips this Buffer.
   * 
   * @return This Buffer, for chaining.
   */
  public Buffer flip() {
    buffer.flip();
    return this;
  }

  /**
   * Places bytes from <strong>this </strong> buffer into the specified buffer, writing until the
   * specified buffer is filled (i.e. {@code buffer.remaining() == 0} ).
   *
   * @param buffer The byte buffer.
   * @return This buffer, for chaining.
   */
  public Buffer get(Buffer buffer) {
    this.buffer.get(buffer.array(), buffer.position(), buffer.remaining());
    return this;
  }

  /**
   * Gets {@code bytes.length} bytes and places them in the specified byte array.
   * 
   * @param bytes The byte array.
   * @return This buffer, for chaining.
   */
  public Buffer get(byte[] bytes) {
    buffer.get(bytes);
    return this;
  }

  /**
   * Gets {@code length} bytes and places them in the specified byte array, starting from {@code
   * offset}.
   *
   * @param bytes The byte array.
   * @param offset The byte array offset.
   * @param length The amount of bytes to place.
   * @return This buffer, for chaining.
   */
  public Buffer get(byte[] bytes, int offset, int length) {
    buffer.get(bytes, offset, length);
    return this;
  }

  /**
   * Gets the value at the specified index.
   * 
   * @param index The index.
   * @return The value.
   */
  public byte get(int index) {
    return buffer.get(index);
  }

  /**
   * Gets a byte from this buffer.
   * 
   * @return The byte.
   */
  public int getByte() {
    return buffer.get();
  }

  /**
   * Gets the byte buffer backing this buffer.
   * 
   * @return The byte buffer.
   */
  public ByteBuffer getByteBuffer() {
    return buffer;
  }

  /**
   * Reads the remaining data in this buffer into a byte array, and returns the array.
   *
   * @return The byte array containing the remaining data.
   */
  public byte[] getBytes() {
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    return bytes;
  }

  /**
   * Gets an int from this buffer.
   * 
   * @return The int.
   */
  public int getInt() {
    return buffer.getInt();
  }

  /**
   * Reads an int from the specified index.
   * 
   * @param index The index.
   * @return The value.
   */
  public int getInt(int index) {
    return buffer.getInt(index);
  }

  /**
   * Gets a long from this buffer.
   * 
   * @return The long.
   */
  public long getLong() {
    return buffer.getLong();
  }

  /**
   * Gets a short from this buffer.
   * 
   * @return The short.
   */
  public int getShort() {
    return buffer.getShort();
  }

  /**
   * Reads a 'smart' from this buffer.
   * 
   * @return The smart.
   */
  public int getSmart() {
    int peek = buffer.get(buffer.position()) & 0xFF;
    if (peek < 128) {
      return getUnsignedByte();
    }

    return getUnsignedShort() - 32768;
  }

  /**
   * Gets a string terminated with the byte value {@code 10} from this buffer.
   * 
   * @return The string.
   */
  public String getString() {
    StringBuilder builder = new StringBuilder();
    char character;
    while ((character = (char) buffer.get()) != 10) {
      builder.append(character);
    }

    return builder.toString();
  }

  /**
   * Gets an unsigned byte from this buffer.
   * 
   * @return The unsigned byte.
   */
  public int getUnsignedByte() {
    return buffer.get() & 0xFF;
  }

  /**
   * Gets an unsigned short from this buffer.
   * 
   * @return The unsigned short.
   */
  public int getUnsignedShort() {
    return buffer.getShort() & 0xFFFF;
  }

  /**
   * Gets a tri-byte from this buffer.
   * 
   * @return The tri- byte.
   */
  public int getUnsignedTriByte() {
    return getUnsignedByte() << 16 | getUnsignedByte() << 8 | getUnsignedByte();
  }

  @Override
  public int hashCode() {
    return buffer.hashCode();
  }

  /**
   * Returns whether or not this buffer has any bytes remaining.
   * 
   * @return {@code true} if there are any bytes remaining, {@code false} if not.
   */
  public boolean hasRemaining() {
    return buffer.hasRemaining();
  }

  /**
   * Returns whether or not the capacity of this Buffer is 0.
   * 
   * @return {@code true} if the capacity is 0, {@code false} if not.
   */
  public boolean isEmpty() {
    return capacity() == 0;
  }

  /**
   * Returns whether or not this buffer is read only.
   * 
   * @return {@code true} if this buffer is read only, {@code false} if not.
   */
  public boolean isReadOnly() {
    return buffer.isReadOnly();
  }

  /**
   * Gets the limit of this buffer.
   * 
   * @return The limit.
   */
  public int limit() {
    return buffer.limit();
  }

  /**
   * Sets the limit of this buffer.
   *
   * @param limit The new limit.
   * @return This buffer, for chaining.
   */
  public Buffer limit(int limit) {
    buffer.limit(limit);
    return this;
  }

  /**
   * Marks this buffer.
   * 
   * @return The buffer.
   */
  public Buffer mark() {
    buffer.mark();
    return this;
  }

  /**
   * Gets the position of this buffer.
   * 
   * @return The position.
   */
  public int position() {
    return buffer.position();
  }

  /**
   * Sets the current position of this buffer.
   * 
   * @param position The new position.
   * @return This buffer, for chaining.
   */
  public Buffer position(int position) {
    buffer.position(position);
    return this;
  }

  /**
   * Places the contents of the specified buffer into this buffer.
   * 
   * @param buffer The buffer.
   * @return This buffer, for chaining.
   */
  public Buffer put(Buffer buffer) {
    this.buffer.put(buffer.buffer);
    return this;
  }

  /**
   * Places the contents of the specified byte array into this buffer.
   * 
   * @param bytes The byte array.
   * @return This buffer, for chaining.
   */
  public Buffer put(byte[] bytes) {
    buffer.put(bytes);
    return this;
  }

  /**
   * Places the contents of the specified byte array into this buffer, starting from {@code offset}
   * and reading {@code length} bytes.
   * 
   * @param bytes The byte array.
   * @param offset The offset.
   * @param length The amount of bytes to place.
   * @return This buffer, for chaining.
   */
  public Buffer put(byte[] bytes, int offset, int length) {
    buffer.put(bytes, offset, length);
    return this;
  }

  /**
   * Puts a {@link ByteBuffer} into this buffer (see {@link ByteBuffer#put(ByteBuffer)} ).
   * 
   * @param buffer The byte buffer.
   * @return This buffer, for chaining.
   */
  public Buffer put(ByteBuffer buffer) {
    buffer.put(buffer);
    return this;
  }

  /**
   * Puts a byte into the buffer.
   * 
   * @param value The value.
   * @return This buffer, for chaining.
   */
  public Buffer put(int value) {
    buffer.put((byte) value);
    return this;
  }

  /**
   * Puts an int into this buffer.
   * 
   * @param value The int.
   * @return This buffer, for chaining.
   */
  public Buffer putInt(int value) {
    buffer.putInt(value);
    return this;
  }

  /**
   * Puts a long into this buffer.
   * 
   * @param value The long.
   * @return This buffer, for chaining.
   */
  public Buffer putLong(long value) {
    buffer.putLong(value);
    return this;
  }

  /**
   * Puts a short into this buffer.
   * 
   * @param value The short.
   * @return This buffer, for chaining.
   */
  public Buffer putShort(int value) {
    buffer.putShort((short) value);
    return this;
  }

  /**
   * Puts a tri-byte into this buffer.
   * 
   * @param value The tri- byte.
   * @return This buffer, for chaining.
   */
  public Buffer putTriByte(int value) {
    buffer.put((byte) (value >> 16));
    buffer.put((byte) (value >> 8));
    buffer.put((byte) value);
    return this;
  }

  /**
   * Gets the amount of bytes remaining in this buffer (i.e. {@link #limit} - {@link #position} ).
   * 
   * @return The amount of bytes remaining.
   */
  public int remaining() {
    return buffer.remaining();
  }

  /**
   * Resets the mark of this buffer.
   * 
   * @return This buffer, for chaining.
   */
  public Buffer reset() {
    buffer.reset();
    return this;
  }

  /**
   * Skips the specified amount of bytes.
   * 
   * @param bytes The amount of bytes to skip.
   * @return This buffer, for chaining.
   */
  public Buffer skip(int bytes) {
    buffer.position(buffer.position() + bytes);
    return this;
  }

  /**
   * Slices this buffer.
   * 
   * @return The sliced buffer.
   */
  public Buffer slice() {
    return new Buffer(buffer.slice());
  }

}
