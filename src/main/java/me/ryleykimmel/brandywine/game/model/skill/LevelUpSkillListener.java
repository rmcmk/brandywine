package me.ryleykimmel.brandywine.game.model.skill;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

/**
 * An implementation of a {@link SkillListener} that listens for leveled up Skill events.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
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

    player
        .write(new ServerChatMessage("You've just advanced %s %s level! You have reached level %s.",
            Strings.getIndefiniteArticle(skill.getName()), skill.getName(), skill.getLevel()));

    if (skill.isCombatSkill()) {
      int oldCombatLevel = skills.getCombatLevel();
      skills.calculateCombatLevel();

      int delta = skills.getCombatLevel() - oldCombatLevel;
      if (delta > 0) {
        player.write(new ServerChatMessage("Congratulations, your Combat level is now %s.",
            skills.getCombatLevel()));
        player.flagUpdate(AppearancePlayerBlock.create(player));
      }
    }
  }

}
