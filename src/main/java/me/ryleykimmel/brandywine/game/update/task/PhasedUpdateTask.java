package me.ryleykimmel.brandywine.game.update.task;

import java.util.concurrent.Phaser;

/**
 * An UpdateTask which wraps around another UpdateTask and notifies the specified Phaser when the
 * task has completed.
 * 
 * @author Graham
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class PhasedUpdateTask implements UpdateTask {

  /**
   * The Phaser to notify when the task has completed.
   */
  private final Phaser phaser;

  /**
   * The UpdateTask to perform.
   */
  private final UpdateTask task;

  /**
   * Constructs a new {@link PhasedUpdateTask} with the specified Phaser and UpdateTask.
   * 
   * @param phaser The Phaser to notify when the task has completed.
   * @param task The UpdateTask to perform.
   */
  public PhasedUpdateTask(Phaser phaser, UpdateTask task) {
    this.phaser = phaser;
    this.task = task;
  }

  @Override
  public void run() {
    try {
      task.run();
    } catch (Exception cause) {
      cause.printStackTrace();
    } finally {
      phaser.arriveAndDeregister();
    }
  }

}
