package me.ryleykimmel.brandywine.game.auth;

import com.google.common.base.MoreObjects;

import me.ryleykimmel.brandywine.game.GameSession;
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials;

/**
 * Represents an authentication request.
 */
public final class AuthenticationRequest {

  /**
   * The GameSession who is making the request.
   */
  private final GameSession session;

  /**
   * The PlayerCredentials that are requesting authentication.
   */
  private final PlayerCredentials credentials;

  /**
   * Constructs a new {@link AuthenticationRequest} with the specified LoginSession and
   * PlayerCredentials.
   *
   * @param session The GameSession who is making the request.
   * @param credentials The PlayerCredentials that are requesting authentication.
   */
  public AuthenticationRequest(GameSession session, PlayerCredentials credentials) {
    this.session = session;
    this.credentials = credentials;
  }

  /**
   * Gets the GameSession who is making the request.
   *
   * @return The GameSession who is making the request.
   */
  public GameSession getSession() {
    return session;
  }

  /**
   * Gets the PlayerCredentials that are requesting authentication.
   *
   * @return The PlayerCredentials that are requesting authentication.
   */
  public PlayerCredentials getCredentials() {
    return credentials;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("session", session).add("credentials", credentials)
        .toString();
  }

}
