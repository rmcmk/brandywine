package me.ryleykimmel.brandywine.network.game.frame;

/**
 * Represents a type of Frame.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public enum FrameType {

	/**
	 * The type that indicates that the Frame is not valid.
	 */
	INVALID,

	/**
	 * The type that indicates that the Frame has no length or payload.
	 */
	EMPTY,

	/**
	 * The fixed type indicates that the Frame has a fixed (known) length.
	 */
	FIXED,

	/**
	 * The variable byte type indicates that the Frame has the length within the range of a single {@code byte}.
	 */
	VARIABLE_BYTE,

	/**
	 * The variable short type indicates that the Frame has the length within the range of a single {@code short}.
	 */
	VARIABLE_SHORT

}