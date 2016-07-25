package me.ryleykimmel.brandywine.common.util;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * A static-utility class containing extension or helper methods for {@link ByteBuf}s.
 */
public final class ByteBufUtil {

  /**
   * The terminator used within the client, equal to <tt>10</tt> and otherwise know as the Jagex
   * {@code String} terminator.
   */
  public static final char J_STRING_TERMINATOR = '\n';

  /**
   * The default {@code String} terminator, equal to <tt>0</tt> and otherwise known as the 'null'
   * {@code String} terminator.
   */
  public static final char DEFAULT_STRING_TERMINATOR = '\0';

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private ByteBufUtil() {
  }

  /**
   * Writes the specified null-terminated String to the specified ByteBuf.
   *
   * @param buffer The ByteBuf to write the String to.
   * @param string The String to write.
   */
  public static void writeString(ByteBuf buffer, String string) {
    writeString(buffer, string, DEFAULT_STRING_TERMINATOR);
  }

  /**
   * Writes the specified newline-terminated String to the specified ByteBuf.
   *
   * @param buffer The ByteBuf to write the String to.
   * @param string The String to write.
   */
  public static void writeJString(ByteBuf buffer, String string) {
    writeString(buffer, string, J_STRING_TERMINATOR);
  }

  /**
   * Gets a null-terminated String from the specified ByteBuf.
   *
   * @param buffer The ByteBuf to read from.
   * @return The null-terminated String.
   */
  public static String getString(ByteBuf buffer) {
    return readString(buffer, DEFAULT_STRING_TERMINATOR);
  }

  /**
   * Gets a newline-terminated String from the specified ByteBuf.
   *
   * @param buffer The ByteBuf to read from.
   * @return The newline-terminated String.
   */
  public static String readJString(ByteBuf buffer) {
    return readString(buffer, J_STRING_TERMINATOR);
  }

  /**
   * Writes the specified String to the specified ByteBuf, followed by the specified terminator,
   * indicating the end of the written String.
   *
   * @param buffer The ByteBuf to write the String to.
   * @param string The String to write.
   * @param terminator The terminator, indicating the end of the String.
   */
  private static void writeString(ByteBuf buffer, String string, char terminator) {
    buffer.writeBytes(string.getBytes(StandardCharsets.UTF_8));
    buffer.writeByte(terminator);
  }

  /**
   * Gets a {@link String} from the specified {@link ByteBuf}, the ByteBuf will continue to get
   * until the specified {@code terminator} is reached.
   * <p>
   * We use a {@link ByteArrayOutputStream} as it is self expanding. We don't want to waste precious
   * time determining a fixed length for the {@code String}.
   * </p>
   *
   * @param buffer The ByteBuf to read from.
   * @param terminator The terminator which denotes when to stop reading.
   * @return The read String.
   */
  public static String readString(ByteBuf buffer, char terminator) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();

    for (; ; ) {
      int read = buffer.readUnsignedByte();
      if (read == terminator) {
        break;
      }
      os.write(read);
    }

    return new String(os.toByteArray());
  }

}
