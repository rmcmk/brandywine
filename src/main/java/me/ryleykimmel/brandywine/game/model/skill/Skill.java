package me.ryleykimmel.brandywine.game.model.skill;

import com.google.common.base.Preconditions;

public final class Skill {

	public static Skill createCombatSkill(int id, String name, int level) {
		return new Skill(id, name, true, level);
	}

	public static Skill createCombatSkill(int id, String name) {
		return createCombatSkill(id, name, 1);
	}

	public static Skill createSkill(int id, String name, int level) {
		return new Skill(id, name, false, level);
	}

	public static Skill createSkill(int id, String name) {
		return createSkill(id, name, 1);
	}

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

	public static final int MAXIMUM_LEVEL = 99;
	public static final int MAXIMUM_EXPERIENCE = 200_000_000;

	private final int id;

	private final String name;

	private final boolean combatSkill;

	private int currentLevel;

	private int level;

	private double experience;

	private Skill(int id, String name, boolean combatSkill, int level) {
		this.id = id;
		this.name = name;
		this.combatSkill = combatSkill;
		this.level = level;

		experience = SkillUtil.experienceOf(level);
		currentLevel = level;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isCombatSkill() {
		return combatSkill;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public int getLevel() {
		return level;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		Preconditions.checkArgument(experience >= 0 && experience <= MAXIMUM_EXPERIENCE);
		this.experience = experience;

		int newLevel = SkillUtil.levelOf(experience);
		int delta = newLevel - level;

		this.level = newLevel;
		this.currentLevel += delta;
	}

	public void setCurrentLevel(int currentLevel) {
		Preconditions.checkArgument(currentLevel >= 0 && currentLevel <= MAXIMUM_LEVEL);
		this.currentLevel = currentLevel;
	}

	public void setLevel(int level) {
		Preconditions.checkArgument(level >= 0 && level <= MAXIMUM_LEVEL);
		this.level = level;
		this.experience = SkillUtil.experienceOf(level);
		this.currentLevel = level;
	}

}