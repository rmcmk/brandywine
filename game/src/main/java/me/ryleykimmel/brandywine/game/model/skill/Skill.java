package me.ryleykimmel.brandywine.game.model.skill;

/**
 * Represents a train(able)-skill.
 */
public final class Skill {

  /**
   * Creates a new Skill which is related to combat.
   * 
   * @param id The id of the Skill.
   * @param name The name of the Skill.
   * @param level The level of the Skill.
   * @return The new Skill, never {@code null}.
   */
  public static Skill createCombatSkill(int id, String name, int level) {
    return new Skill(id, name, true, level);
  }

  /**
   * Creates a new Skill at the default level, which is related to combat
   * 
   * @param id The id of the Skill.
   * @param name The name of the Skill.
   * @return The new Skill, never {@code null}.
   */
  public static Skill createCombatSkill(int id, String name) {
    return createCombatSkill(id, name, DEFAULT_LEVEL);
  }

  /**
   * Creates a new Skill.
   * 
   * @param id The id of the Skill.
   * @param name The name of the Skill.
   * @param level The level of the Skill.
   * @return The new Skill, never {@code null}.
   */
  public static Skill createSkill(int id, String name, int level) {
    return new Skill(id, name, false, level);
  }

  /**
   * Creates a new Skill at the default level.
   * 
   * @param id The id of the Skill.
   * @param name The name of the Skill.
   * @return The new Skill, never {@code null}.
   */
  public static Skill createSkill(int id, String name) {
    return createSkill(id, name, DEFAULT_LEVEL);
  }

  /**
   * The default level of a Skill.
   */
  private static final int DEFAULT_LEVEL = 1;

  /**
   * The maximum level of a Skill.
   */
  public static final int MAXIMUM_LEVEL = 99;

  /**
   * The maximum experience of a Skill.
   */
  public static final int MAXIMUM_EXPERIENCE = 200_000_000;

  /**
   * The attack id.
   */
  public static final int ATTACK = 0;

  /**
   * The defence id.
   */
  public static final int DEFENCE = 1;

  /**
   * The strength id.
   */
  public static final int STRENGTH = 2;

  /**
   * The hitpoints id.
   */
  public static final int HITPOINTS = 3;

  /**
   * The ranged id.
   */
  public static final int RANGED = 4;

  /**
   * The prayer id.
   */
  public static final int PRAYER = 5;

  /**
   * The magic id.
   */
  public static final int MAGIC = 6;

  /**
   * The cooking id.
   */
  public static final int COOKING = 7;

  /**
   * The woodcutting id.
   */
  public static final int WOODCUTTING = 8;

  /**
   * The fletching id.
   */
  public static final int FLETCHING = 9;

  /**
   * The fishing id.
   */
  public static final int FISHING = 10;

  /**
   * The firemaking id.
   */
  public static final int FIREMAKING = 11;

  /**
   * The crafting id.
   */
  public static final int CRAFTING = 12;

  /**
   * The smithing id.
   */
  public static final int SMITHING = 13;

  /**
   * The mining id.rivate
   */
  public static final int MINING = 14;

  /**
   * The herblore id.
   */
  public static final int HERBLORE = 15;

  /**
   * The agility id.
   */
  public static final int AGILITY = 16;

  /**
   * The thieving id.
   */
  public static final int THIEVING = 17;

  /**
   * The slayer id.
   */
  public static final int SLAYER = 18;

  /**
   * The farming id.
   */
  public static final int FARMING = 19;

  /**
   * The runecraft id.
   */
  public static final int RUNECRAFT = 20;

  /**
   * Represents the id of this Skill.
   */
  private final int id;

  /**
   * The name of this Skill.
   */
  private final String name;

  /**
   * Whether or not this Skill relates to combat.
   */
  private final boolean combatSkill;

  /**
   * The current level of this Skill.
   */
  private int currentLevel;

  /**
   * The actual level of this Skill.
   */
  private int level;

  /**
   * The experience of this Skill.
   */
  private double experience;

  /**
   * Constructs a new {@link Skill} with the specified id, name, combat skill flag and level.
   * 
   * @param id The id of this Skill.
   * @param name The name of this Skill.
   * @param combatSkill Whether or not this Skill relates to combat.
   * @param level The actual level of this Skill.
   */
  private Skill(int id, String name, boolean combatSkill, int level) {
    this.id = id;
    this.name = name;
    this.combatSkill = combatSkill;
    this.level = SkillUtil.checkLevel(level);

    experience = SkillUtil.experienceOf(level);
    currentLevel = level; // level was verified above
  }

  /**
   * Gets the id of this Skill.
   * 
   * @return The id of this Skill.
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the name of this Skill.
   * 
   * @return The name of this Skill.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets whether or not this Skill relates to combat.
   * 
   * @return {@code true} if this Skill is a combat skill, otherwise {@code false}.
   */
  public boolean isCombatSkill() {
    return combatSkill;
  }

  /**
   * Gets the current level of this Skill.
   * 
   * @return The current level of this Skill.
   */
  public int getCurrentLevel() {
    return currentLevel;
  }

  /**
   * Gets the level of this Skill.
   * 
   * @return The level of this Skill.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Gets the experience of this Skill.
   * 
   * @return The experience of this Skill.
   */
  public double getExperience() {
    return experience;
  }

  /**
   * Sets this Skills experience.
   * 
   * @param experience The new experience of this Skill.
   */
  public void setExperience(double experience) {
    this.experience = SkillUtil.checkExperience(experience);

    int newLevel = SkillUtil.levelOf(experience);
    int delta = newLevel - level;

    this.level = newLevel;
    this.currentLevel += delta;
  }

  /**
   * Sets this Skills current level.
   * 
   * @param currentLevel The new current level of this Skill.
   */
  public void setCurrentLevel(int currentLevel) {
    this.currentLevel = SkillUtil.checkLevel(currentLevel);
  }

  /**
   * Sets this Skills level.
   * 
   * @param level The new level of this Skill.
   */
  public void setLevel(int level) {
    this.level = SkillUtil.checkLevel(level);
    this.experience = SkillUtil.experienceOf(level);
    this.currentLevel = level;
  }

}
