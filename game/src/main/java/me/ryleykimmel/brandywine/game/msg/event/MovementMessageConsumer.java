package me.ryleykimmel.brandywine.game.msg.event;

import java.util.Queue;

import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.model.MovementQueue;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.MovementMessage;

/**
 * Handles the {@link MovementMessage}.
 */
public final class MovementMessageConsumer implements EventConsumer<MovementMessage> {

  private final Player player;

  public MovementMessageConsumer(Player player) {
    this.player = player;
  }

  @Override
  public void accept(MovementMessage message) {
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
