package me.ryleykimmel.brandywine.game.model.skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public final class SkillSet {

	private final Map<Integer, Skill> skills = new HashMap<>();
	private final Set<SkillListener> listeners = new HashSet<>();

	private int combatLevel;

	public void init() {
		add(Skill.createCombatSkill(Skill.ATTACK, "Attack"));
		add(Skill.createCombatSkill(Skill.DEFENCE, "Defence"));
		add(Skill.createCombatSkill(Skill.STRENGTH, "Strength"));
		add(Skill.createCombatSkill(Skill.HITPOINTS, "Hitpoints", 10)); // Default level for HP is 10
		add(Skill.createCombatSkill(Skill.RANGED, "Ranged"));
		add(Skill.createCombatSkill(Skill.PRAYER, "Prayer"));
		add(Skill.createCombatSkill(Skill.MAGIC, "Magic"));
	}

	private void add(Skill skill) {
		skills.put(skill.getId(), skill);
	}

	public void execute(Consumer<? super Skill> action) {
		skills.values().forEach(action);
	}

	public void addListener(SkillListener listener) {
		listeners.add(listener);
	}

	public void removeListener(SkillListener listener) {
		listeners.remove(listener);
	}

	public void clearListeners() {
		listeners.clear();
	}

	public Skill get(int id) {
		Preconditions.checkElementIndex(id, skills.size(), "Skill for id: " + id + " does not exist.");
		return skills.get(id);
	}

	public String getName(int id) {
		return get(id).getName();
	}

	public int getLevel(int id) {
		return get(id).getLevel();
	}

	public int getCurrentLevel(int id) {
		return get(id).getCurrentLevel();
	}

	public double getExperience(int id) {
		return get(id).getExperience();
	}

	public void setExperience(int id, double experience) {
		Skill skill = get(id);
		skill.setExperience(experience);
		refresh(skill);
	}

	public void setLevel(int id, int level) {
		Skill skill = get(id);
		skill.setLevel(level);
		refresh(skill);
	}

	public void setCurrentLevel(int id, int currentLevel) {
		Skill skill = get(id);
		skill.setCurrentLevel(currentLevel);
		refresh(skill);
	}

	public void setAll(int level) {
		execute(skill -> setLevel(skill.getId(), level));
	}

	public void reset(int id) {
		setCurrentLevel(id, getLevel(id));
	}

	public void resetAll() {
		execute(skill -> setCurrentLevel(skill.getId(), skill.getLevel()));
	}

	public void refresh(int id) {
		refresh(get(id));
	}

	public void refresh(Skill skill) {
		notifySkillUpdated(skill);
	}

	public void refresh() {
		notifySkillsUpdated();
	}

	public void addExpeirence(int id, double experience) {
		Skill skill = get(id);

		double newExperience = Math.min(skill.getExperience() + experience, Skill.MAXIMUM_EXPERIENCE);

		int newLevel = SkillUtil.levelOf(newExperience);
		int delta = newLevel - skill.getLevel();

		skill.setExperience(newExperience);

		if (delta > 0) {
			notifyLevelledUp(skill);
		}
	}

	private void notifyLevelledUp(Skill skill) {
		listeners.forEach(listener -> listener.levelledUp(this, skill));
	}

	private void notifySkillUpdated(Skill skill) {
		listeners.forEach(listener -> listener.skillUpdated(this, skill));
	}

	private void notifySkillsUpdated() {
		listeners.forEach(listener -> listener.skillsUpdated(this));
	}

	public void calculateCombatLevel() {
		int attack = get(Skill.ATTACK).getLevel();
		int defence = get(Skill.DEFENCE).getLevel();
		int strength = get(Skill.STRENGTH).getLevel();
		int hitpoints = get(Skill.HITPOINTS).getLevel();

		int prayer = get(Skill.PRAYER).getLevel() / 2;
		int ranged = Math.round(get(Skill.RANGED).getLevel() * 1.5F);
		int magic = Math.round(get(Skill.MAGIC).getLevel() * 1.5F);

		float base = Ints.max(strength + attack, magic, ranged) * 1.3F;
		float combatLevel = (base + defence + hitpoints + prayer) / 4;

		this.combatLevel = (int) combatLevel;
	}

	public int getTotalLevel() {
		return skills.values().stream().mapToInt(Skill::getLevel).sum();
	}

	public int getCombatLevel() {
		return combatLevel;
	}

}