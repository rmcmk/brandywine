package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.game.frame.Frame;

/**
 * An encoder which encodes some Message into a {@link Frame}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The Message type.
 */
public interface MessageEncoder<T extends Message> {

	/**
	 * Encodes the specified Message into a Frame.
	 *
	 * @param message The Message to encode.
	 * @return The encoded Frame.
	 */
	Frame encode(T message);

}