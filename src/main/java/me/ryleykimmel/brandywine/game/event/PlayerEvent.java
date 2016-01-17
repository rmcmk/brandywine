package me.ryleykimmel.brandywine.game.event;

import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Represents an Event which a Player invoked.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class PlayerEvent extends Event {

  /**
   * The Player who invoked this Event.
   */
  protected final Player player;

  /**
   * Constructs a new {@link PlayerEvent} with the specified Player.
   * 
   * @param player The Player who invoked this Event.
   */
  public PlayerEvent(Player player) {
    this.player = player;
  }

  /**
   * Gets the Player who invoked this Event.
   * 
   * @return The Player who invoked this Event.
   */
  public final Player getPlayer() {
    return player;
  }

}
