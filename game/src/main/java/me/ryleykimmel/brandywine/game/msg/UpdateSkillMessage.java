package me.ryleykimmel.brandywine.game.msg;

import me.ryleykimmel.brandywine.game.model.skill.Skill;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which updates the specified Skill.
 */
public final class UpdateSkillMessage extends Message {

  /**
   * The Skill to be updated.
   */
  private final Skill skill;

  /**
   * Constructs a new {@link UpdateSkillMessage} with the specified Skill.
   *
   * @param skill The Skill to be updated.
   */
  public UpdateSkillMessage(Skill skill) {
    this.skill = skill;
  }

  /**
   * Gets the Skill to be updated.
   *
   * @return The Skill to be updated.
   */
  public Skill getSkill() {
    return skill;
  }

}
