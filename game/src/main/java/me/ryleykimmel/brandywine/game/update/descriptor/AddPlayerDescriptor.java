package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * A PlayerDescriptor which encodes the adding of a Player.
 */
public final class AddPlayerDescriptor extends PlayerDescriptor {

  /**
   * The index within the {@link MobRepository} of our Player.
   */
  private final int index;

  /**
   * The Position of the Player to be added.
   */
  private final Position otherPosition;

  /**
   * Our Player's Position at the time of adding.
   */
  private final Position position;

  /**
   * Constructs a new AddPlayerDescriptor.
   *
   * @param player The Player we are adding other Player's for.
   * @param otherPosition The Position of the added Player.
   */
  public AddPlayerDescriptor(Player player, Position otherPosition) {
    this(player, player.getIndex(), player.getPosition(), otherPosition);
  }

  /**
   * Constructs a new AddPlayerDescriptor.
   *
   * @param player The Player we are adding other Player's for.
   * @param index The index within the {@link MobRepository} of our Player.
   * @param position Our Player's Position at the time of adding.
   * @param otherPosition The Position of the Player to be added.
   */
  public AddPlayerDescriptor(Player player, int index, Position position, Position otherPosition) {
    super(player);
    this.index = index;
    this.position = position;
    this.otherPosition = otherPosition;
  }

  @Override
  public void encodeState(FrameBuilder builder, FrameBuilder blockBuilder) {
    builder.putBits(11, index);
    builder.putBits(1, isBlockUpdatedRequired() ? 1 : 0);
    builder.putBits(1, 1);

    builder.putBits(5, position.getDeltaY(otherPosition));
    builder.putBits(5, position.getDeltaX(otherPosition));
  }

}
