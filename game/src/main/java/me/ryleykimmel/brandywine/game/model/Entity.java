package me.ryleykimmel.brandywine.game.model;

import me.ryleykimmel.brandywine.game.area.Region;
import me.ryleykimmel.brandywine.game.area.RegionRepository;

/**
 * Represents an Entity within the game World.
 */
public abstract class Entity {

  /**
   * Represents the default Position an Entity should spawn at.
   */
  protected static final Position DEFAULT_POSITION = new Position(3200, 3200, 0);

  /**
   * The World this Entity is in.
   */
  protected final World world;

  /**
   * The type of this Entity.
   */
  protected final EntityType type;

  /**
   * The current Position of this Entity.
   */
  protected Position position = DEFAULT_POSITION;

  /**
   * Constructs a new {@link Entity} with the specified World.
   *
   * @param world The World this Entity is in.
   * @param type The type of this Entity.
   */
  public Entity(World world, EntityType type) {
    this.world = world;
    this.type = type;
  }

  /**
   * Gets this Entity's current Position.
   *
   * @return This Entity's current Position.
   */
  public final Position getPosition() {
    return position;
  }

  /**
   * Sets the Position of this Entity.
   *
   * @param position The new Position of this Entity.
   */
  public void setPosition(Position position) {
    Position current = this.position;
    if (current.equals(position)) {
      return;
    }

    RegionRepository repository = world.getRegionRepository();
    Region currentRegion = repository.fromPosition(current),
      next = repository.fromPosition(position);

    currentRegion.removeEntity(this);
    this.position = position;
    next.addEntity(this);
  }

  /**
   * Gets the World this Entity is in.
   *
   * @return The World this Entity is in.
   */
  public final World getWorld() {
    return world;
  }

  /**
   * Gets the type of this Entity.
   *
   * @return The type of this Entity.
   */
  public final EntityType getType() {
    return type;
  }

  /**
   * Tests whether or not this Entity is an instance of the specified EntityType.
   *
   * @param type The type of the Entity.
   * @return {@code true} iff this Entity is an instance of the specified EntityType.
   */
  public final boolean instanceOf(EntityType type) {
    return this.type == type;
  }

}
