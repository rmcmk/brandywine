package me.ryleykimmel.brandywine.game.model.skill;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A container for {@link Skill}s.
 */
public final class SkillSet {

  /**
   * A mapping of skill ids to Skills.
   */
  private final Map<Integer, Skill> skills = new HashMap<>();

  /**
   * A Set of SkillListeners.
   */
  private final Set<SkillListener> listeners = new HashSet<>();

  /**
   * The combat level.
   */
  private int combatLevel;

  /**
   * The total level.
   */
  private int totalLevel;

  /**
   * Initializes Skills.
   */
  public void init() {
    add(Skill.createCombatSkill(Skill.ATTACK, "Attack"));
    add(Skill.createCombatSkill(Skill.DEFENCE, "Defence"));
    add(Skill.createCombatSkill(Skill.STRENGTH, "Strength"));
    add(Skill.createCombatSkill(Skill.HITPOINTS, "Hitpoints", 10)); // Default level for HP is 10
    add(Skill.createCombatSkill(Skill.RANGED, "Ranged"));
    add(Skill.createCombatSkill(Skill.PRAYER, "Prayer"));
    add(Skill.createCombatSkill(Skill.MAGIC, "Magic"));
  }

  /**
   * Adds the specified Skill to the {@link #skills} map.
   *
   * @param skill The Skill to add.
   */
  private void add(Skill skill) {
    skills.put(skill.getId(), skill);
    totalLevel += skill.getLevel();
  }

  /**
   * Executes the specified action over all registered Skills.
   *
   * @param action The action to perform.
   */
  public void execute(Consumer<? super Skill> action) {
    skills.values().forEach(action);
  }

  /**
   * Gets a Skill for the specified id.
   *
   * @param id The id of the Skill.
   * @return The Skill for the specified id.
   */
  public Skill get(int id) {
    return Preconditions.checkNotNull(skills.get(id), "Skill for id: " + id + " does not exist.");
  }

  /**
   * Gets the name of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @return The name of the Skill for the specified id.
   */
  public String getName(int id) {
    return get(id).getName();
  }

  /**
   * Gets the level of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @return The level of the Skill for the specified id.
   */
  public int getLevel(int id) {
    return get(id).getLevel();
  }

  /**
   * Gets the current level of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @return The current level of the Skill for the specified id.
   */
  public int getCurrentLevel(int id) {
    return get(id).getCurrentLevel();
  }

  /**
   * Gets the experience of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @return The experience of the Skill for the specified id.
   */
  public double getExperience(int id) {
    return get(id).getExperience();
  }

  /**
   * Sets the experience of the Skill with the specified id.
   *
   * @param id The id of the Skill
   * @param experience The new experience of the Skill.
   */
  public void setExperience(int id, double experience) {
    Skill skill = get(id);
    int level = skill.getLevel();

    double newExperience = Math.min(experience, Skill.MAXIMUM_EXPERIENCE);
    skill.setExperience(newExperience);

    int delta = skill.getLevel() - level;
    totalLevel += delta;

    refresh(skill);
  }

  /**
   * Adds experience to the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @param experience The amount of experience to add to the Skill.
   */
  public void addExpeirence(int id, double experience) {
    Skill skill = get(id);
    int level = skill.getLevel();

    double newExperience = Math.min(skill.getExperience() + experience, Skill.MAXIMUM_EXPERIENCE);
    skill.setExperience(newExperience);

    int delta = level - skill.getLevel();
    totalLevel += delta;

    if (delta > 0) {
      notifyLeveledUp(skill);
    }
  }

  /**
   * Sets the level of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @param level The new level of the Skill.
   */
  public void setLevel(int id, int level) {
    Skill skill = get(id);
    int oldLevel = skill.getLevel();

    skill.setLevel(level);

    int delta = skill.getLevel() - oldLevel;
    totalLevel += delta;

    refresh(skill);
  }

