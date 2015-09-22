package me.ryleykimmel.brandywine.game.model;

import java.util.HashSet;
import java.util.Set;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.model.skill.SkillSet;
import me.ryleykimmel.brandywine.game.update.UpdateFlags;
import me.ryleykimmel.brandywine.game.update.UpdateFlags.UpdateFlag;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * Represents a mobile Entity within the game World.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class Mob extends Entity {

	/**
	 * Represents the default Position a Mob should spawn at.
	 */
	protected static final Position DEFAULT_POSITION = new Position(3200, 3200, 0);

	/**
	 * This Mobs movement queue.
	 */
	protected final MovementQueue movementQueue = new MovementQueue(this);

	/**
	 * This Mobs skill set.
	 */
	protected final SkillSet skills = new SkillSet();

	/**
	 * This Mobs update flags.
	 */
	protected final UpdateFlags updateFlags = new UpdateFlags();

	/**
	 * A {@link Set} of local Players.
	 */
	private final Set<Player> localPlayers = new HashSet<>();

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
	 * Gets the index of this Mob.
	 *
	 * @return The index of this Mob.
	 */
	public final int getIndex() {
		return index;
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
	 * Gets this Mobs update flags.
	 * 
	 * @return This Mobs update flags.
	 */
	public final UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * Sets the index of this Mob.
	 *
	 * @param index The new index of this Mob.
	 */
	public synchronized final void setIndex(int index) {
		this.index = index;
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
	 * Flags the specified UpdateFlag.
	 * 
	 * @param flag The UpdateFlag to set.
	 */
	public final void flagUpdate(UpdateFlag flag) {
		updateFlags.flag(flag);
	}

	/**
	 * Gets the specified UpdateFlag.
	 * 
	 * @param flag The UpdateFlag to get.
	 * @return {@code true} if the UpdateFlag is set, otherwise {@code false}.
	 */
	public final boolean isUpdateFlagged(UpdateFlag flag) {
		return updateFlags.get(flag);
	}

	/**
	 * Resets this Mobs directions, teleporting attribute and UpdateFlags.
	 */
	public void reset() {
		firstDirection = secondDirection = Direction.NONE;
		teleporting = false;
		updateFlags.clear();
	}

	/**
	 * Writes the specified Message for this Mob.
	 * 
	 * @param message The Message to write.
	 */
	public void write(Message message) {

	}

}