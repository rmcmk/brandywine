package me.ryleykimmel.brandywine.common;

import com.google.common.primitives.Chars;

import java.util.List;

/**
 * A static-utility class containing extension and helper methods for {@code char}s
 */
public final class Characters {

  /**
   * An inherently immutable {@link List} of vowel {@link Character characters}.
   */
  public static final List<Character> VOWELS = Chars.asList('a', 'e', 'i', 'o', 'u');

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private Characters() {
  }

  /**
   * Returns a flag denoting whether or not the specified {@code char} is a vowel.
   *
   * @param character The character to check.
   * @return {@code true} if and only if the specified {@code char} is a vowel otherwise
   * {@code false}.
   */
  public static boolean isVowel(char character) {
    return VOWELS.contains(Character.toLowerCase(character));
  }

}
