package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Position;
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

  private final Position position;

  private final Position lastKnownRegion;

  public TeleportPlayerDescriptor(Player player, Position position, Position lastKnownRegion,
      Updater updater) {
    this(player, updater, player.hasMapRegionChanged(), position, lastKnownRegion);
  }

  public TeleportPlayerDescriptor(Player player, Updater updater, boolean mapRegionChanged,
      Position position, Position lastKnownRegion) {
    super(player, updater);
    this.mapRegionChanged = mapRegionChanged;
    this.position = position;
    this.lastKnownRegion = lastKnownRegion;
  }

  public boolean hasMapRegionChanged() {
    return mapRegionChanged;
  }

  public Position getPosition() {
    return position;
  }

  public Position getLastKnownRegion() {
    return lastKnownRegion;
  }

}
