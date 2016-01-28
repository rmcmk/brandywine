package me.ryleykimmel.brandywine.game.update.descriptor;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.Updater;

/**
 * A PlayerDescriptor which encodes the adding of a Player.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AddPlayerDescriptor extends PlayerDescriptor {

  private final int index;
  private final Position otherPosition;
  private final Position position;

  public AddPlayerDescriptor(Player player, Position otherPosition, Updater updater) {
    this(player, updater, player.getIndex(), player.getPosition(), otherPosition);
  }

  public AddPlayerDescriptor(Player player, Updater updater, int index, Position position, Position otherPosition) {
    super(player, updater);
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

}
