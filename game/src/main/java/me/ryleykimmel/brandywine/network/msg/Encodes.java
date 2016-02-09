package me.ryleykimmel.brandywine.network.msg;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type encodes Messages as specified by {@link Encodes#value()}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Encodes {

  /**
   * Represents the Message class the annotated type encodes.
   *
   * @return The Message the annotated type encodes.
   */
  Class<? extends Message> value();

}
