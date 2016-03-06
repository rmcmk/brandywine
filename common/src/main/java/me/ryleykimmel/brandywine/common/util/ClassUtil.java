package me.ryleykimmel.brandywine.common.util;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * A static-utility class containing extension or helper methods for {@link Class classes} or
 * generic objects.
 */
public final class ClassUtil {

  /**
   * Returns the specified {@link Annotation} for the potentially annotated object if it exists.
   *
   * @param object The Object to test contains the specified Annotation.
   * @param annotation The Annotation to check the specified Class for.
   * @return The Annotation if and only if it exists otherwise {@link Optional#empty()}.
   */
  public static <T extends Annotation, V> Optional<T> getAnnotation(V object, Class<T> annotation) {
    return getAnnotation(object.getClass(), annotation);
  }

  /**
   * Returns the specified {@link Annotation} for the potentially annotated {@link Class} if it
   * exists.
   *
   * @param clazz The Class to test contains the specified Annotation.
   * @param annotation The Annotation to check the specified Class for.
   * @return The Annotation if and only if it exists otherwise {@link Optional#empty()}.
   */
  public static <T extends Annotation> Optional<T> getAnnotation(Class<?> clazz,
      Class<T> annotation) {
    return Optional.ofNullable(clazz.getAnnotation(annotation));
  }

  /**
   * Tests whether or not the specified {@code clazz} implements the specified {@code superClass}.
   * 
   * @param clazz The Class to test, may not be {@code null}.
   * @param superClass The interface Class, may not be {@code null}.
   * @return {@code true} if the {@code clazz} implements the {@code superClass}, otherwise
   * {@code false}.
   */
  public static boolean hasInterface(Class<?> clazz, Class<?> superClass) {
    Set<Class<?>> interfaces = ImmutableSet.copyOf(clazz.getInterfaces());
    return interfaces.contains(superClass);
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private ClassUtil() {}

}
