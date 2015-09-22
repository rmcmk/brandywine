package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes the removal of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RemovePlayerDescriptor extends PlayerDescriptor {

	/**
	 * Constructs a new {@link RemovePlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player we are updating.
	 * @param tickets The appearance tickets.
	 */
	public RemovePlayerDescriptor(Player player, int[] tickets) {
		super(player, tickets);
	}

	@Override
	public void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);
	}

}