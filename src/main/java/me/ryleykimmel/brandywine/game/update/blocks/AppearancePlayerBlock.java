package me.ryleykimmel.brandywine.game.update.blocks;

import me.ryleykimmel.brandywine.game.model.player.Appearance;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.UpdateBlock;

/**
 * Encodes the appearance player UpdateBlock.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AppearancePlayerBlock extends UpdateBlock {

	public static AppearancePlayerBlock create(Player player) {
		return new AppearancePlayerBlock(player.getAppearance(), player.getSkills().getCombatLevel(), 
				/* player.getSkills().getTotalLevel() */0, player.getEncodedUsername());
	}

	private static final int MASK = 0x10;

	private final Appearance appearance;
	private final int combatLevel;
	private final int totalLevel;
	private final long encodedUsername;

	public AppearancePlayerBlock(Appearance appearance, int combatLevel, int totalLevel, long encodedUsername) {
		super(MASK);
		this.appearance = appearance;
		this.combatLevel = combatLevel;
		this.totalLevel = totalLevel;
		this.encodedUsername = encodedUsername;
	}
	
	public Appearance getAppearance() {
		return appearance;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public int getTotalLevel() {
		return totalLevel;
	}

	public long getEncodedUsername() {
		return encodedUsername;
	}

}