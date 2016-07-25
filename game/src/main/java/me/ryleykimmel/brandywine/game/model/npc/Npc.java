package me.ryleykimmel.brandywine.game.model.npc;

import me.ryleykimmel.brandywine.game.model.EntityType;
import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.model.World;

/**
 * Represents a non-playable character.
 */
public class Npc extends Mob {

  /**
   * Constructs a new {@link Npc}.
   *
   * @param world The World this Npc is in.
   */
  public Npc(World world) {
    super(world, EntityType.NPC);
  }

}
