package me.ryleykimmel.brandywine.network.game.frame;

/**
 * Represents a mathematical data transformation performed on a piece of data to change its value. The reverse of the transformation must then be performed on the received data, to
 * obtain the original value.
 *
 * @author Graham
 * @author Major
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public enum DataTransformation {

	/**
	 * None, no transformation is performed.
	 */
	NONE,

	/**
	 * The add transformation increments the value by 128, which must then be removed to retrieve the original value. This transformation is applied to both bytes and shorts - when
	 * applied to a short, the least-significant byte is incremented.
	 */
	ADD,

	/**
	 * The subtract transformation subtracts the value from 128; the resulting value must also be subtracted from 128 to obtain the original value. This transformation is also only
	 * applied to bytes and shorts.
	 */
	SUBTRACT,

	/**
	 * The simplest of the three, the negate transformation simply inverts the sign bit of the value (i.e. a positive value becomes negative, and a negative value becomes
	 * positive). This transformation is only used on byte values.
	 */
	NEGATE

}