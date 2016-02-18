package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameReader;

/**
 * A decoder which decodes some {@link Frame} into some Message.
 *
 * @param <T> The Message type.
 */
public interface MessageDecoder<T extends Message> {

  /**
   * Decodes the specified Frame into some Message.
   *
   * @param session The Session which is decoding the specified Frame.
   * @param frame The FrameReader.
   * @return The decoded Message.
   */
  T decode(GameSession session, FrameReader frame);

}
