package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which responds to a {@link LoginMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class LoginResponseMessage implements Message {

  /**
   * Delay for 2 seconds login status.
   */
  public static final int STATUS_DELAY = 1;

  /**
   * OK login status.
   */
  public static final int STATUS_OK = 2;

  /**
   * Invalid credentials login status.
   */
  public static final int STATUS_INVALID_CREDENTIALS = 3;

  /**
   * Account disabled login status.
   */
  public static final int STATUS_ACCOUNT_DISABLED = 4;

  /**
   * Account online login status.
   */
  public static final int STATUS_ACCOUNT_ONLINE = 5;

  /**
   * Game updated login status.
   */
  public static final int STATUS_GAME_UPDATED = 6;

  /**
   * Server full login status.
   */
  public static final int STATUS_SERVER_FULL = 7;

  /**
   * Login server offline login status.
   */
  public static final int STATUS_LOGIN_SERVER_OFFLINE = 8;

  /**
   * Too many connections login status.
   */
  public static final int STATUS_TOO_MANY_CONNECTIONS = 9;

  /**
   * Bad session id login status.
   */
  public static final int STATUS_BAD_SESSION_ID = 10;

  /**
   * Login server rejected session login status.
   */
  public static final int STATUS_LOGIN_SERVER_REJECTED_SESSION = 11;

  /**
   * Members account required login status.
   */
  public static final int STATUS_MEMBERS_ACCOUNT_REQUIRED = 12;

  /**
   * Could not complete login status.
   */
  public static final int STATUS_COULD_NOT_COMPLETE = 13;

  /**
   * Server updating login status.
   */
  public static final int STATUS_UPDATING = 14;

  /**
   * Reconnection OK login status.
   */
  public static final int STATUS_RECONNECTION_OK = 15;

  /**
   * Too many login attempts login status.
   */
  public static final int STATUS_TOO_MANY_LOGINS = 16;

  /**
   * Standing in members area on free world status.
   */
  public static final int STATUS_IN_MEMBERS_AREA = 17;

  /**
   * Invalid login server status.
   */
  public static final int STATUS_INVALID_LOGIN_SERVER = 20;

  /**
   * Profile transfer login status.
   */
  public static final int STATUS_PROFILE_TRANSFER = 21;

  /**
   * The status of the response.
   */
  private final int status;

  /**
   * The privilege level of the connecting client.
   */
  private final int privilege;

  /**
   * Whether or not the connecting client is flagged.
   */
  private final boolean flagged;

  /**
   * Constructs a new {@link LoginResponseMessage} with the specified status, privilege level and
   * flagged indication.
   *
   * @param status The status of the response.
   * @param privilege The privilege level of the connecting client.
   * @param flagged Whether or not the connecting client is flagged.
   */
  public LoginResponseMessage(int status, int privilege, boolean flagged) {
    this.status = status;
    this.privilege = privilege;
    this.flagged = flagged;
  }

  /**
   * Constructs a new {@link LoginResponseMessage} with the specified status.
   *
   * @param status The status of the response.
   */
  public LoginResponseMessage(int status) {
    this(status, 0, false);
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
