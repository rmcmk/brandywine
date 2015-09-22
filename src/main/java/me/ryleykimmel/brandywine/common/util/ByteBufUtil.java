package me.ryleykimmel.brandywine.common.util;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import me.ryleykimmel.brandywine.network.game.frame.FrameConstants;

/**
 * A static-utility class containing extension or helper methods for {@link ByteBuf}s.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ByteBufUtil {

	/**
	 * Writes the specified null-terminated String to the specified ByteBuf.
	 *
	 * @param buffer The ByteBuf to write the String to.
	 * @param string The String to write.
	 */
	public static void writeString(ByteBuf buffer, String string) {
		writeString(buffer, string, FrameConstants.DEFAULT_STRING_TERMINATOR);
	}

	/**
	 * Writes the specified newline-terminated String to the specified ByteBuf.
	 *
	 * @param buffer The ByteBuf to write the String to.
	 * @param string The String to write.
	 */
	public static void writeJString(ByteBuf buffer, String string) {
		writeString(buffer, string, FrameConstants.J_STRING_TERMINATOR);
	}

	/**
	 * Gets a null-terminated String from the specified ByteBuf.
	 *
	 * @param buffer The ByteBuf to read from.
	 * @return The null-terminated String.
	 */
	public static String getString(ByteBuf buffer) {
		return readString(buffer, FrameConstants.DEFAULT_STRING_TERMINATOR);
	}

	/**
	 * Gets a newline-terminated String from the specified ByteBuf.
	 *
	 * @param buffer The ByteBuf to read from.
	 * @return The newline-terminated String.
	 */
	public static String readJString(ByteBuf buffer) {
		return readString(buffer, FrameConstants.J_STRING_TERMINATOR);
	}

	/**
	 * Writes the specified String to the specified ByteBuf, followed by the specified terminator, indicating the end of the written String.
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
	 * Gets a {@link String} from the specified {@link ByteBuf}, the ByteBuf will continue to get until the specified {@code terminator} is reached.
	 * <p>
	 * We use a {@link ByteArrayOutputStream} as it is self expanding. We don't want to waste precious time determining a fixed length for the {@code String}.
	 * </p>
	 *
	 * @param buffer The ByteBuf to read from.
	 * @param terminator The terminator which denotes when to stop reading.
	 * @return The read String.
	 */
	public static String readString(ByteBuf buffer, char terminator) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		for (;;) {
			int read = buffer.readUnsignedByte();
			if (read == terminator) {
				break;
			}
			os.write(read);
		}

		return new String(os.toByteArray());
	}

	/**
	 * Sole private constructor to discourage instantiation of this class.
	 */
	private ByteBufUtil() {
	}

}