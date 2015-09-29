package me.ryleykimmel.brandywine.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class CharactersTest {

	@Test
	public void testIsVowel() {
		char vowel = 'a';
		char not_vowel = 'b';

		assertTrue(Characters.isVowel(vowel));
		assertFalse(Characters.isVowel(not_vowel));

		char upper_case_vowel = 'A';
		char upper_case_not_vowel = 'B';

		assertTrue(Characters.isVowel(upper_case_vowel));
		assertFalse(Characters.isVowel(upper_case_not_vowel));
	}

}