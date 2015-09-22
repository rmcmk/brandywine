package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.game.model.skill.Skill;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which updates the specified Skill.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class UpdateSkillMessage implements Message {

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