  /**
   * Sets the current level of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   * @param currentLevel The new current level of the Skill.
   */
  public void setCurrentLevel(int id, int currentLevel) {
    Skill skill = get(id);
    skill.setCurrentLevel(currentLevel);
    refresh(skill);
  }

  /**
   * Sets the level of <strong>every</strong> Skill.
   *
   * @param level The new level of each Skill.
   */
  public void setAll(int level) {
    execute(skill -> setLevel(skill.getId(), level));
  }

  /**
   * Resets the current level of the Skill with the specified id.
   *
   * @param id The id of the Skill.
   */
  public void reset(int id) {
    setCurrentLevel(id, getLevel(id));
  }

  /**
   * Resets <strong>every</strong> Skills current level.
   */
  public void resetAll() {
    execute(skill -> setCurrentLevel(skill.getId(), skill.getLevel()));
  }

  /**
   * Adds the specified SkillListener to this SkillSet.
   *
   * @param listener The SkillListener to add.
   */
  public void addListener(SkillListener listener) {
    listeners.add(listener);
  }

  /**
   * Removes the specified SkillListener from this SkillSet.
   *
   * @param listener The SkillListener to remove.
   */
  public void removeListener(SkillListener listener) {
    listeners.remove(listener);
  }

  /**
   * Clears all SkillListeners from this SkillSet.
   */
  public void clearListeners() {
    listeners.clear();
  }

  /**
   * Refreshes the Skill with the specified id.
   *
   * @param id The id of the Skill.
   */
  public void refresh(int id) {
    refresh(get(id));
  }

  /**
   * Refreshes the specified Skill.
   *
   * @param skill The Skill to refresh.
   */
  public void refresh(Skill skill) {
    notifySkillUpdated(skill);
  }

  /**
   * Refreshes <strong>every</strong> Skill.
   */
  public void refresh() {
    notifySkillsUpdated();
  }

  /**
   * Notifies the SkillListeners that the specified Skill has leveled up.
   *
   * @param skill The Skill which leveled up.
   */
  private void notifyLeveledUp(Skill skill) {
    listeners.forEach(listener -> listener.leveledUp(this, skill));
  }

  /**
   * Notifies the SkillListeners that the specified Skill was updated.
   *
   * @param skill The Skill which was updated.
   */
  private void notifySkillUpdated(Skill skill) {
    listeners.forEach(listener -> listener.skillUpdated(this, skill));
  }

  /**
   * Notifies the SkillListeners that <strong>every</strong> Skill was updated.
   */
  private void notifySkillsUpdated() {
    listeners.forEach(listener -> listener.skillsUpdated(this));
  }

  /**
   * Calculates the combat level of this SkillSet.
   *
   * @return The combat level.
   */
  public int calculateCombatLevel() {
    int attack = get(Skill.ATTACK).getLevel();
    int defence = get(Skill.DEFENCE).getLevel();
    int strength = get(Skill.STRENGTH).getLevel();
    int hitpoints = get(Skill.HITPOINTS).getLevel();

    int prayer = get(Skill.PRAYER).getLevel() / 2;
    int ranged = Math.round(get(Skill.RANGED).getLevel() * 1.5F);
    int magic = Math.round(get(Skill.MAGIC).getLevel() * 1.5F);

    float base = Ints.max(strength + attack, magic, ranged) * 1.3F;
    float combatLevel = (base + defence + hitpoints + prayer) / 4;

    return (int) combatLevel;
  }

  /**
   * Gets the total level of this SkillSet.
   *
   * @return The total level of this SkillSet.
   */
  public int getTotalLevel() {
    return totalLevel;
  }

  /**
   * Gets the combat level of this SkillSet.
   *
   * @return The combat level of this SkillSet.
   */
  public int getCombatLevel() {
    return combatLevel;
  }

  /**
   * Sets the combat level of this SkillSet.
   *
   * @param combatLevel The new combat level of this SkillSet.
   */
  public void setCombatLevel(int combatLevel) {
    this.combatLevel = combatLevel;
  }

}
