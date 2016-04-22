package me.ryleykimmel.brandywine.game.event;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.MoreObjects;

/**
 * A chain of {@link EventConsumer}s.
 *
 * @param <E> The type of {@link Event} the consumers in this chain listen for.
 */
public final class EventConsumerChain<E extends Event> {

  /**
   * The Set of EventConsumers.
   */
  private final Set<EventConsumer<E>> consumers = new HashSet<>();

  /**
   * The Class type of this chain.
   */
  private final Class<E> type;

  /**
   * Constructs a new {@link EventConsumerChain}.
   *
   * @param type The {@link Class} type of this chain.
   */
  public EventConsumerChain(Class<E> type) {
    this.type = type;
  }

  /**
   * Adds an {@link EventConsumer} to this chain.
   *
   * @param consumer The EventConsumer to add.
   */
  public void add(EventConsumer<E> consumer) {
    consumers.add(consumer);
  }

  /**
   * Notifies each {@link EventConsumer} in this chain that an {@link Event} has occurred.
   *
   * @param event The event.
   * @return {@code true} if the Event should continue on with its outcome.
   */
  public boolean notify(E event) {
    for (EventConsumer<E> consumer : consumers) {
      consumer.accept(event);

      if (event.terminated()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("type", type).add("consumers", consumers)
        .toString();
  }

}
