package me.ryleykimmel.brandywine.game.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
