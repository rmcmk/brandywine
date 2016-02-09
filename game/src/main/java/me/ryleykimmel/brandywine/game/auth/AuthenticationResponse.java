package me.ryleykimmel.brandywine.game.auth;

import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

/**
 * Represents the response to an AuthenticationRequest.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public enum AuthenticationResponse {

  /**
   * The AuthenticationResponse which indicates success.
   */
  SUCCESS(LoginResponseMessage.STATUS_OK),

  /**
   * The AuthenticationResponse which indicates invalid credentials.
   */
  INVALID_CREDENTIALS(LoginResponseMessage.STATUS_INVALID_CREDENTIALS),

  /**
   * The AuthenticationResponse which indicates that too many login attempts failed.
   */
  TOO_MANY_LOGINS(LoginResponseMessage.STATUS_TOO_MANY_LOGINS),

  /**
   * The AuthenticationResponse which indicates that the login server is offline.
   */
  LOGIN_SERVER_OFFLINE(LoginResponseMessage.STATUS_LOGIN_SERVER_OFFLINE);

  /**
   * The status response code.
   */
  private final int status;

  /**
   * Constructs a new {@link AuthenticationResponse} with the specified status.
   *
   * @param status The status response code.
   */
  private AuthenticationResponse(int status) {
    this.status = status;
  }

  /**
   * Gets the status response code.
   *
   * @return The status response code.
   */
  public int getStatus() {
    return status;
  }

}
