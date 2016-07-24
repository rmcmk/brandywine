package me.ryleykimmel.brandywine.game.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import me.ryleykimmel.brandywine.game.area.Region;
import me.ryleykimmel.brandywine.game.area.RegionCoordinates;

/**
 * Represents a position in the World.
 */
public final class Position {

  /**
   * The number of height levels, (0, 3] inclusive.
   */
  public static final int HEIGHT_LEVELS = 4;

  /**
   * The maximum distance players/NPCs can 'see'.
   */
  public static final int MAX_DISTANCE = 15;

  /**
   * The height plane.
   */
  private final int height;

  /**
   * The absolute x coordinate.
   */
  private final int x;

  /**
   * The absolute y coordinate.
   */
  private final int y;

  /**
   * Constructs a new {@link Position} with the specified x and y coordinates.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   */
  public Position(int x, int y) {
    this(x, y, 0);
  }

  /**
   * Constructs a new {@link Position} with the specified x, y and height coordinates.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @param height The height.
   */
  public Position(int x, int y, int height) {
    this.x = x;
    this.y = y;
    this.height = Preconditions.checkElementIndex(height, HEIGHT_LEVELS,
        "Height must be [0, 3), received " + height + ".");
  }

  /**
   * Gets the x coordinate of the central region.
   *
   * @return The x coordinate of the central region.
   */
  public int getCentralRegionX() {
    return x / 8;
  }

  /**
   * Gets the y coordinate of the central region.
   *
   * @return The y coordinate of the central region.
   */
  public int getCentralRegionY() {
    return y / 8;
  }

  /**
   * Gets the half x coordinate.
   *
   * @return The half x coordinate.
   */
  public int getHalfX() {
    return x * 2 + 1;
  }

  /**
   * Gets the half y coordinate.
   *
   * @return The half y coordinate.
   */
  public int getHalfY() {
    return y * 2 + 1;
  }

  /**
   * Gets the delta x coordinate between this Position and the specified Position.
   * 
   * @param other The other Position.
   * @return The delta x coordinate.
   */
  public int getDeltaX(Position other) {
    return x - other.getX();
  }

  /**
   * Gets the delta y coordinate between this Position and the specified Position.
   * 
   * @param other The other Position.
   * @return The delta y coordinate.
   */
  public int getDeltaY(Position other) {
    return y - other.getY();
  }

  /**
   * Gets the height level.
   *
   * @return The height level.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Gets the x coordinate inside the region of this position.
   *
   * @return The local x coordinate.
   */
  public int getLocalX() {
    return getLocalX(this);
  }

  /**
   * Gets the local x coordinate inside the region of the {@code base} position.
   *
   * @param base The base position.
   * @return The local x coordinate.
   */
  public int getLocalX(Position base) {
    return x - base.getTopLeftRegionX() * 8;
  }

  /**
   * Gets the y coordinate inside the region of this position.
   *
   * @return The local y coordinate.
   */
  public int getLocalY() {
    return getLocalY(this);
  }

  /**
   * Gets the local y coordinate inside the region of the {@code base} position.
   *
   * @param base The base position.
   * @return The local y coordinate.
   */
  public int getLocalY(Position base) {
    return y - base.getTopLeftRegionY() * 8;
  }

  /**
   * Gets the x coordinate of the region this position is in.
   *
   * @return The region x coordinate.
   */
  public int getTopLeftRegionX() {
    return getCentralRegionX() - 6;
  }

  /**
   * Gets the y coordinate of the region this position is in.
   *
   * @return The region y coordinate.
   */
  public int getTopLeftRegionY() {
    return getCentralRegionY() - 6;
  }

  /**
   * Gets the x coordinate.
   *
   * @return The x coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the y coordinate.
   *
   * @return The y coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Gets the distance between this position and another position. Only x and y are considered (i.e.
   * 2 dimensions).
   *
   * @param other The other position.
   * @return The distance.
   */
  public int getDistance(Position other) {
    int deltaX = getDeltaX(other);
    int deltaY = getDeltaY(other);
    return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
  }

  /**
   * Checks if this position is within distance of the other.
   *
   * @param other The other position.
   * @return {@code true} if so, {@code false} if not.
   */
  public boolean isWithinDistance(Position other) {
    int deltaX = getDeltaX(other);
    int deltaY = getDeltaY(other);
    return deltaX >= -16 && deltaX <= 15 && deltaY >= -16 && deltaY <= 15 && height == other.height;
  }

  /**
   * Checks if the position is within distance of another.
   *
   * @param other The other position.
   * @param distance The distance.
   * @return {@code true} if so, {@code false} if not.
   */
  public boolean isWithinDistance(Position other, int distance) {
    int deltaX = Math.abs(getDeltaX(other));
    int deltaY = Math.abs(getDeltaY(other));
    return deltaX <= distance && deltaY <= distance && height == other.height;
  }

  /**
   * Gets the longest horizontal or vertical delta between the two positions.
   *
   * @param other The other position.
   * @return The longest horizontal or vertical delta.
   */
  public int getLongestDelta(Position other) {
    int deltaX = Math.abs(getDeltaX(other));
    int deltaY = Math.abs(getDeltaY(other));
    return Math.max(deltaX, deltaY);
  }

  /**
   * Returns the {@link RegionCoordinates} of the {@link Region} this position is inside.
   *
   * @return The region coordinates.
   */
  public RegionCoordinates getRegionCoordinates() {
    return RegionCoordinates.fromPosition(this);
  }

  @Override
  public int hashCode() {
    return height << 30 | (y & 0x7FFF) << 15 | x & 0x7FFF;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Position) {
      Position other = (Position) obj;
      return x == other.x && y == other.y && height == other.y;
    }

    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("x", x).add("y", y).add("height", height)
        .toString();
  }


}
