package me.ryleykimmel.brandywine.game.event;

import me.ryleykimmel.brandywine.game.model.player.Player;

public abstract class PlayerEvent extends Event {

  protected final Player player;

  public PlayerEvent(Player player) {
    this.player = player;
  }

  public final Player getPlayer() {
    return player;
  }

}
