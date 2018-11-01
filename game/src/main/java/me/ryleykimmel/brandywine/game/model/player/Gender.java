package me.ryleykimmel.brandywine.game.model.player;

/**
 * Represents the Gender of a Player.
 */
public enum Gender {

  /**
   * Represents the male gender.
   */
  MALE(0),

  /**
   * Represents the female gender.
   */
  FEMALE(1);

  /**
   * The value of the Gender.
   */
  private final int value;

  /**
   * Constructs a new {@link Gender} with the specified value.
   *
   * @param value The value of the Gender.
   */
  Gender(int value) {
    this.value = value;
  }

  /**
   * Gets the Gender based on the specified value.
   *
   * @param value The value of the Gender to get.
   * @return The Gender for the specified value.
   */
  public static Gender forValue(int value) {
    switch (value) {
      case 0:
        return MALE;
      case 1:
        return FEMALE;
    }

    throw new IllegalArgumentException(value + " is not a recgonized gender.");
  }

  /**
   * Gets the value of the Gender.
   *
   * @return The value of the Gender.
   */
  public int getValue() {
    return value;
  }

}
