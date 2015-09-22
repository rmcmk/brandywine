package me.ryleykimmel.brandywine.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public final class StringsTest {

	@Test
	public void test_to_first_upper() {
		String input = "hEllo WORLd";
		String expected = "Hello world";

		assertEquals(expected, Strings.toFirstUpper(input));
	}

	@Test
	public void test_international_chars() {
		String input = "Ï€Ï€Ï€Ï€Ï€Ï€Ï€Ï€";
		String expected = "Ï€ï€ï€ï€ï€ï€ï€ï€";

		assertEquals(expected, Strings.toFirstUpper(input));
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_zero_length_strings() {
		Strings.toFirstUpper("");
	}

	@Test(expected = NullPointerException.class)
	public void test_null_Strings() {
		Strings.toFirstUpper(null);
	}

}