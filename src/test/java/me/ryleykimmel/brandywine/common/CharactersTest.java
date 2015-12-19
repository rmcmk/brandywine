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
	}
	
	@Test
	public void testIsVowelUppercase() {
		char vowel = 'A';
		char not_vowel = 'B';

		assertTrue(Characters.isVowel(vowel));
		assertFalse(Characters.isVowel(not_vowel));
	}

}