package me.ryleykimmel.brandywine.game.update;

import java.util.BitSet;

/**
 * Holds a {@link BitSet} of UpdateFlags.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public class UpdateFlags {

	/**
	 * A BitSet of UpdateFlags.
	 */
	private final BitSet flags = new BitSet();

	/**
	 * Represents a single UpdateFlag.
	 * 
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 */
	public enum UpdateFlag {

		/**
		 * Indicates that an appearance update is required.
		 */
		APPEARANCE,

		/**
		 * Indicates that a chat update is required.
		 */
		CHAT,

		/**
		 * Indicates that a graphic update is required.
		 */
		GRAPHIC,

		/**
		 * Indicates that an animation update is required.
		 */
		ANIMATION,

		/**
		 * Indicates that a forced chat update is required.
		 */
		FORCED_CHAT,

		/**
		 * Indicates that a face update is required.
		 */
		FACE_MOB,

		/**
		 * Indicates that a face update is required.
		 */
		FACE_COORDINATE,

		/**
		 * Indicates that a primary hit update is required.
		 */
		PRIMARY_HIT,

		/**
		 * Indicates that a secondary hit update is required.
		 */
		SECONDARY_HIT,

		/**
		 * Indicates that a transform update is required.
		 */
		TRANSFORM
	}

	/**
	 * Flags the specified UpdateFlag.
	 * 
	 * @param flag The UpdateFlag to set.
	 */
	public void flag(UpdateFlag flag) {
		flags.set(flag.ordinal());
	}

	/**
	 * Gets the value of the specified UpdateFlag.
	 * 
	 * @param flag The UpdateFlag to get.
	 * @return {@code true} if the UpdateFlag is set, otherwise {@code false}.
	 */
	public boolean get(UpdateFlag flag) {
		return flags.get(flag.ordinal());
	}

	/**
	 * Clears all of the UpdateFlags.
	 */
	public void clear() {
		flags.clear();
	}

}