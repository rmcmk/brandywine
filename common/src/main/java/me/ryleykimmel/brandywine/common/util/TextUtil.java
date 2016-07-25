package me.ryleykimmel.brandywine.common.util;

/**
 * A static utility class containing text-related extension or helper methods.
 */
public final class TextUtil {

  /**
   * An array of characters ordered by frequency - the elements with lower indices (generally)
   * appear more often in chat messages.
   */
  public static final char[] FREQUENCY_ORDERED_CHARS = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's',
    'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0',
    '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
    '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']'
  };

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private TextUtil() {
  }

  /**
   * Compresses the input text ({@code in}) and places the result in the {@code out} array.
   *
   * @param in The input text.
   * @param out The output array.
   * @return The number of bytes written to the output array.
   */
  public static int compress(String in, byte[] out) {
    if (in.length() > 80) {
      in = in.substring(0, 80);
    }
    in = in.toLowerCase();

    int carry = -1;
    int outPos = 0;
    for (int inPos = 0; inPos < in.length(); inPos++) {
      char c = in.charAt(inPos);
      int tblPos = 0;
      for (int i = 0; i < FREQUENCY_ORDERED_CHARS.length; i++) {
        if (c == FREQUENCY_ORDERED_CHARS[i]) {
          tblPos = i;
          break;
        }
      }
      if (tblPos > 12) {
        tblPos += 195;
      }
      if (carry == -1) {
        if (tblPos < 13) {
          carry = tblPos;
        } else {
          out[outPos++] = (byte) tblPos;
        }
      } else if (tblPos < 13) {
        out[outPos++] = (byte) ((carry << 4) + tblPos);
        carry = -1;
      } else {
        out[outPos++] = (byte) ((carry << 4) + (tblPos >> 4));
        carry = tblPos & 0xF;
      }
    }
    if (carry != -1) {
      out[outPos++] = (byte) (carry << 4);
    }
    return outPos;
  }

  /**
   * Filters invalid characters from the specified string.
   *
   * @param str The input string.
   * @return The filtered string.
   */
  public static String filterInvalidCharacters(String str) {
    char[] chars = str.toLowerCase().toCharArray();
    StringBuilder builder = new StringBuilder(chars.length);

    for (char character : chars) {
      for (char validChar : FREQUENCY_ORDERED_CHARS) {
        if (character == validChar) {
          builder.append(character);
          break;
        }
      }
    }

    return builder.toString();
  }

  /**
   * Uncompresses the compressed data ({@code in}) with the length ({@code len}) and returns the
   * uncompressed {@link String}.
   *
   * @param in The compressed input data.
   * @param len The length.
   * @return The uncompressed {@link String}.
   */
  public static String decompress(byte[] in, int len) {
    byte[] out = new byte[4096];
    int outPos = 0;
    int carry = -1;

    for (int i = 0; i < len * 2; i++) {
      int tblPos = in[i / 2] >> 4 - 4 * (i % 2) & 0xF;
      if (carry == -1) {
        if (tblPos < 13) {
          out[outPos++] = (byte) FREQUENCY_ORDERED_CHARS[tblPos];
        } else {
          carry = tblPos;
        }
      } else {
        out[outPos++] = (byte) FREQUENCY_ORDERED_CHARS[(carry << 4) + tblPos - 195];
        carry = -1;
      }
    }

    return new String(out, 0, outPos);
  }

}
