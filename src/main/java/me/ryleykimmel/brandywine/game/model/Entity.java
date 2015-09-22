package me.ryleykimmel.brandywine.game.model;

/**
 * Represents an Entity within the game World.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class Entity {

	/**
	 * The current Position of this Entity.
	 */
	protected Position position;

	/**
	 * Sets the Position of this Entity.
	 *
	 * @param position The new Position of this Entity.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * Gets this Entities current Position.
	 *
	 * @return This Entities current Position.
	 */
	public final Position getPosition() {
		return position;
	}

}