package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.game.io.ResponseCode;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which responds to a {@link LoginMessage}.
 */
public final class LoginResponseMessage implements Message {

  /**
   * The response code.
   */
  private final ResponseCode response;

  /**
   * The privilege level of the connecting client.
   */
  private final int privilege;

  /**
   * Whether or not the connecting client is flagged.
   */
  private final boolean flagged;

  /**
   * Constructs a new {@link LoginResponseMessage}.
   *
   * @param response The response code.
   * @param privilege The privilege level of the connecting client.
   * @param flagged Whether or not the connecting client is flagged.
   */
  public LoginResponseMessage(ResponseCode response, int privilege, boolean flagged) {
    this.response = response;
    this.privilege = privilege;
    this.flagged = flagged;
  }

  /**
   * Constructs a new {@link LoginResponseMessage}.
   *
   * @param response The response code.
   */
  public LoginResponseMessage(ResponseCode response) {
    this(response, 0, false);
  }

  /**
   * Gets the response code.
   *
   * @return The response code.
   */
  public ResponseCode getResponse() {
    return response;
  }

  /**
   * Gets the privilege level of the connecting client.
   *
   * @return The privilege level of the connecting client.
   */
  public int getPrivilege() {
    return privilege;
  }

  /**
   * Gets whether or not the connecting client is flagged.
   *
   * @return {@code true} if and only if the connecting client is flagged otherwise {@code false}.
   */
  public boolean isFlagged() {
    return flagged;
  }

}
