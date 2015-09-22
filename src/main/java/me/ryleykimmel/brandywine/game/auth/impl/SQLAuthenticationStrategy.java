package me.ryleykimmel.brandywine.game.auth.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import org.sql2o.data.Row;

import me.ryleykimmel.brandywine.game.auth.AuthenticationResponse;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials;

/**
 * An {@link AuthenticationStrategy} which utilizes jdbc-SQL to validate requests.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class SQLAuthenticationStrategy implements AuthenticationStrategy {

	/**
	 * The maximum amount of characters a single username may be.
	 */
	private static final int MAXIMUM_USERNAME_LENGTH = 12;

	/**
	 * The maximum amount of characters a single password may be.
	 */
	private static final int MAXIMUM_PASSWORD_LENGTH = 20;

	/**
	 * A helper used to establish jdbc connections.
	 */
	private final Sql2o sql2o;

	/**
	 * Constructs a new {@link SQLAuthenticationStrategy} with the specified Sql2o.
	 * 
	 * @param sql2o A helper used to establish jdbc connections.
	 */
	public SQLAuthenticationStrategy(Sql2o sql2o) {
		this.sql2o = sql2o;
	}

	@Override
	public AuthenticationResponse authenticate(Player player) {
		PlayerCredentials credentials = player.getCredentials();

		String username = credentials.getUsername();
		String password = credentials.getPassword();

		if (username.isEmpty() || username.length() > MAXIMUM_USERNAME_LENGTH) {
			return AuthenticationResponse.INVALID_CREDENTIALS;
		}

		if (password.isEmpty() || password.length() > MAXIMUM_PASSWORD_LENGTH) {
			return AuthenticationResponse.INVALID_CREDENTIALS;
		}

		try (Connection connection = sql2o.open()) {
			String remoteAddress = player.getSession().getRemoteAddress().getHostString();

			List<Row> results = connection.createQuery("SELECT * FROM failed_logins WHERE remote_addr = :remote_addr").addParameter("remote_addr", remoteAddress)
					.executeAndFetchTable().rows();

			int count = 0;
			for (Row result : results) {
				count = result.getInteger("count");
				long date = result.getLong("timestamp");

				Instant expire = Instant.ofEpochMilli(date).plus(1, ChronoUnit.MINUTES);
				Instant now = Instant.now();

				if (now.isAfter(expire)) {
					connection.createQuery("DELETE FROM failed_logins WHERE remote_addr = :remote_addr").addParameter("remote_addr", remoteAddress).executeUpdate();
				} else if (count >= 5) {
					return AuthenticationResponse.TOO_MANY_LOGINS;
				}
			}

			results = connection.createQuery("SELECT * FROM players WHERE username = :username").addParameter("username", username).executeAndFetchTable().rows();

			if (results.isEmpty()) {
				// Account does not exist, maybe create response code for 'you must create an
				// account before playing'
				return AuthenticationResponse.SUCCESS;
			}

			for (Row result : results) {
				int id = result.getInteger("id");
				String remotePassword = result.getString("password");

				if (!BCrypt.checkpw(password, remotePassword)) {
					connection
							.createQuery("INSERT INTO failed_logins (count, timestamp, remote_addr) VALUES (:count, :timestamp, :remote_addr) "
									+ "ON DUPLICATE KEY UPDATE count = VALUES(count), timestamp = VALUES(timestamp), remote_addr = VALUES(remote_addr)")
							.addParameter("count", count + 1).addParameter("timestamp", System.currentTimeMillis()).addParameter("remote_addr", remoteAddress).executeUpdate();
					return AuthenticationResponse.INVALID_CREDENTIALS;
				}

				player.setDatabaseId(id);
			}
		} catch (Sql2oException cause) { // SQL server offline
			return AuthenticationResponse.LOGIN_SERVER_OFFLINE;
		}

		return AuthenticationResponse.SUCCESS;
	}

}