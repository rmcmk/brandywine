package me.ryleykimmel.brandywine.network.game.frame;

/**
 * Represents the endianness, used within frame buffers when reading or writing bytes.
 *
 * @author Graham
 * @author Major
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public enum DataOrder {

	/**
	 * In little-endian representation, the least significant part of the unit (the part that could affect the value the least) is stored first. Again considering the unsigned
	 * binary value 10000000, in little-endian form the left-most part has the lowest value, so it is equivalent to the decimal value 1.
	 */
	LITTLE,

	/**
	 * In big-endian representation, the most significant part of the unit is stored first. Consider the unsigned binary value 10000000 - when read in big-endian form, the
	 * left-most (i.e. first) part has the highest value, so it is equivalent to the decimal value 128.
	 */
	BIG,

	/**
	 * A representation used only by integers, the middle endian format stores the most significant part in the second-last byte. This is also known as the "V1" endianness. Unlike
	 * big and little endian, this is not a standard endianness.
	 */
	MIDDLE,

	/**
	 * Another representation used only by integers, the inverse middle endian format stored the most significant part in the second byte. This is also known as the "V2"
	 * endianness. Like middle endian, this is not a standard endianness.
	 */
	INVERSED_MIDDLE

}