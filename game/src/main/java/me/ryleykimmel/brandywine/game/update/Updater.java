package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.npc.Npc;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * The {@link Updater} manages the update sequence which keeps clients synchronized with World.
 */
@FunctionalInterface
public interface Updater {

  /**
   * Updates the state of the clients with the state of the server.
   *
   * @param players The {@link MobRepository} containing the {@link Player}s.
   * @param npcs The {@link MobRepository} containing the {@link Npc}s.
   */
  void update(MobRepository<Player> players, MobRepository<Npc> npcs);

}
