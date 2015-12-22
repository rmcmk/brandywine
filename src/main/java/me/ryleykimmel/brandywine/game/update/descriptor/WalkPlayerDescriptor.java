package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.Updater;

/**
 * A PlayerDescriptor which encodes the walking movement of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public class WalkPlayerDescriptor extends PlayerDescriptor {

  /**
   * The direction of movement.
   */
  private final Direction direction;

  public WalkPlayerDescriptor(Player player, Updater updater) {
    this(player, updater, player.getFirstDirection());
  }

  public WalkPlayerDescriptor(Player player, Updater updater, Direction direction) {
    super(player, updater);
    this.direction = direction;
  }

  public Direction getDirection() {
    return direction;
  }

}
