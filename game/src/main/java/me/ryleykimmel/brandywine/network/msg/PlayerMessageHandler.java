package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Handles {@link Message}s for {@link Player}s.
 * 
 * @param <T> The Message type.
 */
@FunctionalInterface
public interface PlayerMessageHandler<T extends Message> extends MessageHandler<T> {

  /**
   * Handles the specified Message for the specified Player.
   * 
   * @param player The Player we're handling for.
   * @param message The Message we're handling.
   */
  void handle(Player player, T message);

}
