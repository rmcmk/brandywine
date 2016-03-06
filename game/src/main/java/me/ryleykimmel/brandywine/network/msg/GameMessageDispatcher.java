package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.game.GameSession;

/**
 * Dispatches {@link Message}s for a {@link GameSession}.
 */
public final class GameMessageDispatcher implements MessageDispatcher<GameMessageHandler<Message>> {

  /**
   * The GameSession we are dispatching for.
   */
  private final GameSession session;

  /**
   * Constructs a new {@link GameMessageDispatcher}.
   * 
   * @param session The GameSession we are dispatching for.
   */
  public GameMessageDispatcher(GameSession session) {
    this.session = session;
  }

  @Override
  public void dispatch(GameMessageHandler<Message> handler, Message message) {
    handler.handle(session, message);
  }

}
