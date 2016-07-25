package me.ryleykimmel.brandywine.game.event;

import java.util.function.Consumer;

/**
 * Consumes the specified Event.
 *
 * @param <E> The type of Event.
 */
public interface EventConsumer<E extends Event> extends Consumer<E> {

}
