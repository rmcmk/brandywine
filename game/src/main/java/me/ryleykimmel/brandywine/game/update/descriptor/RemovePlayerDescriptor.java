package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;

/**
 * A PlayerDescriptor which encodes the removal of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RemovePlayerDescriptor extends PlayerDescriptor {

  public RemovePlayerDescriptor(Player player) {
    super(player);
    clear();
  }

}
