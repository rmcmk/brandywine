package me.ryleykimmel.brandywine.game.model.skill;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.msg.impl.UpdateSkillMessage;

public final class SynchronizationSkillListener implements SkillListener {

  private final Player player;

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
