package me.ryleykimmel.brandywine.network.msg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type handles Messages as specified by {@link Handles#value()}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handles {

  /**
   * Represents the Message class the annotated type handles.
   *
   * @return The Message the annotated type handles.
   */
  Class<? extends Message> value();

}
