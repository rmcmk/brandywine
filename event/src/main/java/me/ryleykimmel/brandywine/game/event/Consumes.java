package me.ryleykimmel.brandywine.game.event;

import java.lang.annotation.*;

/**
 * Signifies the {@link Event} an {@link EventConsumer} consumes.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Consumes {

    /**
     * Gets the {@link Event} type that a {@link EventConsumer} consumes.
     *
     * @return The Event type.
     */
    Class<? extends Event> value();

}
