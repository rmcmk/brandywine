package me.ryleykimmel.brandywine.game.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

import me.ryleykimmel.brandywine.network.msg.impl.ResetDestinationMessage;

/**
 * A queue of {@link Direction}s which a {@link Mob} will follow.
 *
 * @author Graham @author Major @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class MovementQueue {

  /**
   * The Mob who owns this MovementQueue.
   */
  private final Mob mob;

  /**
   * A {@link Deque} of Points in this MovementQueue.
   */
  private final Deque<Position> points = new ArrayDeque<>();

  /**
   * A {@link Deque} of previously visited Points in this MovementQueue.
   */
  private final Deque<Position> previousPoints = new ArrayDeque<>();

  /**
   * Flag indicating running status of this queue.
   */
  private boolean running;

  /**
   * Constructs a new {@link MovementQueue} with the specified Mob.
   *
   * @param mob The Mob who owns this MovementQueue.
   */
  public MovementQueue(Mob mob) {
    this.mob = mob;
  }

  /**
   * Adds the first step into the MovementQueue.
   *
   * @param next The Position to add.
   */
  public void addFirstStep(Position next) {
    points.clear();
    running = false;

    /*
     * We need to connect 'current' and 'next' whilst accounting for the fact that the client and
     * server might be out of sync (i.e. what the client thinks is 'current' is different to what
     * the server thinks is 'current').
     *
     * First try to connect them via points from the previous queue.
     */
    Queue<Position> backtrack = new ArrayDeque<>();

    while (!previousPoints.isEmpty()) {
      Position position = previousPoints.pollLast();
      backtrack.add(position);

      if (position.equals(next)) {
        backtrack.forEach(this::addStep);
        previousPoints.clear();
        return;
      }
    }

    /* If that doesn't work, connect the points directly. */
    previousPoints.clear();
    addStep(next);
  }

  /**
   * Adds a step to this MovementQueue.
   *
   * @param next The {@link Position} of the step.
   */
  public void addStep(Position next) {
    Position current = points.peekLast();

    /*
     * If current equals next, addFirstStep doesn't end up adding anything points queue. This makes
     * peekLast() return null. If it does, the correct behaviour is to fill it in with
     * mob.getPosition().
     */
    if (current == null) {
      current = mob.getPosition();
    }

    addStep(current, next);
  }

  /**
   * Adds the {@code next} step to this MovementQueue.
   *
   * @param current The current {@link Position}. @param next The next Position.
   */
  private void addStep(Position current, Position next) {
    int nextX = next.getX(), nextY = next.getY();
    int deltaX = nextX - current.getX();
    int deltaY = nextY - current.getY();

    int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));

    for (int count = 0; count < max; count++) {
      if (deltaX < 0) {
        deltaX++;
      } else if (deltaX > 0) {
        deltaX--;
      }

      if (deltaY < 0) {
        deltaY++;
      } else if (deltaY > 0) {
        deltaY--;
      }

      points.add(new Position(nextX - deltaX, nextY - deltaY, next.getHeight()));
    }
  }

  /**
   * Clears the MovementQueue.
   */
  public void clear() {
    points.clear();
    previousPoints.clear();
    running = false;
    mob.write(new ResetDestinationMessage());
  }

  /**
   * Called every pulse, updates the queue.
   */
  public void pulse() {
    Position position = mob.getPosition();
    int height = position.getHeight();

    Direction firstDirection = Direction.NONE;
    Direction secondDirection = Direction.NONE;

    Position next = points.poll();
    if (next != null) {
      previousPoints.add(next);
      firstDirection = Direction.between(position, next);
      position = new Position(next.getX(), next.getY(), height);

      if (running) {
        next = points.poll();
        if (next != null) {
          previousPoints.add(next);
          secondDirection = Direction.between(position, next);
          position = new Position(next.getX(), next.getY(), height);
        }
      }
    }

    mob.setDirections(firstDirection, secondDirection);
    mob.setPosition(position);
  }

  /**
   * Sets the running status of this queue.
   *
   * @param running The running status of this queue.
   */
  public void setRunning(boolean running) {
    this.running = running;
  }

  /**
   * Gets the size of the queue.
   *
   * @return The size of the queue.
   */
  public int size() {
    return points.size();
  }

}
