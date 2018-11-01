package me.ryleykimmel.brandywine.game.model.skill;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.message.ServerChatMessage;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * An implementation of a {@link SkillListener} that listens for leveled up Skill events.
 */
public final class LevelUpSkillListener implements SkillListener {

  /**
   * The Player.
   */
  private final Player player;

  /**
   * Constructs a new {@link LevelUpSkillListener} with the specified Player.
   *
   * @param player The Player.
   */
  public LevelUpSkillListener(Player player) {
    this.player = player;
  }

  @Override
  public void leveledUp(SkillSet skills, Skill skill) {
    // TODO: 'Click here to continue' level up widget

    player.write(
        new ServerChatMessage("You've just advanced %s %s level! You have reached level %s.",
            Strings.getIndefiniteArticle(skill.getName()), skill.getName(),
            skill.getLevel()));

    if (skill.isCombatSkill()) {
      int oldCombatLevel = skills.getCombatLevel();
      int newCombatLevel = skills.calculateCombatLevel();

      int delta = newCombatLevel - oldCombatLevel;
      if (delta > 0) {
        skills.setCombatLevel(newCombatLevel);
        player.write(new ServerChatMessage("Congratulations, your Combat level is now %s.",
            skills.getCombatLevel()));
        player.updateAppearance();
      }
    }
  }

}
