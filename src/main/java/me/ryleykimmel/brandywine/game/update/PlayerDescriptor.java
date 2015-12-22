package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.game.frame.DataOrder;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * Describes a segment of the Player updating prodecure.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class PlayerDescriptor extends Descriptor<Player, PlayerUpdateMessage> {

	public PlayerDescriptor(Player player, Updater updater) {
		super(player, updater);
	}

	/**
	 * Encodes this Descriptor and UpdateBlocks.
	 * 
	 * @param message The Players update message.
	 * @param builder The Descriptors FrameBuilder.
	 * @param blockBuilder The UpdateBlocks FrameBuilder.
	 */
	public void encode(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		updater.getDescriptorEncoders().encode(this, message, builder, blockBuilder);

		if (!isBlockUpdatedRequired()) {
			return;
		}

		int mask = 0;
		for (UpdateBlock block : getBlocks()) {
			mask |= block.getMask();
		}

		if (mask > 0xFF) {
			mask |= 0x40;
			blockBuilder.put(DataType.SHORT, DataOrder.LITTLE, mask);
		} else {
			blockBuilder.put(DataType.BYTE, mask);
		}

		encodeBlock(message, blockBuilder, AppearancePlayerBlock.class);
		encodeBlock(message, blockBuilder, ChatPlayerBlock.class);
	}

}