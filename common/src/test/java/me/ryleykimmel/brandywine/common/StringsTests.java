package me.ryleykimmel.brandywine.common;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests the {@link Strings} class.
 */
@RunWith(JUnit4.class)
public final class StringsTests {

  /**
   * The expected exception rule.
   */
  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  /**
   * Tests the {@link Strings#toFirstUpper(String)}.
   */
  @Test
  public void toFirstUpper() {
    assertThat(Strings.toFirstUpper("hEllO WoRlD")).isEqualTo("Hello world");

    assertThat(Strings.toFirstUpper("h")).isEqualTo("H");
  }

  /**
   * Tests failure of {@link Strings#toFirstUpper(String)} with zero length.
   */
  @Test
  public void failToFirstUpperZeroLength() {
    thrown.expect(IllegalArgumentException.class);
    Strings.toFirstUpper("");
  }

  /**
   * Tests failure of {@link Strings#toFirstUpper(String)} with null input.
   */
  @Test
  public void failToFirstUpperNull() {
    thrown.expect(IllegalArgumentException.class);
    Strings.toFirstUpper(null);
  }

}
