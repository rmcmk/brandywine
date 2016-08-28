package me.ryleykimmel.brandywine.game.model.player;

import me.ryleykimmel.brandywine.game.event.Event;

/**
 * An Event which occurs when a Player is initialized.
 */
public final class InitializePlayerEvent extends Event {

  /**
   * The Player that was initialized.
   */
  private final Player player;

  /**
   * Constructs a new {@link InitializePlayerEvent}.
   *
   * @param player The Player that was initialized.
   */
  public InitializePlayerEvent(Player player) {
    this.player = player;
  }

  /**
   * Gets the Player that was initialized.
   *
   * @return The Player that was initialized.
   */
  public Player getPlayer() {
    return player;
  }

}
