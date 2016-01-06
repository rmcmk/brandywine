package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.npc.Npc;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.encode.UpdateBlockEncoderSet;
import me.ryleykimmel.brandywine.game.update.descriptor.encode.DescriptorEncoderSet;

/**
 * The {@link Updater} manages the update sequence which keeps clients synchronized with World.
 * 
 * @author Graham
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class Updater {

  /**
   * The set of {@link UpdateBlockEncoder}s.
   */
  protected final UpdateBlockEncoderSet updateBlockEncoders = new UpdateBlockEncoderSet();

  /**
   * The set of {@link DescriptorEncoder}s.
   */
  protected final DescriptorEncoderSet descriptorEncoders = new DescriptorEncoderSet();

  /**
   * Updates the state of the clients with the state of the server.
   *
   * @param players The {@link MobRepository} containing the {@link Player}s.
   * @param npcs The {@link MobRepository} containing the {@link Npc}s.
   */
  public abstract void update(MobRepository<Player> players, MobRepository<Npc> npcs);

  /**
   * Gets the UpdateBlockEncoder set.
   * 
   * @return The UpdateBlockEncoder set.
   */
  public final UpdateBlockEncoderSet getUpdateBlockEncoders() {
    return updateBlockEncoders;
  }

  /**
   * Gets the DescriptorEncoder set.
   * 
   * @return The DescriptorEncoder set.
   */
  public final DescriptorEncoderSet getDescriptorEncoders() {
    return descriptorEncoders;
  }


}
