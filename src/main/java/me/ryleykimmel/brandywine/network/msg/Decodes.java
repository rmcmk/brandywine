package me.ryleykimmel.brandywine.network.msg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type decodes Messages, as specified by {@link Decodes#value()}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Decodes {

  /**
   * The opcodes of the Frames the annotated type decodes.
   *
   * @return The opcodes of the Frames the annotated type decodes.
   */
  int[] value();

}
