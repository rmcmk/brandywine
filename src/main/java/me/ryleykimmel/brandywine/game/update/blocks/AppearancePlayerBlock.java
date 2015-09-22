package me.ryleykimmel.brandywine.game.update.blocks;

import me.ryleykimmel.brandywine.game.model.player.Appearance;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerBlock;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * Encodes the appearance player UpdateBlock.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AppearancePlayerBlock extends PlayerBlock {

	/**
	 * Constructs a new {@link AppearancePlayerBlock} with the specified Player.
	 * 
	 * @param player The Player we are updating.
	 */
	public AppearancePlayerBlock(Player player) {
		super(player, 0x10);
	}

	@Override
	public void encode(PlayerUpdateMessage message, FrameBuilder builder) {
		FrameBuilder propertiesBuilder = new FrameBuilder(builder.alloc());

		Appearance appearance = mob.getAppearance();

		propertiesBuilder.put(DataType.BYTE, appearance.getGender().getValue());
		propertiesBuilder.put(DataType.BYTE, 0); // Head icon

		propertiesBuilder.put(DataType.BYTE, 0); // hat
		propertiesBuilder.put(DataType.BYTE, 0); // cape
		propertiesBuilder.put(DataType.BYTE, 0); // amulet
		propertiesBuilder.put(DataType.BYTE, 0); // weapon

		// 0x200 + chest id || 0x100 + chest appearance
		propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.CHEST));

		// 0x200 + shield id || 0
		propertiesBuilder.put(DataType.BYTE, 0);

		// 0x200 + chest id if platebody || 0x100 + arms appearance
		propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.ARMS));

		// 0x200 + legs id || 0x100 + legs appearance
		propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.LEGS));

		// if full-helm/full-mask 0 || 0x100 + head appearance
		propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.HAIR));

		// 0x200 + hands id || 0x100 + hands appearance
		propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.HANDS));

		// 0x200 + boots id || 0x100 + feet appearance
		propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.FEET));

		// if full helm OR is female 0 || 0x100 + facial hair appearance
		int value = appearance.isFemale() ? 0 : appearance.getStyle(Appearance.FACIAL_HAIR);
		propertiesBuilder.put(appearance.isFemale() ? DataType.BYTE : DataType.SHORT, value);

		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.HAIR));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.CHEST));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.LEGS));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.FEET));
		propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.SKIN));

		propertiesBuilder.put(DataType.SHORT, 0x328); // stand
		propertiesBuilder.put(DataType.SHORT, 0x337); // stand turn
		propertiesBuilder.put(DataType.SHORT, 0x333); // walk
		propertiesBuilder.put(DataType.SHORT, 0x334); // turn 180
		propertiesBuilder.put(DataType.SHORT, 0x335); // turn 90 cw
		propertiesBuilder.put(DataType.SHORT, 0x336); // turn 90 ccw
		propertiesBuilder.put(DataType.SHORT, 0x338); // run

		propertiesBuilder.put(DataType.LONG, mob.getEncodedUsername());
		propertiesBuilder.put(DataType.BYTE, mob.getSkills().getCombatLevel());
		propertiesBuilder.put(DataType.SHORT, 0);

		builder.put(DataType.BYTE, DataTransformation.NEGATE, propertiesBuilder.getLength());
		builder.putBytes(propertiesBuilder);
	}

}