package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Dispatches {@link Message}s for a {@link Player}.
 */
public final class PlayerMessageDispatcher
    implements MessageDispatcher<PlayerMessageHandler<Message>> {

  /**
   * The Player we are dispatching for.
   */
  private final Player player;

  /**
   * Constructs a new {@link PlayerMessageDispatcher}.
   * 
   * @param player The Player we are dispatching for.
   */
  public PlayerMessageDispatcher(Player player) {
    this.player = player;
  }

  @Override
  public void dispatch(PlayerMessageHandler<Message> handler, Message message) {
    handler.handle(player, message);
  }

}
