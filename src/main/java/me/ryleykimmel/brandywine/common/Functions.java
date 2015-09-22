package me.ryleykimmel.brandywine.common;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A static utility class containing common/helpful utility functions.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Functions {

	/**
	 * Gets a Stream of instances of {@code T} for the specified Object within this Function.
	 * 
	 * <p>
	 * The intended use of this Function is for {@link Stream#flatMap(Function)}.
	 * 
	 * @param clazz The type of {@code T}.
	 * @return A Stream of instances of {@code T} if the specified Object {@link Class#isInstance(Object) is an instance of T} otherwise {@link Stream#empty()}.
	 */
	public static <T> Function<Object, Stream<T>> instancesOf(Class<T> clazz) {
		return object -> clazz.isInstance(object) ? Stream.of(clazz.cast(object)) : Stream.empty();
	}

	/**
	 * Sole private constructor to discourage instantiation of this class.
	 */
	private Functions() {
	}

}