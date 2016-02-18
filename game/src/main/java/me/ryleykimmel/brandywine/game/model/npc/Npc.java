package me.ryleykimmel.brandywine.game.model.npc;

import me.ryleykimmel.brandywine.ServerContext;
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
   * @param context The context of the Server.
   */
  public Npc(World world, ServerContext context) {
    super(world, EntityType.NPC, context);
  }

}
