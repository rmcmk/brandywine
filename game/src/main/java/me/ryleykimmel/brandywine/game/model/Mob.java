package me.ryleykimmel.brandywine.game.model;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.model.skill.SkillSet;
import me.ryleykimmel.brandywine.game.update.UpdateBlock;
import me.ryleykimmel.brandywine.network.msg.Message;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a mobile Entity within the game World.
 */
public abstract class Mob extends Entity {

  /**
   * This Mobs movement queue.
   */
  protected final MovementQueue movementQueue = new MovementQueue(this);

  /**
   * This Mobs skill set.
   */
  protected final SkillSet skills = new SkillSet();

  /**
   * A {@link Set} of local Players.
   */
  private final Set<Player> localPlayers = new HashSet<>();

  /**
   * A {@link Set} of pending {@link UpdateBlock}s.
   */
  private final Set<UpdateBlock> pendingUpdates = new HashSet<>();

  /**
   * The index of this Mob.
   */
  private int index = -1;

  /**
   * The first Direction of this Mob.
   */
  private Direction firstDirection = Direction.NONE;

  /**
   * The second Direction of this Mob.
   */
  private Direction secondDirection = Direction.NONE;

  /**
   * Whether or not this Mob is currently running.
   */
  private boolean running;

  /**
   * Whether or not this mob is currently teleporting.
   */
  private boolean teleporting;

  /**
   * Constructs a new {@link Mob} with the specified World.
   *
   * @param world The World this Mob is in.
   * @param type The type of this Entity.
   */
  public Mob(World world, EntityType type) {
    super(world, type);
  }

  /**
   * Gets the index of this Mob.
   *
   * @return The index of this Mob.
   */
  public final int getIndex() {
    return index;
  }

  /**
   * Sets the index of this Mob.
   *
   * @param index The new index of this Mob.
   */
  public final void setIndex(int index) {
    this.index = index;
  }

  /**
   * Gets this Mobs movement queue.
   *
   * @return This Mobs movement queue.
   */
  public final MovementQueue getMovementQueue() {
    return movementQueue;
  }

  /**
   * Gets this Mobs skill set.
   *
   * @return This Mobs skills set.
   */
  public final SkillSet getSkills() {
    return skills;
  }

  /**
   * Gets whether or not this Mob is currently running.
   *
   * @return {@code true} if this Mob is currently running otherwise {@code false}.
   */
  public final boolean isRunning() {
    return running;
  }

  /**
   * Sets whether or not this Mob is currently running.
   *
   * @param running {@code true} if this Mob should be running otherwise {@code false}.
   */
  public final void setRunning(boolean running) {
    this.running = running;
    // TODO: Configs
  }

  /**
   * Gets this Mobs first direction.
   *
   * @return The first direction of this Mob.
   */
  public final Direction getFirstDirection() {
    return firstDirection;
  }

  /**
   * Gets this Mobs second direction.
   *
   * @return The second direction of this Mob.
   */
  public final Direction getSecondDirection() {
    return secondDirection;
  }

  /**
   * Sets the directions for this Mob.
   *
   * @param firstDirection This Mobs first direction.
   * @param secondDirection This mobs second direction.
   */
  public final void setDirections(Direction firstDirection, Direction secondDirection) {
    this.firstDirection = firstDirection;
    this.secondDirection = secondDirection;
  }

  /**
   * Gets whether or not this Mob is currently teleporting.
   *
   * @return {@code true} if this Mob is teleporting otherwise {@code false}.
   */
  public final boolean isTeleporting() {
    return teleporting;
  }

  /**
   * Teleports this Mob to the specified Position.
   *
   * @param position The Position to teleport this Mob to.
   */
  public void teleport(Position position) {
    setPosition(position);
    teleporting = true;
  }

  /**
   * Checks whether or not this Mob is active.
   *
   * @return {@code true} if and only if this Mob is active otherwise {@code false}.
   */
  public final boolean isActive() {
    return index != -1;
  }

  /**
   * Gets the Set of local Players.
   *
   * @return The Set of local Players.
   */
  public final Set<Player> getLocalPlayers() {
    return localPlayers;
  }

  /**
   * Resets this Mobs directions, teleporting attribute and UpdateFlags.
   */
  public void reset() {
    firstDirection = secondDirection = Direction.NONE;
    teleporting = false;
    pendingUpdates.clear();
  }

  /**
   * Flags an UpdateBlock.
   *
   * @param block The UpdateBlock to flag.
   */
  public final void flagUpdate(UpdateBlock block) {
    if (!pendingUpdates.contains(block)) {
      pendingUpdates.add(block);
    }
  }

  /**
   * Gets the {@link Set} of pending {@link UpdateBlock}s.
   *
   * @return The Set of pending UpdateBlocks.
   */
  public final Set<UpdateBlock> getPendingUpdates() {
    return pendingUpdates;
  }

  /**
   * Writes the specified Message for this Mob.
   *
   * @param message The Message to write.
   */
  public void write(Message message) {

  }

}
