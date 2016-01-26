package me.ryleykimmel.brandywine.game.model;

/**
 * Represents an Entity within the game World.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class Entity {

  /**
   * Represents the default Position a Mob should spawn at.
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
   * Sets the Position of this Entity.
   *
   * @param position The new Position of this Entity.
   */
  public void setPosition(Position position) {
    this.position = position;
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

  /**
   * Converts this Entity to the specified Entity class.
   * 
   * @param clazz The class of the Entity.
   * @return The converted type, if possible, otherwise an UnsupportedOperationException is thrown.
   */
  public final <T extends Entity> T convert(Class<T> clazz) {
    if (!clazz.isInstance(Entity.class)) {
      throw new UnsupportedOperationException(
          "You may only convert this Entity into a more specific Entity type.");
    }

    T entity = clazz.cast(this);

    if (!entity.instanceOf(type)) {
      throw new UnsupportedOperationException(
          "The EntityType " + type + " and " + entity.getType() + " are not of the same instance.");
    }

    return entity;
  }

}
