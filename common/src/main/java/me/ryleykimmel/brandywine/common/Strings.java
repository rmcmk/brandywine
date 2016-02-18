package me.ryleykimmel.brandywine.common;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Iterables;

/**
 * A static-utility class containing extension and helper methods for {@link String strings}.
 */
public final class Strings {

  /**
   * Lower-cases the specified {@code String} and capitalizes the first character.
   *
   * @param string The String, may not be {@code null}.
   * @return The formatted String.
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
   * @param string The String to find the indefinite article for.
   * @return The indefinite article.
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
   * @param string The String to capitalize.
   * @return The capitalized String.
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
   * null}.
   * @param args The arguments to be substituted into the message template.
   * @return The formatted String.
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
   * Splits the specified {@code String} into segments delimited by {@code delim}.
   * 
   * <pre>
   * <code>
   * String string = "\"I am a String\" Hey hey hey";
   * Arrays.toString(explode(string, '"'));
   *          -> [I am a String, Hey, hey, hey]
   * </code>
   * </pre>
   * 
   * @param string The {@code String} to explode.
   * @param delim The delimiter.
   * @return The exploded segments of the {@code String}.
   */
  public static String[] split(String string, char delim) {
    List<String> arguments = new ArrayList<>();
    StringBuilder builder = new StringBuilder();
    boolean quoted = false, escaped = false;

    for (int i = 0, len = string.length(); i < len; i++) {
      char c = string.charAt(i);
      if (c == ' ' && !quoted) {
        arguments.add(builder.toString());
        builder.setLength(0); // reset builder
      } else if (c == delim && !escaped) {
        quoted = !quoted;
      } else if (c == '\\') {
        escaped = true;
      } else {
        escaped = false;
        builder.append(c);
      }
    }

    if (builder.length() > 0) {
      arguments.add(builder.toString());
    }

    return Iterables.toArray(arguments, String.class);
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private Strings() {}

}
