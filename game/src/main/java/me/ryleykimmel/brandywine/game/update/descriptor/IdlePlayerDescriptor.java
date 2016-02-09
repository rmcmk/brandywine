package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;

/**
 * A PlayerDescriptor which encodes when a Player is idle. (not moving)
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class IdlePlayerDescriptor extends PlayerDescriptor {

  public IdlePlayerDescriptor(Player player) {
    super(player);
  }

}
