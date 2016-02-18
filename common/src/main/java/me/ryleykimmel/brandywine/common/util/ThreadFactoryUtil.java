package me.ryleykimmel.brandywine.common.util;

import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.util.internal.StringUtil;

/**
 * A static-utility class containing helper/utility methods for {@link ThreadFactory thread
 * factories}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ThreadFactoryUtil {

  /**
   * Creates a {@link ThreadFactoryBuilder} for the specified Object.
   *
   * @param obj The Object to create a ThreadFactory for.
   * @return The new ThreadFactoryBuilder, never {@code null}.
   */
  public static ThreadFactoryBuilder create(Object obj) {
    return new ThreadFactoryBuilder().setNameFormat(StringUtil.simpleClassName(obj.getClass()));
  }

  /**
   * Creates a {@link ThreadFactoryBuilder} for the specified name.
   *
   * @param name The name format of the thread factory.
   * @return The new ThreadFactoryBuilder, never {@code null}.
   */
  public static ThreadFactoryBuilder create(String name) {
    return new ThreadFactoryBuilder().setNameFormat(name.concat("-%d"));
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private ThreadFactoryUtil() {}

}
