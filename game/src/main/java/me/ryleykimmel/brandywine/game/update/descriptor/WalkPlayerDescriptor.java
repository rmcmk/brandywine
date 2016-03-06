package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

/**
 * A PlayerDescriptor which encodes the walking movement of a Player.
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

  @Override
  public void encodeState(FrameBuilder builder, FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 1);
    builder.putBits(3, direction.getValue());
    builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
  }

}
