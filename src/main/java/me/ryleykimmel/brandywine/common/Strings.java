package me.ryleykimmel.brandywine.common;

/**
 * A static-utility class containing extension and helper methods for {@link String strings}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Strings {

  /**
   * Lower-cases the specified {@code String} and capitalizes the first character.
   *
   * @param string The String, may not be {@code null}. @return The formatted String.
   */
  public static String toFirstUpper(String string) {
    Assertions.checkNonEmpty(string, "String may not be null or empty!");

    String lower = string.toLowerCase();
    char first = lower.charAt(0);

    return Character.toUpperCase(first) + lower.substring(1);
  }

  /**
   * Returns the indefinite article for the specified {@code String}.
   *
   * @param string The String to find the indefinite article for. @return The indefinite article.
   */
  public static String getIndefiniteArticle(String string) {
    Assertions.checkNonEmpty(string, "String may not be null or empty!");

    char first = string.charAt(0);
    String article = Characters.isVowel(first) ? "an" : "a";

    return article;
  }

  /**
   * Capitalizes the first character in the specified String as well as the first character in
   * subsequent sentences within the specified String.
   * 
   * @param string The String to capitalize. @return The capitalized String.
   */
  public static String capitalize(String string) {
    Assertions.checkNonEmpty(string, "String may not be null or empty!");

    StringBuilder builder = new StringBuilder(string);

    boolean capitalize = true;
    for (int index = 0; index < builder.length(); index++) {
      char character = builder.charAt(index);
      if (character == '.' || character == '!' || character == '?') {
        capitalize = true;
      } else if (capitalize && !Character.isWhitespace(character)) {
        builder.setCharAt(index, Character.toUpperCase(character));
        capitalize = false;
      }
    }

    return builder.toString();
  }

  /**
   * Substitutes each {@code %s} in {@code template} with an argument. These are matched by
   * position: the first {@code %s} gets {@code args[0]}, etc.
   *
   * @param string A {@code String} containing 0 or more {@code %s} place holders, may not be {@code
   * null}. @param args The arguments to be substituted into the message template. @return The
   * formatted String.
   */
  public static String format(String string, Object... args) {
    Assertions.checkNonEmpty(string, "String may not be null or empty!");

    StringBuilder builder = new StringBuilder(string.length() + 16 * args.length);

    int templateStart = 0;
    for (int index = 0, length = args.length; index < length;) {
      int placeholderStart = string.indexOf("%s", templateStart);
      if (placeholderStart == -1) {
        break;
      }

      builder.append(string.substring(templateStart, placeholderStart));
      builder.append(args[index++]);
      templateStart = placeholderStart + 2;
    }

    return builder.append(string.substring(templateStart)).toString();
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private Strings() {}

}
