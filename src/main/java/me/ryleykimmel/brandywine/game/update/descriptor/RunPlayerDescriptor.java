package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.Updater;

/**
 * A PlayerDescriptor which encodes running movement of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class RunPlayerDescriptor extends PlayerDescriptor {

	/**
	 * The first direction of movement.
	 */
	private final Direction firstDirection;

	/**
	 * The second direction of movement.
	 */
	private final Direction secondDirection;

	public RunPlayerDescriptor(Player player, Updater updater) {
		this(player, updater, player.getFirstDirection(), player.getSecondDirection());
	}
	
	public RunPlayerDescriptor(Player player, Updater updater, Direction firstDirection, Direction secondDirection) {
		super(player, updater);
		this.firstDirection = firstDirection;
		this.secondDirection = secondDirection;
	}

	public Direction getFirstDirection() {
		return firstDirection;
	}

	public Direction getSecondDirection() {
		return secondDirection;
	}

}