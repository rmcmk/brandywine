package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.game.GameSession;

/**
 * Handles {@link Message}s for {@link GameSession}s.
 * 
 * @param <T> The Message type.
 */
@FunctionalInterface
public interface GameMessageHandler<T extends Message> extends MessageHandler<T> {

  /**
   * Handles the specified Message for the specified GameSession.
   * 
   * @param session The GameSession we're handling for.
   * @param message The Message we're handling.
   */
  void handle(GameSession session, T message);

}
