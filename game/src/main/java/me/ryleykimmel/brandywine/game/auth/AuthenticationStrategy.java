package me.ryleykimmel.brandywine.game.auth;

import java.io.IOException;
import java.sql.SQLException;

import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * The strategy to use when authenticating requests.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@FunctionalInterface
public interface AuthenticationStrategy {

  /**
   * Attempts to authenticate the specified Player.
   *
   * @param player The Player to authenticate.
   * @return The response indicating the state of the authentication.
   * @throws IOException If some I/O error occurs.
   * @throws SQLException If some database access error occurs.
   */
  AuthenticationResponse authenticate(Player player) throws IOException, SQLException;

}
