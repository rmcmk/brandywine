package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.game.GameSession;

/**
 * Handles received Messages while serving some {@link GameSession}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com> @param <T> The Message type.
 */
public interface MessageHandler<T extends Message> {

  /**
   * Handles the received Message for the specified GameSession.
   *
   * @param session The GameSession to handle the Message for. @param message The Message to handle.
   */
  void handle(GameSession session, T message);

}
