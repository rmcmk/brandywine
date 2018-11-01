package me.ryleykimmel.brandywine.common;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests the {@link Characters} class.
 */
@RunWith(JUnit4.class)
public final class CharactersTests {

  /**
   * Tests {@link Characters#isVowel(char)}.
   */
  @Test
  public void isVowel() {
    char vowel = 'a', notVowel = 'b';

    assertThat(Characters.isVowel(vowel)).isTrue();
    assertThat(Characters.isVowel(notVowel)).isFalse();

    assertThat(Characters.isVowel(Character.toUpperCase(vowel))).isTrue();
    assertThat(Characters.isVowel(Character.toUpperCase(notVowel))).isFalse();
  }

}
