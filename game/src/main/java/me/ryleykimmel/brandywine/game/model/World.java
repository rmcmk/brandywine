package me.ryleykimmel.brandywine.game.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import me.ryleykimmel.brandywine.game.area.Region;
import me.ryleykimmel.brandywine.game.area.RegionRepository;
import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.npc.Npc;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.ParallelUpdater;
import me.ryleykimmel.brandywine.game.update.Updater;

/**
 * A representation of the in-game World, containing repositories of {@link Mob mobs} and other
 * things only relevant to the World.
 */
public final class World {

  /**
   * The maximum amount of Players that may occupy any given World.
   */
  public static final int MAXIMUM_PLAYERS = 2_048;

  /**
   * The maximum amount of Npcs that may occupy any given World.
   */
  public static final int MAXIMUM_NPCS = 32_768;

  /**
   * A {@link MobRepository} of registered Players.
   */
  private final MobRepository<Player> playerRepository = new MobRepository<>(MAXIMUM_PLAYERS);

  /**
   * A {@link MobRepository} of registered Npcs.
   */
  private final MobRepository<Npc> npcRepository = new MobRepository<>(MAXIMUM_NPCS);

  /**
   * A {@link Map} of encoded Player usernames to Players.
   */
  private final Map<Long, Player> players = new HashMap<>();

  /**
   * The {@link Updater} for this World.
   */
  private final Updater updater = new ParallelUpdater();

  /**
   * The {@link RegionRepository} for this World.
   */
  private final RegionRepository regionRepository = new RegionRepository();

  /**
   * Attempts to add the specified Player to the World.
   *
   * @param player The Player to add.
   * @return {@code true} if and only if the specified Player was added otherwise {@code false}.
   */
  public boolean addPlayer(Player player) {
    if (playerRepository.add(player)) {
      players.put(player.getEncodedUsername(), player);

      Region region = regionRepository.fromPosition(player.getPosition());
      region.addEntity(player);

      return true;
    }

    return false;
  }

  /**
   * Finalizes the removal of the specified Player.
   * 
   * @param player The Player to remove.
   */
  public void finalizePlayerRemoval(Player player) {
    players.remove(player.getEncodedUsername());

    Region region = regionRepository.fromPosition(player.getPosition());
    region.removeEntity(player);

    playerRepository.remove(player);
  }

  /**
   * Checks whether or not the specified Player is online.
   *
   * @param player The Player to check.
   * @return {@code true} if and only if the Player is online otherwise {@code false}.
   */
  public boolean isOnline(Player player) {
    return isOnline(player.getEncodedUsername());
  }

  /**
   * Checks whether or not the specified encoded username is online.
   *
   * @param username The username to check.
   * @return {@code true} if and only if the username is online otherwise {@code false}.
   */
  public boolean isOnline(long username) {
    return get(username).isPresent();
  }

  /**
   * Attempts to get a Player, wrapped in an Optional, for the specified encoded username.
   *
   * @param username The encoded username of the Player.
   * @return The Player, wrapped in an Optional.
   */
  public Optional<Player> get(long username) {
    Player player = players.get(username);
    return Optional.ofNullable(player);
  }

  /**
   * Pulses the World.
   */
  public void pulse() {
    updater.update(playerRepository, npcRepository);
  }

  /**
   * Gets the total amount of Players online.
   *
   * @return The total amount of Players online.
   */
  public int getPlayerCount() {
    return playerRepository.size();
  }

  /**
   * Gets the total amount of Npcs online.
   *
   * @return The total amount of Npcs online.
   */
  public int getNpcCount() {
    return npcRepository.size();
  }

  /**
   * Gets all of the registered Players within this World.
   *
   * @return All of the registered Players within this World.
   */
  public MobRepository<Player> getPlayers() {
    return playerRepository;
  }

  /**
   * Gets all of the registered Npcs within this World.
   *
   * @return All of the registered Npcs within this World.
   */
  public MobRepository<Npc> getNpcs() {
    return npcRepository;
  }

  /**
   * Gets this World's RegionRepository.
   * 
   * @return The RegionRepository for this World.
   */
  public RegionRepository getRegionRepository() {
    return regionRepository;
  }

}
