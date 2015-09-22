package me.ryleykimmel.brandywine.game.model.skill;

public interface SkillListener {

	default void levelledUp(SkillSet skills, Skill skill) {
		// Method intended to be overridden.
	}

	default void skillUpdated(SkillSet skills, Skill skill) {
		// Method intended to be overridden.
	}

	default void skillsUpdated(SkillSet skills) {
		// Method intended to be overridden.
	}

}