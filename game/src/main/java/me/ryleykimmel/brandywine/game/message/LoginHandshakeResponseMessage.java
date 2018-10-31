package me.ryleykimmel.brandywine.game.message;

import me.ryleykimmel.brandywine.network.message.Message;

/**
 * A {@link Message} which responds to a {@link LoginHandshakeMessage}.
 */
public final class LoginHandshakeResponseMessage extends Message {

  /**
   * The status of the response.
   */
  private final int status;

  /**
   * The client expects 8 dummy bytes when initiating login.
   */
  private final byte[] dummy;

  /**
   * The session key.
   */
  private final long sessionKey;

  /**
   * Constructs a new {@link LoginHandshakeResponseMessage} with the specified status, dummy and
   * session key.
   *
   * @param status The status of the response.
   * @param dummy The dummy bytes.
   * @param sessionKey The session key.
   */
  public LoginHandshakeResponseMessage(int status, byte[] dummy, long sessionKey) {
    this.status = status;
    this.dummy = dummy;
    this.sessionKey = sessionKey;
  }

  /**
   * Gets the status of the response.
   *
   * @return The status of the response.
   */
  public int getStatus() {
    return status;
  }

  /**
   * Gets the expected dummy bytes.
   *
   * @return The dummy bytes.
   */
  public byte[] getDummy() {
    return dummy;
  }

  /**
   * Gets the session key.
   *
   * @return The session key.
   */
  public long getSessionKey() {
    return sessionKey;
  }

}
