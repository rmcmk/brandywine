package me.ryleykimmel.brandywine.game.update.task;

import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * An UpdateTask which performs post-update logic, after updating has occurred.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class PostPlayerUpdateTask implements UpdateTask {

  /**
   * The Player we are performing post-update logic for.
   */
  private final Player player;

  /**
   * Constructs a new {@link PostPlayerUpdateTask} with the specified Player.
   * 
   * @param player The Player we are performing post-update logic for.
   */
  public PostPlayerUpdateTask(Player player) {
    this.player = player;
  }

  @Override
  public void run() {
    player.reset();

    if (player.isExcessivePlayersSet()) {
      player.decrementViewingDistance();
      player.resetExcessivePlayers();
    } else {
      player.incrementViewingDistance();
    }

    player.getSession().flush();
  }

}
