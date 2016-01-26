package me.ryleykimmel.brandywine.game.event.impl;

import me.ryleykimmel.brandywine.game.event.Event;
import me.ryleykimmel.brandywine.game.model.Mob;

/**
 * Represents an Event which a Mob invoked.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The type of the Mob who invoked this Event.
 */
public abstract class MobEvent<T extends Mob> extends Event {

  /**
   * The Mob who invoked this Event.
   */
  protected final T mob;

  /**
   * Constructs a new {@link MobEvent} with the specified Mob.
   * 
   * @param mob The Mob who invoked this Event.
   */
  public MobEvent(T mob) {
    this.mob = mob;
  }

  /**
   * Gets the Mob who invoked this Event.
   * 
   * @return The Mob who invoked this Event.
   */
  public final T getMob() {
    return mob;
  }

}
