package me.ryleykimmel.brandywine.game.message.listener;

import com.google.common.base.Preconditions;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageListener;

/**
 * A delegate {@link MessageListener} for in-game Message's.
 *
 * @param <T> The Message type.
 */
public interface GameMessageListener<T extends Message> extends MessageListener<T> {

  @Override
  default void handle(Session session, T message) {
    Preconditions.checkArgument(session.hasAttr(Player.ATTRIBUTE_KEY),
        "No player present [message: %s session: %s]",
        message.getClass().getSimpleName(),
        session.toString());

    handle(session.attr(Player.ATTRIBUTE_KEY).get(), message);
  }

  /**
   * Handles the specified Message for the specified Player.
   *
   * @param player The Player which received the Message.
   * @param message The Message to process.
   */
  void handle(Player player, T message);

}