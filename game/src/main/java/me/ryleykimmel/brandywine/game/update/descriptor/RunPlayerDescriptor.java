package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * A PlayerDescriptor which encodes running movement of a Player.
 */
public final class RunPlayerDescriptor extends PlayerDescriptor {

  /**
   * The first Direction of movement.
   */
  private final Direction firstDirection;

  /**
   * The second Direction of movement.
   */
  private final Direction secondDirection;

  /**
   * Constructs a new RunPlayerDescriptor.
   *
   * @param player The Player who is running.
   */
  public RunPlayerDescriptor(Player player) {
    this(player, player.getFirstDirection(), player.getSecondDirection());
  }

  /**
   * Constructs a new RunPlayerDescriptor.
   *
   * @param player The Player who is running.
   * @param firstDirection The first Direction of movement.
   * @param secondDirection The second Direction of movement.
   */
  public RunPlayerDescriptor(Player player, Direction firstDirection, Direction secondDirection) {
    super(player);
    this.firstDirection = firstDirection;
    this.secondDirection = secondDirection;
  }

  @Override
  public void encodeState(FrameBuilder builder, FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 2);
    builder.putBits(3, firstDirection.getValue());
    builder.putBits(3, secondDirection.getValue());
    builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
  }

}
