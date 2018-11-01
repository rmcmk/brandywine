package me.ryleykimmel.brandywine.game.message;

import java.util.Queue;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.network.message.Message;

/**
 * A {@link Message} which requests movement.
 */
public final class MovementMessage extends Message {

  /**
   * Whether or not we should run.
   */
  private final boolean running;

  /**
   * A {@link Queue} of Positions the client is attempting to move to.
   */
  private final Queue<Position> steps;

  /**
   * Constructs a new {@link MovementMessage} with the specified Queue of Positions and the running flag.
   *
   * @param steps A {@link Queue} of Positions the client is attempting to move to.
   * @param running Whether or not we should run.
   */
  public MovementMessage(Queue<Position> steps, boolean running) {
    this.steps = steps;
    this.running = running;
  }

  /**
   * Gets the Queue of Positions the client is attempting to move to.
   *
   * @return The Queue of Positions the client is attempting to move to.
   */
  public Queue<Position> getSteps() {
    return steps;
  }

  /**
   * Gets whether or not we should run.
   *
   * @return {@code true} if we should run otherwise {@code false}.
   */
  public boolean isRunning() {
    return running;
  }

}
