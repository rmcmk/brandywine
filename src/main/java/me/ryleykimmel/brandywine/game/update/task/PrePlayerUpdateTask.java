package me.ryleykimmel.brandywine.game.update.task;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.msg.impl.RebuildRegionMessage;

/**
 * An UpdateTask which performs pre-update logic, before updating has occurred.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class PrePlayerUpdateTask implements UpdateTask {

  /**
   * The Player we are performing pre-update logic for.
   */
  private final Player player;

  /**
   * Constructs a new {@link PrePlayerUpdateTask} with the specified Player.
   * 
   * @param player The Player we are performing pre-update logic for.
   */
  public PrePlayerUpdateTask(Player player) {
    this.player = player;
  }

  @Override
  public void run() {
    player.getMovementQueue().pulse();

    if (player.isTeleporting()) {
      player.resetViewingDistance();
    }

    if (isRegionChangeRequired()) {
      Position position = player.getPosition();
      player.setLastKnownRegion(position);
      player.write(new RebuildRegionMessage(position));
    }
  }

  /**
   * Determines if a region change is required for the specified Player.
   * 
   * @return {@code true} if a region change is required and the region needs rebuilt, otherwise
   * {@code false}.
   */
  private boolean isRegionChangeRequired() {
    Position lastKnownRegion = player.getLastKnownRegion();
    Position position = player.getPosition();

    int deltaX = position.getLocalX(lastKnownRegion);
    int deltaY = position.getLocalY(lastKnownRegion);

    return deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY >= 88;
  }

  @Override
  public void exceptionCaught(Throwable cause) {
    cause.printStackTrace();
    player.disconnect();
  }

}
