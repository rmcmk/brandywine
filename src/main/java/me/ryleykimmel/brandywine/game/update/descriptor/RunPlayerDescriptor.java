package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes running movement of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RunPlayerDescriptor extends PlayerDescriptor {

	/**
	 * The first direction of movement.
	 */
	private final Direction first;

	/**
	 * The second direction of movement.
	 */
	private final Direction second;

	/**
	 * Constructs a new {@link RunPlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player we are updating.
	 * @param tickets The appearance tickets.
	 */
	public RunPlayerDescriptor(Player player, int[] tickets) {
		super(player, tickets);
		this.first = player.getFirstDirection();
		this.second = player.getSecondDirection();
	}

	@Override
	public void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(1, 1);
		builder.putBits(2, 2);
		builder.putBits(3, first.getValue());
		builder.putBits(3, second.getValue());
		builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
	}

}