package me.ryleykimmel.brandywine.game.event;

/**
 * A type of event that may occur in the game world.
 */
public abstract class Event {

  /**
   * Indicates whether or not the Event chain has been terminated.
   */
  private boolean terminated;

  /**
   * Terminates the Event chain.
   */
  public final void terminate() {
    terminated = true;
  }

  /**
   * Returns whether or not the Event chain has been terminated.
   *
   * @return {@code true} if the Event chain has been terminated, otherwise {@code false}.
   */
  public final boolean terminated() {
    return terminated;
  }

}
