package me.ryleykimmel.brandywine.game;

import me.ryleykimmel.brandywine.Service;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Services the game every pulse.
 */
public final class GameService extends Service {

  /**
   * The interval between pulses, in milliseconds.
   */
  private static final long PULSE_INTERVAL = 600L;

  /**
   * The maximum amount of Players registered per pulse.
   */
  private static final int REGISTERS_PER_PULSE = 10;

  /**
   * The maximum amount of unregisters per pulse.
   */
  private static final int UNREGISTERS_PER_PULSE = 10;

  /**
   * A {@link Queue} of Players awaiting registration.
   */
  private final Queue<Player> queuedPlayers = new ConcurrentLinkedQueue<>();

  /**
   * A {@link Queue} of old Players which need removed.
   */
  private final Queue<Player> oldPlayers = new ConcurrentLinkedQueue<>();

  /**
   * The game World.
   */
  private final World world;

  /**
   * Constructs a new {@link GameService}.
   *
   * @param world The game World.
   */
  public GameService(World world) {
    super(PULSE_INTERVAL);
    this.world = world;
  }

  /**
   * Attempts to queue the specified Player for registration, if possible.
   *
   * @param player The Player to queue.
   * @return {@code true} if and only if the Player can be registered otherwise {@code false}.
   */
  public boolean queuePlayer(Player player) {
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
  public void removePlayer(Player player) {
    oldPlayers.add(player);
  }

  /**
   * Finalizes the removal of the specified Player.
   *
   * @param player The Player being removed.
   */
  public void finalizePlayerRemoval(Player player) {
    world.finalizePlayerRemoval(player);
  }

  /**
   * Tests whether or not the specified Player is online or is queued to become online.
   *
   * @param player The Player to test.
   * @return {@code true} if the Player is online or is awaiting login otherwise {@code false}.
   */
  public boolean isPlayerOnline(Player player) {
    return world.isOnline(player) || queuedPlayers.contains(player);
  }

  @Override
  public void execute() {
    for (int count = 0; count < UNREGISTERS_PER_PULSE; count++) {
      Player player = oldPlayers.poll();
      if (player == null) {
        break;
      }

      // TODO: Serialize player, which should in turn call 'finalizePlayerRemoval(Player)'
      // when serialization is complete.
      // For now, we will just remove, forcefully.
      finalizePlayerRemoval(player);
    }

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
