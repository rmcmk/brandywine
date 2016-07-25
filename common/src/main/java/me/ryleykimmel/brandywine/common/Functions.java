package me.ryleykimmel.brandywine.common;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A static utility class containing common/helpful utility functions.
 */
public final class Functions {

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private Functions() {
  }

  /**
   * Gets a Stream of instances of {@code T} for the specified Object within this Function.
   * <p>
   * <p>
   * The intended use of this Function is for {@link Stream#flatMap(Function)}.
   *
   * @param clazz The type of {@code T}.
   * @return A Stream of instances of {@code T} if the specified Object
   * {@link Class#isInstance(Object) is an instance of T} otherwise {@link Stream#empty()}.
   */
  public static <T> Function<Object, Stream<T>> instancesOf(Class<T> clazz) {
    return object -> clazz.isInstance(object) ? Stream.of(clazz.cast(object)) : Stream.empty();
  }

  /**
   * Gets an IntStream of character values from the specified {@code char}s. Any char which maps to
   * a surrogate code point is passed through uninterpreted.
   * <p>
   * <p>
   * If the sequence is mutated while the stream is being read, the result is undefined.
   *
   * @param chars The chars to build the Stream from.
   * @return An IntStream of char values the specified {@code char}s
   */
  public static IntStream unboxedChars(char[] chars) {
    final class CharIterator implements PrimitiveIterator.OfInt {

      private int current = 0;

      @Override
      public void forEachRemaining(IntConsumer block) {
        for (; current < chars.length; current++) {
          block.accept(chars[current]);
        }
      }

      @Override
      public boolean hasNext() {
        return current < chars.length;
      }

      @Override
      public int nextInt() {
        if (hasNext()) {
          return (int) chars[current++];
        } else {
          throw new NoSuchElementException();
        }
      }

    }

    return StreamSupport.intStream(
      () -> Spliterators.spliterator(new CharIterator(), chars.length, Spliterator.ORDERED),
      Spliterator.SUBSIZED | Spliterator.SIZED | Spliterator.ORDERED, false);
  }

}
