package me.ryleykimmel.brandywine.game.model.inter;

import me.ryleykimmel.brandywine.game.event.Event;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * An Event which occurs when all Interfaces are closed.
 */
public final class InterfacesClosedEvent extends Event {

  /**
   * The Player whose Interfaces closed.
   */
  private final Player player;

  /**
   * Constructs a new InterfacesClosedEvent.
   *
   * @param player The Player whose Interfaces closed.
   */
  public InterfacesClosedEvent(Player player) {
    this.player = player;
  }

  /**
   * Gets the Player whose Interfaces closed.
   *
   * @return The Player whose Interfaces closed.
   */
  public Player getPlayer() {
    return player;
  }

}
