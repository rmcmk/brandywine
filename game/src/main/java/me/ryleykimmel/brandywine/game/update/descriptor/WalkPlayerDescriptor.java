package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;

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

  public WalkPlayerDescriptor(Player player) {
    this(player, player.getFirstDirection());
  }

  public WalkPlayerDescriptor(Player player, Direction direction) {
    super(player);
    this.direction = direction;
  }

  public Direction getDirection() {
    return direction;
  }

}
