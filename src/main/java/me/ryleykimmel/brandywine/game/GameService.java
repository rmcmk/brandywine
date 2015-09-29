package me.ryleykimmel.brandywine.game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.Service;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Services the game every pulse.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class GameService extends Service {

	/**
	 * The interval between pulses, in milliseconds.
	 */
	private static final long PULSE_INTERVAL = 600L;

	/**
	 * The maximum amount of Players registered per pulse.
	 */
	private static final int REGISTERS_PER_PULSE = 50;

	/**
	 * A {@link Queue} of Players awaiting registration.
	 */
	private final Queue<Player> queuedPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The game World.
	 */
	private final World world = new World();

	/**
	 * Constructs a new {@link GameService} with the specified ServerContext.
	 *
	 * @param context The context of the server.
	 */
	public GameService(ServerContext context) {
		super(context, PULSE_INTERVAL);
	}

	/**
	 * Attempts to queue the specified Player for registration, if possible.
	 *
	 * @param player The Player to queue.
	 * @return {@code true} if and only if the Player can be registered otherwise {@code false}.
	 */
	public synchronized boolean queuePlayer(Player player) {
		if (queuedPlayers.size() + world.getPlayerCount() > World.MAXIMUM_PLAYERS) {
			return false;
		}

		return queuedPlayers.offer(player);
	}

	/**
	 * Removes the specified Player from the World.
	 * 
	 * @param player The Player to remove.
	 */
	public synchronized void removePlayer(Player player) {
		world.removePlayer(player);
	}

	/**
	 * Tests whether or not the specified Player is online or is queued to become online.
	 * 
	 * @param player The Player to test.
	 * @return {@code true} if the Player is online or is awaiting login otherwise {@code false}.
	 */
	public synchronized boolean isPlayerOnline(Player player) {
		return world.isOnline(player.getEncodedUsername()) || queuedPlayers.contains(player);
	}

	@Override
	public synchronized void execute() {
		for (int count = 0; count < REGISTERS_PER_PULSE; count++) {
			Player player = queuedPlayers.poll();
			if (player == null) {
				break;
			}

			// Forward to IO service -- everything is verified and ready to go!
			if (!world.addPlayer(player)) {
				player.disconnect();
				continue;
			}

			player.login();
		}

		// Pulse the world
		world.pulse();
	}

	/**
	 * Gets the game World.
	 *
	 * @return The game World.
	 */
	public World getWorld() {
		return world;
	}

}