package me.ryleykimmel.brandywine.game.model;

/**
 * Represents a single Direction.
 *
 * @author Graham
 */
public enum Direction {

  /**
   * No movement.
   */
  NONE(-1),

  /**
   * North west movement.
   */
  NORTH_WEST(0),

  /**
   * North movement.
   */
  NORTH(1),

  /**
   * North east movement.
   */
  NORTH_EAST(2),

  /**
   * West movement.
   */
  WEST(3),

  /**
   * East movement.
   */
  EAST(4),

  /**
   * South west movement.
   */
  SOUTH_WEST(5),

  /**
   * South movement.
   */
  SOUTH(6),

  /**
   * South east movement.
   */
  SOUTH_EAST(7);

  /**
   * Gets the Direction between the two {@link Position positions}.
   *
   * @param current The difference between two X coordinates. @param next The difference between two
   * Y coordinates. @return The Direction between the two Positions.
   */
  public static Direction between(Position current, Position next) {
    int deltaX = next.getX() - current.getX();
    int deltaY = next.getY() - current.getY();

    switch (deltaY) {
      case 1:
        switch (deltaX) {
          case 1:
            return NORTH_EAST;

          case 0:
            return NORTH;

          default:
            return NORTH_WEST;
        }

      case -1:
        switch (deltaX) {
          case 1:
            return SOUTH_EAST;

          case 0:
            return SOUTH;

          default:
            return SOUTH_WEST;
        }

      default:
        switch (deltaX) {
          case 1:
            return EAST;

          case -1:
            return WEST;

          default:
            throw new IllegalArgumentException("Difference between Positions must be [-1, 1].");
        }
    }
  }

  /**
   * The value of this Direction.
   */
  private final int value;

  /**
   * Constructs a new {@link Direction} with the specified value.
   *
   * @param value The value of this Direction.
   */
  private Direction(int value) {
    this.value = value;
  }

  /**
   * Gets the value of this Direction.
   *
   * @return The value of this Direction.
   */
  public int getValue() {
    return value;
  }

}
