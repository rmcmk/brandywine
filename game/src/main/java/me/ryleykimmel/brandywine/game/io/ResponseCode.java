package me.ryleykimmel.brandywine.game.io;

/**
 * Represents an {@code int} code as a response.
 */
public final class ResponseCode {

  /**
   * Delay for 2 seconds login status.
   */
  public static final ResponseCode STATUS_DELAY = new ResponseCode(1);

  /**
   * OK login status.
   */
  public static final ResponseCode STATUS_OK = new ResponseCode(2);

  /**
   * Invalid credentials login status.
   */
  public static final ResponseCode STATUS_INVALID_CREDENTIALS = new ResponseCode(3);

  /**
   * Account disabled login status.
   */
  public static final ResponseCode STATUS_ACCOUNT_DISABLED = new ResponseCode(4);

  /**
   * Account online login status.
   */
  public static final ResponseCode STATUS_ACCOUNT_ONLINE = new ResponseCode(5);

  /**
   * Game updated login status.
   */
  public static final ResponseCode STATUS_GAME_UPDATED = new ResponseCode(6);

  /**
   * Server full login status.
   */
  public static final ResponseCode STATUS_SERVER_FULL = new ResponseCode(7);

  /**
   * Login server offline login status.
   */
  public static final ResponseCode STATUS_LOGIN_SERVER_OFFLINE = new ResponseCode(8);

  /**
   * Too many connections login status.
   */
  public static final ResponseCode STATUS_TOO_MANY_CONNECTIONS = new ResponseCode(9);

  /**
   * Bad session id login status.
   */
  public static final ResponseCode STATUS_BAD_SESSION_ID = new ResponseCode(10);

  /**
   * Login server rejected session login status.
   */
  public static final ResponseCode STATUS_LOGIN_SERVER_REJECTED_SESSION = new ResponseCode(11);

  /**
   * Members account required login status.
   */
  public static final ResponseCode STATUS_MEMBERS_ACCOUNT_REQUIRED = new ResponseCode(12);

  /**
   * Could not complete login status.
   */
  public static final ResponseCode STATUS_COULD_NOT_COMPLETE = new ResponseCode(13);

  /**
   * Server updating login status.
   */
  public static final ResponseCode STATUS_UPDATING = new ResponseCode(14);

  /**
   * Reconnection OK login status.
   */
  public static final ResponseCode STATUS_RECONNECTION_OK = new ResponseCode(15);

  /**
   * Too many login attempts login status.
   */
  public static final ResponseCode STATUS_TOO_MANY_LOGINS = new ResponseCode(16);

  /**
   * Standing in members area on free world status.
   */
  public static final ResponseCode STATUS_IN_MEMBERS_AREA = new ResponseCode(17);

  /**
   * Invalid login server status.
   */
  public static final ResponseCode STATUS_INVALID_LOGIN_SERVER = new ResponseCode(20);

  /**
   * Profile transfer login status.
   */
  public static final ResponseCode STATUS_PROFILE_TRANSFER = new ResponseCode(21);

  /**
   * The response code.
   */
  private final int code;

  /**
   * Constructs a new {@link ResponseCode}.
   * 
   * @param code The response code.
   */
  public ResponseCode(int code) {
    this.code = code;
  }

  /**
   * Gets the response code.
   * 
   * @return The response code.
   */
  public int getCode() {
    return code;
  }

}
