package me.ryleykimmel.brandywine.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;

public final class ClassUtilTest {

  @Test
  public void testGetAnnotation() {
    Optional<TestAnnotation> trueOptional =
        ClassUtil.getAnnotation(AnnotatedClass.class, TestAnnotation.class);
    assertTrue(trueOptional.isPresent());

    Optional<TestAnnotation> falseOptional =
        ClassUtil.getAnnotation(NotAnnotatedClass.class, TestAnnotation.class);
    assertFalse(falseOptional.isPresent());
  }

  @Test(expected = NullPointerException.class)
  public void failGetAnnotationNullClass() {
    ClassUtil.getAnnotation(null, TestAnnotation.class);
  }

  @Test(expected = NullPointerException.class)
  public void failGetAnnotationNullAnnotation() {
    ClassUtil.getAnnotation(AnnotatedClass.class, null);
  }

  @Test(expected = NoSuchElementException.class)
  public void failGetAnnotationNoArgument() {
    Optional<TestAnnotation> optional =
        ClassUtil.getAnnotation(NotAnnotatedClass.class, TestAnnotation.class);
    optional.get();
  }

  @TestAnnotation
  private static final class AnnotatedClass {
  }

  private static final class NotAnnotatedClass {
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  private static @interface TestAnnotation {
  }

}
