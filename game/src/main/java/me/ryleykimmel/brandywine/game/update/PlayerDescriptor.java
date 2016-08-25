package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.frame.DataOrder;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * Describes a segment of the Player updating procedure.
 */
public abstract class PlayerDescriptor extends Descriptor<Player> {

  /**
   * Constructs a new PlayerDescriptor.
   *
   * @param player The Player who owns this Descriptor.
   */
  public PlayerDescriptor(Player player) {
    super(player);
  }

  @Override
  public final void encode(FrameBuilder builder, FrameBuilder blockBuilder) {
    encodeState(builder, blockBuilder);

    if (!isBlockUpdatedRequired()) {
      return;
    }

    if (mask > 0xFF) {
      mask |= 0x40;
      blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, mask);
    } else {
      blockBuilder.put(DataType.BYTE, mask);
    }

    encodeBlock(blockBuilder, AppearancePlayerBlock.class);
    encodeBlock(blockBuilder, ChatPlayerBlock.class);
  }

}
