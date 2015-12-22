package me.ryleykimmel.brandywine.network.msg.handler;

import java.util.Queue;

import me.ryleykimmel.brandywine.game.model.MovementQueue;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Handles;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.MovementMessage;

/**
 * Handles the {@link MovementMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Handles(MovementMessage.class)
public final class MovementMessageHandler implements MessageHandler<MovementMessage> {

  @Override
  public void handle(GameSession session, MovementMessage message) {
    Player player = session.attr().get();

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
