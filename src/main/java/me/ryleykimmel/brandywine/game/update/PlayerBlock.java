package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * Represents an UpdateBlock for a Player Mob.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class PlayerBlock extends UpdateBlock<Player> {

	/**
	 * Constructs a new {@link PlayerBlock} with the specified Player and mask.
	 * 
	 * @param player The Player being updated.
	 * @param mask The mask of this UpdateBlock.
	 */
	public PlayerBlock(Player player, int mask) {
		super(player, mask);
	}

	/**
	 * Encodes this UpdateBlock for the specified Player.
	 * 
	 * @param message The PlayerUpdateMessage.
	 * @param builder The FrameBuilder.
	 */
	public abstract void encode(PlayerUpdateMessage message, FrameBuilder builder);

}