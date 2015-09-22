package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * A PlayerDescriptor which encodes the teleportation of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class TeleportPlayerDescriptor extends PlayerDescriptor {

	/**
	 * A flag denoting whether or not the map region has changed.
	 */
	private final boolean mapRegionChanged;

	/**
	 * Constructs a new {@link TeleportPlayerDescriptor} with the specified Player and appearance tickets.
	 * 
	 * @param player The Player we are updating.
	 * @param tickets The appearance tickets.
	 */
	public TeleportPlayerDescriptor(Player player, int[] tickets) {
		super(player, tickets);
		this.mapRegionChanged = player.hasMapRegionChanged();
	}

	@Override
	public void encodeDescriptor(PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);

		Position position = message.getPosition();
		builder.putBits(2, position.getHeight());
		builder.putBits(1, mapRegionChanged ? 0 : 1);
		builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
		builder.putBits(7, position.getLocalY(message.getLastKnownRegion()));
		builder.putBits(7, position.getLocalX(message.getLastKnownRegion()));
	}

}