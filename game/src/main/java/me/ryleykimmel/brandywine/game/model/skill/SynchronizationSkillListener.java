package me.ryleykimmel.brandywine.game.model.skill;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.message.UpdateSkillMessage;

/**
 * An implementation of a {@link SkillListener} which keeps the server and client state of Skills
 * synchronized.
 */
public final class SynchronizationSkillListener implements SkillListener {

  /**
   * The Player.
   */
  private final Player player;

  /**
   * Constructs a new {@link SynchronizationSkillListener} with the specified Player.
   *
   * @param player The Player.
   */
  public SynchronizationSkillListener(Player player) {
    this.player = player;
  }

  @Override
  public void skillsUpdated(SkillSet skills) {
    skills.execute(skill -> skillUpdated(skills, skill));
  }

  @Override
  public void skillUpdated(SkillSet skills, Skill skill) {
    player.write(new UpdateSkillMessage(skill));
  }

}
