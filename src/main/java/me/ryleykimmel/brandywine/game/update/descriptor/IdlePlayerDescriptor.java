package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes when a Player is idle. (not moving)
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class IdlePlayerDescriptor extends PlayerDescriptor {

	/**
	 * Constructs a new {@link IdlePlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player we are updating.
	 * @param tickets The appearance tickets.
	 */
	public IdlePlayerDescriptor(Player player, int[] tickets) {
		super(player, tickets);
	}

	@Override
	public void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		if (isBlockUpdatedRequired()) {
			builder.putBits(1, 1);
			builder.putBits(2, 0);
		} else {
			builder.putBits(1, 0);
		}
	}

}