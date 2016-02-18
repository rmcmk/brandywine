package me.ryleykimmel.brandywine.game.model;

import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.game.area.Region;
import me.ryleykimmel.brandywine.game.area.RegionRepository;

/**
 * Represents an Entity within the game World.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
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
   * The context of the Server.
   */
  protected final ServerContext context;

  /**
   * The current Position of this Entity.
   */
  protected Position position = DEFAULT_POSITION;

  /**
   * Constructs a new {@link Entity} with the specified World.
   * 
   * @param world The World this Entity is in.
   * @param type The type of this Entity.
   * @param context The context of the Server.
   */
  public Entity(World world, EntityType type, ServerContext context) {
    this.world = world;
    this.type = type;
    this.context = context;
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
   * Gets the context of the Server.
   * 
   * @return The context of the Server.
   */
  public final ServerContext getContext() {
    return context;
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
