package me.ryleykimmel.brandywine.game.event;

import com.google.common.base.MoreObjects;

import java.util.HashSet;
import java.util.Set;

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
   */
  public void notify(E event) {
    consumers.forEach(consumer -> consumer.accept(event));
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("type", type)
            .add("consumers", consumers)
            .toString();
  }

}
