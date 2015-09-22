package me.ryleykimmel.brandywine.game.update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.npc.Npc;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.task.PhasedUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.PlayerUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.PostPlayerUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.PrePlayerUpdateTask;
import me.ryleykimmel.brandywine.game.update.task.UpdateTask;

/**
 * An implementation of a {@link Updater} which runs in a thread-pool. A {@link Phaser} is used to ensure that updating is complete before moving on.
 * 
 * @author Graham
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ParallelUpdater implements Updater {

	/**
	 * The Phaser used to control synchronization.
	 */
	private final Phaser phaser = new Phaser(1);

	/**
	 * A fixed ExecutorService thread-pool used for executing UpdateTasks.
	 */
	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder().setNameFormat("updater").build());

	@Override
	public void update(MobRepository<Player> players, MobRepository<Npc> npcs) {
		int playerCount = players.size();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			UpdateTask task = new PrePlayerUpdateTask(player);
			executor.submit(new PhasedUpdateTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			UpdateTask task = new PlayerUpdateTask(player, players);
			executor.submit(new PhasedUpdateTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			UpdateTask task = new PostPlayerUpdateTask(player);
			executor.submit(new PhasedUpdateTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();
	}

}