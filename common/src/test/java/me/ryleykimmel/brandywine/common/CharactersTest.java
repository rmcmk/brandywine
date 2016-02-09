package me.ryleykimmel.brandywine.common;

import org.junit.Assert;
import org.junit.Test;

public final class CharactersTest {

  @Test
  public void testIsVowel() {
    char vowel = 'a';
    char not_vowel = 'b';

    Assert.assertTrue(Characters.isVowel(vowel));
    Assert.assertFalse(Characters.isVowel(not_vowel));
  }

  @Test
  public void testIsVowelUppercase() {
    char vowel = 'A';
    char not_vowel = 'B';

    Assert.assertTrue(Characters.isVowel(vowel));
    Assert.assertFalse(Characters.isVowel(not_vowel));
  }

}
