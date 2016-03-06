package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

/**
 * A PlayerDescriptor which encodes when a Player is idle. (not moving)
 */
public final class IdlePlayerDescriptor extends PlayerDescriptor {

  public IdlePlayerDescriptor(Player player) {
    super(player);
  }

  @Override
  public void encodeState(FrameBuilder builder, FrameBuilder blockBuilder) {
    if (isBlockUpdatedRequired()) {
      builder.putBits(1, 1);
      builder.putBits(2, 0);
    } else {
      builder.putBits(1, 0);
    }
  }

}
