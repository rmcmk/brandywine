package me.ryleykimmel.brandywine.common;

import org.junit.Assert;
import org.junit.Test;

public final class StringsTest {

  @Test
  public void testToFirstUpper() {
    String input = "hEllo WORLd";
    String expected = "Hello world";

    Assert.assertEquals(expected, Strings.toFirstUpper(input));
  }

  @Test
  public void testToFirstUpperInternational() {
    String input = "Ï€Ï€Ï€Ï€Ï€Ï€Ï€Ï€";
    String expected = "Ï€ï€ï€ï€ï€ï€ï€ï€";

    Assert.assertEquals(expected, Strings.toFirstUpper(input));
  }

  @Test(expected = IllegalArgumentException.class)
  public void failToFirstUpperZeroLength() {
    Strings.toFirstUpper("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void failToFirstUpperNull() {
    Strings.toFirstUpper(null);
  }

}
