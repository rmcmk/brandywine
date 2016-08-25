package me.ryleykimmel.brandywine.game.update.blocks;

import me.ryleykimmel.brandywine.game.model.player.Appearance;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.UpdateBlock;
import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * Encodes the appearance player UpdateBlock.
 */
public final class AppearancePlayerBlock extends UpdateBlock {

  /**
   * The mask of this UpdateBlock.
   */
  private static final int MASK = 0x10;

  /**
   * The Player's appearance.
   */
  private final Appearance appearance;

  /**
   * The Player's combat level.
   */
  private final int combatLevel;

  /**
   * The Player's total skill level.
   */
  private final int totalLevel;

  /**
   * The Player's encoded username.
   */
  private final long encodedUsername;

  /**
   * Constructs a new AppearancePlayerBlock.
   *
   * @param appearance The Player's appearance.
   * @param combatLevel The Player's combat level.
   * @param totalLevel The Player's total skill level.
   * @param encodedUsername The Player's encoded username.
   */
  public AppearancePlayerBlock(Appearance appearance, int combatLevel, int totalLevel,
                                long encodedUsername) {
    super(MASK);
    this.appearance = appearance;
    this.combatLevel = combatLevel;
    this.totalLevel = totalLevel;
    this.encodedUsername = encodedUsername;
  }

  /**
   * Creates a new {@link AppearancePlayerBlock} from the specified Player.
   *
   * @param player The Player to create an AppearancePlayerBlock from.
   * @return A new AppearancePlayerBlock, never {@code null}.
   */
  public static AppearancePlayerBlock create(Player player) {
    return new AppearancePlayerBlock(player.getAppearance(), player.getSkills().getCombatLevel(),
        /* player.getSkills().getTotalLevel() */0, player.getEncodedUsername());
  }

  @Override
  public void encode(FrameBuilder builder) {
    FrameBuilder propertiesBuilder = new FrameBuilder(builder.allocator());

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
    if (appearance.isFemale()) {
      propertiesBuilder.put(DataType.BYTE, 0);
    } else {
      propertiesBuilder.put(DataType.SHORT, appearance.getStyle(Appearance.FACIAL_HAIR));
    }

    propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.HAIR));
    propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.CHEST));
    propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.LEGS));
    propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.FEET));
    propertiesBuilder.put(DataType.BYTE, appearance.getColor(Appearance.SKIN));

    propertiesBuilder.put(DataType.SHORT, 0x328); // idle anim
    propertiesBuilder.put(DataType.SHORT, 0x337); // turn anim
    propertiesBuilder.put(DataType.SHORT, 0x333); // walk anim
    propertiesBuilder.put(DataType.SHORT, 0x334); // half turn anim
    propertiesBuilder.put(DataType.SHORT, 0x335); // quarter clockwise turn anim
    propertiesBuilder.put(DataType.SHORT, 0x336); // quarter anti-clockwise turn anim
    propertiesBuilder.put(DataType.SHORT, 0x338); // run anim

    propertiesBuilder.put(DataType.LONG, encodedUsername);
    propertiesBuilder.put(DataType.BYTE, combatLevel);
    propertiesBuilder.put(DataType.SHORT, totalLevel);

    builder.put(DataType.BYTE, DataTransformation.NEGATE, propertiesBuilder.getLength());
    builder.putBytes(propertiesBuilder);
  }

}
