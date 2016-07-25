package me.ryleykimmel.brandywine.game.model.skill;

/**
 * A listener for {@link Skill} events.
 */
public interface SkillListener {

  /**
   * Listens for level up Skill events.
   *
   * @param skills The SkillSet.
   * @param skill The Skill that was leveled up.
   */
  default void leveledUp(SkillSet skills, Skill skill) {
    // Method intended to be overridden.
  }

  /**
   * Listens for update Skill events.
   *
   * @param skills The SkillSet.
   * @param skill The Skill that was updated.
   */
  default void skillUpdated(SkillSet skills, Skill skill) {
    // Method intended to be overridden.
  }

  /**
   * Listens for bulk update Skill events.
   *
   * @param skills The SkillSet.
   */
  default void skillsUpdated(SkillSet skills) {
    // Method intended to be overridden.
  }

}
