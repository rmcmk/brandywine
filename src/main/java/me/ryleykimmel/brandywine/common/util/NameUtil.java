package me.ryleykimmel.brandywine.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A static utility class containing extension or helper methods for {@link Player} names.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Graham
 */
public final class NameUtil {

	/**
	 * Decodes the base 37 value into a String.
	 *
	 * @param value The String, expressed in base 37 as a {@code long}.
	 * @return The decoded String.
	 */
	public static String decodeBase37(long value) {
		char[] chars = new char[12];

		int length = 0;

		while (value != 0) {
			int remainder = (int) (value % 37);
			value /= 37;

			char character;
			if (remainder >= 1 && remainder <= 26) {
				character = (char) ('a' + remainder - 1);
			} else if (remainder >= 27 && remainder <= 36) {
				character = (char) ('0' + remainder - 27);
			} else {
				character = '_';
			}

			chars[chars.length - length - 1] = character;
		}

		return new String(chars, chars.length - length, length);
	}

	/**
	 * Encodes the specified String into base 37, storing the result in a {@code long}.
	 *
	 * @param string The String to encode.
	 * @return The long containing the String, expressed in base 37.
	 */
	public static long encodeBase37(String string) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(string) || string.length() < 13, "Input string must be [1, 12] and non-null.");

		long encoded = 0;

		for (int index = 0; index < string.length(); index++) {
			char character = string.charAt(index);
			encoded *= 37;

			if (character >= 'A' && character <= 'Z') {
				encoded += character - 'A' + 1;
			} else if (character >= 'a' && character <= 'z') {
				encoded += character - 'a' + 1;
			} else if (character >= '0' && character <= '9') {
				encoded += character - '0' + 26 + 1;
			} else if (character != '_') {
				throw new IllegalArgumentException("Illegal character in string: must be [a-zA-Z0-9_].");
			}
		}

		while (encoded % 37 == 0 && encoded != 0) {
			encoded /= 37;
		}

		return encoded;
	}

	/**
	 * Sole private constructor to discourage instantiation of this class.
	 */
	private NameUtil() {
	}

}