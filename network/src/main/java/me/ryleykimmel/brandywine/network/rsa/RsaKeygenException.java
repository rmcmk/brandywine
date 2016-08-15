package me.ryleykimmel.brandywine.network.rsa;

/**
 * An exception which is thrown when we are unable create or read RSA keys.
 */
public final class RsaKeygenException extends Exception {

  /**
   * Constructs a new RsaKeygenException with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param message The detail message. The detail message is saved for
   * later retrieval by the {@link #getMessage()} method.
   */
  public RsaKeygenException(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message and
   * cause.  <p>Note that the detail message associated with
   * {@code cause} is <i>not</i> automatically incorporated in
   * this exception's detail message.
   *
   * @param message The detail message (which is saved for later retrieval
   * by the {@link #getMessage()} method).
   * @param cause The cause (which is saved for later retrieval by the
   * {@link #getCause()} method).  (A <tt>null</tt> value is
   * permitted, and indicates that the cause is nonexistent or
   * unknown.)
   */
  public RsaKeygenException(String message, Throwable cause) {
    super(message, cause);
  }

}
