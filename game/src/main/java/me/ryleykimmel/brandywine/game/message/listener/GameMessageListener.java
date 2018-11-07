package me.ryleykimmel.brandywine.game.message.listener;

import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageListener;

/**
 * A delegate {@link MessageListener} for in-game Message's.
 *
 * @param <T> The Message type.
 */
public abstract class GameMessageListener<T extends Message> implements MessageListener<T> {

  /**
   * The World listening to this {@link Message}.
   */
  protected final World world;

  /**
   * Constructs a new {@link GameMessageListener}.
   *
   * @param world The World listening to this {@link Message}.
   */
  public GameMessageListener(World world) {
    this.world = world;
  }

  @Override
  public final void handle(Session session, T message) {
    world.findPlayer(session.getActivePlayer()).ifPresent(player -> handle(player, message));
  }

  /**
   * Handles the specified Message for the specified Player.
   *
   * @param player The Player which received the Message.
   * @param message The Message to process.
   */
  abstract void handle(Player player, T message);

}