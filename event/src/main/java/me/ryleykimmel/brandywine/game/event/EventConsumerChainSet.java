package me.ryleykimmel.brandywine.game.event;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * A set of {@link EventConsumerChain}s.
 */
public final class EventConsumerChainSet {

  /**
   * The Map of Event Classes to EventConsumerChains.
   */
  private final Map<Class<? extends Event>, EventConsumerChain<? extends Event>> chains = new HashMap<>();

  /**
   * Notifies the appropriate {@link EventConsumerChain} that an {@link Event} has occurred.
   *
   * @param event The Event.
   * @return {@code true} if the Event should continue on with its outcome.
   */
  public <E extends Event> boolean notify(E event) {
    @SuppressWarnings("unchecked")
    EventConsumerChain<E> chain = (EventConsumerChain<E>) chains.get(event.getClass());
    return chain == null || chain.notify(event);
  }

  /**
   * Places the {@link EventConsumerChain} into this set.
   *
   * @param clazz The {@link Class} to associate the EventListenerChain with.
   * @param consumer The EventListenerChain.
   */
  public <E extends Event> void addConsumer(Class<E> clazz, EventConsumer<E> consumer) {
    @SuppressWarnings("unchecked")
    EventConsumerChain<E> chain = (EventConsumerChain<E>) chains.computeIfAbsent(clazz,
      EventConsumerChain::new);
    chain.add(consumer);
  }

  /**
   * Places the {@link EventConsumerChain} into this set.
   *
   * @param consumer The EventListenerChain.
   */
  public <E extends Event> void addConsumer(EventConsumer<E> consumer) {
    Consumes consumes = consumer.getClass().getAnnotation(Consumes.class);
    Preconditions.checkNotNull(consumes,
      "EventConsumer must be annotated with @Consumes to infer Event type.");
    @SuppressWarnings("unchecked")
    Class<E> clazz = (Class<E>) consumes.value();
    addConsumer(clazz, consumer);
  }

}
