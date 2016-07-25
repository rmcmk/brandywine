package me.ryleykimmel.brandywine.game.model.player;

import com.google.common.base.MoreObjects;
import me.ryleykimmel.brandywine.common.util.NameUtil;

import java.util.Arrays;

/**
 * Represents the credentials of a Player.
 */
public final class PlayerCredentials {

  /**
   * The user id.
   */
  private final int userId;

  /**
   * The username.
   */
  private final String username;

  /**
   * The password.
   */
  private final String password;

  /**
   * The encoded username
   */
  private final long encodedUsername;

  /**
   * The session keys.
   */
  private final int[] sessionKeys;

  /**
   * Constructs a new {@link PlayerCredentials} with the specified user id, username, password and
   * session keys.
   *
   * @param userId The user id.
   * @param username The username.
   * @param password The password.
   * @param sessionKeys The session keys.
   */
  public PlayerCredentials(int userId, String username, String password, int[] sessionKeys) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.sessionKeys = sessionKeys;
    this.encodedUsername = NameUtil.encodeBase37(username);
  }

  /**
   * Gets the user id.
   *
   * @return The user id.
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Gets the username.
   *
   * @return The username.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Gets the password.
   *
   * @return The password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets the encoded username.
   *
   * @return The encoded username.
   */
  public long getEncodedUsername() {
    return encodedUsername;
  }

  /**
   * Gets the session keys.
   *
   * @return The session keys.
   */
  public int[] getSessionKeys() {
    return sessionKeys;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("userId", userId).add("username", username)
             .add("password", password).add("encodedUsername", encodedUsername)
             .add("sessionKeys", Arrays.toString(sessionKeys)).toString();
  }

}
