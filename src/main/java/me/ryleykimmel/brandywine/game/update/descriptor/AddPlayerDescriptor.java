package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes the adding of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AddPlayerDescriptor extends PlayerDescriptor {

	/**
	 * Constructs a new {@link AddPlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player we are updating.
	 * @param tickets The appearance tickets.
	 */
	public AddPlayerDescriptor(Player player, int[] tickets) {
		super(player, tickets);
	}

	@Override
	public void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(11, mob.getIndex());
		builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
		builder.putBits(1, 1);

		Position position = mob.getPosition();
		builder.putBits(5, position.getY() - message.getPosition().getY());
		builder.putBits(5, position.getX() - message.getPosition().getX());
	}

}