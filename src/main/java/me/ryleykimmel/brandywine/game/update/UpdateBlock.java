package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.Mob;

/**
 * Represents an update block.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The type of Mob.
 */
public abstract class UpdateBlock<T extends Mob> {

	/**
	 * The Mob being updated.
	 */
	protected final T mob;

	/**
	 * The mask of this UpdateBlock.
	 */
	private final int mask;

	/**
	 * Constructs a new {@link UpdateBlock} with the specified Mob and mask.
	 * 
	 * @param mob The Mob being updated.
	 * @param mask The Mask of this UpdateBlock.
	 */
	public UpdateBlock(T mob, int mask) {
		this.mob = mob;
		this.mask = mask;
	}

	/**
	 * Gets the Mob being updated.
	 * 
	 * @return The Mob being updated.
	 */
	public final T getMob() {
		return mob;
	}

	/**
	 * Gets the Mask of this UpdateBlock.
	 * 
	 * @return The Mask of this UpdateBlock.
	 */
	public final int getMask() {
		return mask;
	}

}