package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes the walking movement of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public class WalkPlayerDescriptor extends PlayerDescriptor {

	/**
	 * The direction of movement.
	 */
	private final Direction direction;

	/**
	 * Constructs a new {@link WalkPlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player we are updating.
	 * @param tickets The appearance tickets.
	 */
	public WalkPlayerDescriptor(Player player, int[] tickets) {
		super(player, tickets);
		this.direction = player.getFirstDirection();
	}

	@Override
	public void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(1, 1);
		builder.putBits(2, 1);
		builder.putBits(3, direction.getValue());
		builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
	}

}