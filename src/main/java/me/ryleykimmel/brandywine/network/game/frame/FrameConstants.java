package me.ryleykimmel.brandywine.network.game.frame;

/**
 * A class representing common Frame constants.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class FrameConstants {

	/**
	 * The terminator used within the client, equal to <tt>10</tt> and otherwise know as the Jagex {@code String} terminator.
	 */
	public static final char J_STRING_TERMINATOR = '\n';

	/**
	 * The default {@code String} terminator, equal to <tt>0</tt> and otherwise known as the 'null' {@code String} terminator.
	 */
	public static final char DEFAULT_STRING_TERMINATOR = '\0';

	/**
	 * Sole private constructor to discourage instantiation of this class.
	 */
	private FrameConstants() {
	}

}