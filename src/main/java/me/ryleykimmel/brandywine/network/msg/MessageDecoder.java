package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.game.frame.Frame;

/**
 * A decoder which decodes some {@link Frame} into some Message.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The Message type.
 */
public interface MessageDecoder<T extends Message> {

  /**
   * Decodes the specified Frame into some Message.
   *
   * @param frame The Frame to decode.
   * @return The decoded Message.
   */
  T decode(Frame frame);

}
