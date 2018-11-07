package me.ryleykimmel.brandywine.game.message.listener;

import java.util.Queue;
import me.ryleykimmel.brandywine.game.message.MovementMessage;
import me.ryleykimmel.brandywine.game.model.MovementQueue;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Listener for the {@link MovementMessage}.
 */
public final class MovementMessageListener extends GameMessageListener<MovementMessage> {

  /**
   * Constructs a new {@link MovementMessageListener}.
   *
   * @param world The World listening to this {@link MovementMessage}.
   */
  public MovementMessageListener(World world) {
    super(world);
  }

  @Override
  public void handle(Player player, MovementMessage message) {
    MovementQueue queue = player.getMovementQueue();
    Queue<Position> steps = message.getSteps();

    Position step = steps.poll();
    if (step == null) {
      return;
    }

    queue.addFirstStep(step);

    while ((step = steps.poll()) != null) {
      queue.addStep(step);
    }

    queue.setRunning(message.isRunning() || player.isRunning());
  }

}