package me.ryleykimmel.brandywine.game.model.skill;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

public final class LevelUpSkillListener implements SkillListener {

	private final Player player;

	public LevelUpSkillListener(Player player) {
		this.player = player;
	}

	@Override
	public void levelledUp(SkillSet skills, Skill skill) {
		// TODO: 'Click here to continue' level up widget

		player.write(new ServerChatMessage("You've just advanced %s %s level! You have reached level %s.", Strings.getIndefiniteArticle(skill.getName()), skill.getName(),
				skill.getLevel()));

		if (skill.isCombatSkill()) {
			int oldCombatLevel = skills.getCombatLevel();
			skills.calculateCombatLevel();

			int delta = skills.getCombatLevel() - oldCombatLevel;
			if (delta > 0) {
				player.write(new ServerChatMessage("Congratulations, your Combat level is now %s.", skills.getCombatLevel()));
			}
		}
	}

}