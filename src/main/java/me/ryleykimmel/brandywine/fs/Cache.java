package me.ryleykimmel.brandywine.fs;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

import com.google.common.base.Preconditions;

import me.ryleykimmel.brandywine.common.Buffer;

/**
 * Represents a Cache within a FileSystem.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Cache {

  /**
   * The size of a chunk.
   */
  public static final int CHUNK_SIZE = 512;

  /**
   * The size of a block.
   */
  public static final int BLOCK_SIZE = Sector.BYTES + CHUNK_SIZE;

  /**
   * The index this Cache is within.
   */
  private final int indexId;

  /**
   * The data channel.
   */
  private final SeekableByteChannel dataChannel;

  /**
   * The index channel.
   */
  private final SeekableByteChannel indexChannel;

  /**
   * Constructs a new {@link Cache} with the specified index id, data channel and index channel.
   * 
   * @param indexId The index this Cache is within.
   * @param dataChannel The data channel.
   * @param indexChannel The index channel.
   */
  public Cache(int indexId, SeekableByteChannel dataChannel, SeekableByteChannel indexChannel) {
    this.indexId = ++indexId;
    this.dataChannel = dataChannel;
    this.indexChannel = indexChannel;
  }

  /**
   * Gets the file, represented as a {@link Buffer}, with the specified id.
   * 
   * @param id The id of the file.
   * @return A Buffer representing the files content.
   * @throws IOException If some I/O exception occurs.
   */
  public Buffer getFile(int id) throws IOException {
    Index index = getIndex(id);

    int read = 0;
    int size = index.getSize(), next = index.getBlock();

    Buffer buffer = Buffer.allocate(size);

    for (int block = 0; read < size; block++) {
      Sector sector = getSector(next);
      Preconditions.checkArgument(block == sector.getBlock());

      int bytes = Math.min(CHUNK_SIZE, size - read);

      long position = (long) next * BLOCK_SIZE + Sector.BYTES;
      Buffer chunk = Buffer.allocate(bytes);
      synchronized (dataChannel) {
        dataChannel.position(position);
        dataChannel.read(chunk.getByteBuffer());
      }
      buffer.put(chunk.flip());

      read += bytes;
      next = sector.getNextBlock();

      if (size > read) {
        Preconditions.checkArgument(sector.getIndex() == indexId);
        Preconditions.checkArgument(sector.getId() == id);
      }
    }

    return buffer.flip().copy();
  }

  /**
   * Gets the Sector for the specified block.
   * 
   * @param block The block.
   * @return The Sector.
   * @throws IOException If some I/O exception occurs.
   */
  private Sector getSector(int block) throws IOException {
    long position = (long) block * BLOCK_SIZE;

    Buffer buffer = Buffer.allocate(BLOCK_SIZE);
    synchronized (dataChannel) {
      dataChannel.position(position);
      dataChannel.read(buffer.getByteBuffer());
    }
    buffer.flip();

    return Sector.decode(buffer);
  }

  /**
   * Gets the Index for the specified id.
   * 
   * @param id The id of the Index.
   * @return The Index.
   * @throws IOException If some I/O exception occurs.
   */
  private Index getIndex(int id) throws IOException {
    long position = (long) id * Index.BYTES;

    Buffer buffer = Buffer.allocate(Index.BYTES);
    synchronized (indexChannel) {
      indexChannel.position(position);
      indexChannel.read(buffer.getByteBuffer());
    }
    buffer.flip();

    return Index.decode(buffer);
  }

}
