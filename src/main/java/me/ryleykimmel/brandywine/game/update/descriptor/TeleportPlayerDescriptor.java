package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.Updater;

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

	public TeleportPlayerDescriptor(Player player, Updater updater) {
		this(player, updater, player.hasMapRegionChanged());
	}

	public TeleportPlayerDescriptor(Player player, Updater updater, boolean mapRegionChanged) {
		super(player, updater);
		this.mapRegionChanged = mapRegionChanged;
	}

	public boolean hasMapRegionChanged() {
		return mapRegionChanged;
	}

}