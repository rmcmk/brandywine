package me.ryleykimmel.brandywine.common.util;

import com.google.common.io.ByteStreams;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import me.ryleykimmel.brandywine.common.Buffer;

/**
 * A static-utility class containing containing extension or helper methods to compress and
 * decompress data.
 */
public final class CompressionUtil {

  /**
   * Uncompresses the GZIP {@code byte} array.
   *
   * @param bytes The compressed bytes.
   * @return The uncompressed bytes.
   * @throws IOException If some I/O exception occurs.
   */
  public static byte[] gunzip(byte[] bytes) throws IOException {
    return toByteArray(new GZIPInputStream(new ByteArrayInputStream(bytes)));
  }

  /**
   * Uncompresses the GZIP Buffer.
   *
   * @param buffer The compressed Buffer.
   * @return The uncompressed bytes, as a Buffer.
   * @throws IOException If some I/O exception occurs.
   */
  public static Buffer gunzip(Buffer buffer) throws IOException {
    return Buffer.wrap(gunzip(buffer.getBytes()));
  }

  /**
   * Uncompresses a BZIP2 {@code byte} array without the header.
   *
   * @param bytes The compressed bytes.
   * @return The uncompressed bytes.
   * @throws IOException If some I/O exception occurs.
   */
  public static byte[] bunzip2(byte[] bytes) throws IOException {
    byte[] bzip2 = new byte[bytes.length + 4];
    bzip2[0] = 'B';
    bzip2[1] = 'Z';
    bzip2[2] = 'h';
    bzip2[3] = '1';
    System.arraycopy(bytes, 0, bzip2, 4, bytes.length);

    return toByteArray(new BZip2CompressorInputStream(new ByteArrayInputStream(bzip2)));
  }

  /**
   * Uncompresses a BZIP2 Buffer without the header.
   *
   * @param buffer The compressed Buffer.
   * @return The uncompressed bytes, as a Buffer.
   * @throws IOException If some I/O exception occurs.
   */
  public static Buffer bunzip2(Buffer buffer) throws IOException {
    return Buffer.wrap(bunzip2(buffer.getBytes()));
  }

  /**
   * Copies all {@code byte}s from the specified InputStream into a {@code byte} array.
   * <p>
   * This method closes the InputStream.
   * </p>
   *
   * @param is The InputStream to read from.
   * @return A {@code byte} array containing all of the data from the specified InputStream.
   * @throws IOException If some I/O exception occurs.
   */
  private static byte[] toByteArray(InputStream is) throws IOException {
    try {
      return ByteStreams.toByteArray(is);
    } finally {
      is.close();
    }
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private CompressionUtil() {}

}
