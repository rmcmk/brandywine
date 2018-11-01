package me.ryleykimmel.brandywine.common.util;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import me.ryleykimmel.brandywine.common.Buffer;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

/**
 * A static-utility class containing containing extension or helper methods to compress and decompress data.
 */
public final class CompressionUtil {

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private CompressionUtil() {
  }

  /**
   * Uncompresses the GZIP {@code byte} array.
   *
   * @param bytes The compressed bytes.
   * @return The uncompressed bytes.
   * @throws IOException If some I/O exception occurs.
   */
  public static byte[] gunzip(byte[] bytes) throws IOException {
    try (InputStream is = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
      return ByteStreams.toByteArray(is);
    }
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

    try (InputStream is = new BZip2CompressorInputStream(new ByteArrayInputStream(bzip2))) {
      return ByteStreams.toByteArray(is);
    }
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

}
