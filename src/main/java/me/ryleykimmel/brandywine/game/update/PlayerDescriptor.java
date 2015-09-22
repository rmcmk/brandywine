package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.UpdateFlags.UpdateFlag;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.game.update.descriptor.IdlePlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.RunPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.TeleportPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.WalkPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.DataOrder;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * Describes a segment of the Player updating prodecure.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class PlayerDescriptor extends Descriptor<Player> {

	/**
	 * Creates an appropriate PlayerDescriptor.
	 * 
	 * @param player The Player this segment updates.
	 * @param tickets The Players appearance tickets.
	 * @return A new {@link PlayerDescriptor} with the specified Player and appearance tickets, never {@code null}.
	 */
	public static PlayerDescriptor create(Player player, int[] tickets) {
		if (player.isTeleporting()) {
			return new TeleportPlayerDescriptor(player, tickets);
		} else if (player.getFirstDirection() == Direction.NONE) {
			return new IdlePlayerDescriptor(player, tickets);
		} else if (player.getSecondDirection() == Direction.NONE) {
			return new WalkPlayerDescriptor(player, tickets);
		} else {
			return new RunPlayerDescriptor(player, tickets);
		}
	}

	/**
	 * Constructs a new {@link PlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player this segment updates.
	 * @param tickets The Players appearance tickets.
	 */
	public PlayerDescriptor(Player player, int[] tickets) {
		super(player);

		if (player.isActive()) {
			int index = player.getIndex() - 1;
			int ticket = player.getAppearanceTicket();
			if (tickets[index] != ticket || player.isUpdateFlagged(UpdateFlag.APPEARANCE)) {
				tickets[index] = ticket;
				addBlock(new AppearancePlayerBlock(player));
			}
		}

		if (player.isUpdateFlagged(UpdateFlag.CHAT)) {
			addBlock(new ChatPlayerBlock(player));
		}
	}

	/**
	 * Encodes this Descriptor and UpdateBlocks.
	 * 
	 * @param message The Players update message.
	 * @param builder The Descriptors FrameBuilder.
	 * @param blockBuilder The UpdateBlocks FrameBuilder.
	 */
	public void encode(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		encodeDescriptor(message, builder, blockBuilder);

		if (!isBlockUpdatedRequired()) {
			return;
		}

		int mask = 0;
		for (UpdateBlock<Player> block : getBlocks()) {
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

	/**
	 * Encodes an UpdateBlock.
	 * 
	 * @param message The Players update message.
	 * @param blockBuilder The UpdateBlocks FrameBuilder.
	 * @param type The UpdateBlocks type.
	 */
	private void encodeBlock(PlayerUpdateMessage message, FrameBuilder blockBuilder, Class<? extends PlayerBlock> type) {
		getBlock(type).ifPresent(block -> block.encode(message, blockBuilder));
	}

	/**
	 * Encodes this Descriptor.
	 * 
	 * @param message The Players update message.
	 * @param builder The Descriptors FrameBuilder.
	 * @param blockBuilder The UpdateBlocks FrameBuilder.
	 */
	public abstract void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder);

}