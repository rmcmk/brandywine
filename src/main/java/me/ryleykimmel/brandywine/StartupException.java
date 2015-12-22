package me.ryleykimmel.brandywine;

import com.google.common.base.Preconditions;

/**
 * Indicates that some error occurred whilst launching the server.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class StartupException extends RuntimeException {
  /** use serialVersionUID from JDK 1.1 for interoperability */
  private static final long serialVersionUID = 4051573209777643883L;

  /**
   * Constructs a new {@link StartupException} with the specified detail message.
   *
   * @param message The detail message, may be {@code null}.
   */
  public StartupException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@link StartupException} with the specified detail message and cause.
   *
   * @param message The detail message, may be {@code null}. @param cause The Exception, may not be
   * {@code null}.
   */
  public StartupException(String message, Exception cause) {
    super(message, Preconditions.checkNotNull(cause));
  }

  /**
   * Constructs a new {@link StartupException} with the specified cause.
   *
   * @param cause The Exception, may not be {@code null}.
   */
  public StartupException(Exception cause) {
    this(cause.getMessage(), cause);
  }

}
