package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.npc.Npc;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.task.PlayerUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.PostPlayerUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.PrePlayerUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.UpdateTask;

/**
 * An implementation of a {@link Updater} which runs on a single thread.
 * 
 * @author Graham @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class SequentialUpdater extends Updater {

  @Override
  public void update(MobRepository<Player> players, MobRepository<Npc> npcs) {
    for (Player player : players) {
      UpdateTask task = new PrePlayerUpdateTask(player);
      task.run();
    }

    for (Player player : players) {
      UpdateTask task = new PlayerUpdateTask(this, player, players);
      task.run();
    }

    for (Player player : players) {
      UpdateTask task = new PostPlayerUpdateTask(player);
      task.run();
    }
  }

}
