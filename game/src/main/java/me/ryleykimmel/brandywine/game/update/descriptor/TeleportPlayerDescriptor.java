package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * A PlayerDescriptor which encodes the teleportation of a Player.
 */
public final class TeleportPlayerDescriptor extends PlayerDescriptor {

  /**
   * A flag denoting whether or not the map region has changed.
   */
  private final boolean mapRegionChanged;

  /**
   * The Position of our Player before teleporting.
   */
  private final Position position;

  /**
   * The last known region of our Player.
   */
  private final Position lastKnownRegion;

  /**
   * Constructs a new TeleportPlayerDescriptor.
   *
   * @param player The Player who is teleporting.
   * @param position The Position of our Player before teleporting.
   * @param lastKnownRegion The last known region of our Player.
   */
  public TeleportPlayerDescriptor(Player player, Position position, Position lastKnownRegion) {
    this(player, player.hasMapRegionChanged(), position, lastKnownRegion);
  }

  /**
   * Constructs a new TeleportPlayerDescriptor.
   *
   * @param player The Player who is teleporting.
   * @param mapRegionChanged A flag denoting whether or not the map region has changed.
   * @param position The Position of our Player before teleporting.
   * @param lastKnownRegion The last known region of our Player.
   */
  public TeleportPlayerDescriptor(Player player, boolean mapRegionChanged, Position position,
      Position lastKnownRegion) {
    super(player);
    this.mapRegionChanged = mapRegionChanged;
    this.position = position;
    this.lastKnownRegion = lastKnownRegion;
  }

  @Override
  public void encodeState(FrameBuilder builder, FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 3);

    builder.putBits(2, position.getHeight());

    builder.putBits(1, mapRegionChanged ? 0 : 1);
    builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);

    builder.putBits(7, position.getLocalY(lastKnownRegion));
    builder.putBits(7, position.getLocalX(lastKnownRegion));
  }

}
