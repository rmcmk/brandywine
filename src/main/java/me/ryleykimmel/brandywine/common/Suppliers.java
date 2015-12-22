package me.ryleykimmel.brandywine.common;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * A static-utility class containing helper methods for supplying objects.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Suppliers {

  /**
   * Supplies an {@link IllegalArgumentException} with the specified message.
   *
   * @param message The detail error message @return The supplied exception.
   */
  public static Supplier<? extends RuntimeException> illegal(String message) {
    return () -> new IllegalArgumentException(message);
  }

  /**
   * Supplies a {@link Collection} to a {@link Collector}, ideal for supplying custom collections to
   * {@link Stream#collect}
   *
   * @param collection The Collection to supply. @return a Collector which collects all the input
   * elements into a Collection, in encounter order.
   */
  public static <T, C extends Collection<T>> Collector<T, ?, C> collection(C collection) {
    return Collectors.toCollection(() -> collection);
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private Suppliers() {}

}
