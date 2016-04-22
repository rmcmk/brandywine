package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * A PlayerDescriptor which encodes the adding of a Player.
 */
public final class AddPlayerDescriptor extends PlayerDescriptor {

  private final int index;
  private final Position otherPosition;
  private final Position position;

  public AddPlayerDescriptor(Player player, Position otherPosition) {
    this(player, player.getIndex(), player.getPosition(), otherPosition);
  }

  public AddPlayerDescriptor(Player player, int index, Position position, Position otherPosition) {
    super(player);
    this.index = index;
    this.position = position;
    this.otherPosition = otherPosition;
  }

  public int getIndex() {
    return index;
  }

  public Position getPosition() {
    return position;
  }

  public Position getOtherPosition() {
    return otherPosition;
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
