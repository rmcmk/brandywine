package me.ryleykimmel.brandywine.game.update.blocks.encode;

import me.ryleykimmel.brandywine.game.model.player.Appearance;
import me.ryleykimmel.brandywine.game.update.UpdateBlockEncoder;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

public final class AppearancePlayerBlockEncoder
    implements UpdateBlockEncoder<AppearancePlayerBlock> {

  @Override
  public void encode(AppearancePlayerBlock block, FrameBuilder builder) {
    FrameBuilder propertiesBuilder = new FrameBuilder(builder.alloc());

    Appearance appearance = block.getAppearance();

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

    propertiesBuilder.put(DataType.LONG, block.getEncodedUsername());
    propertiesBuilder.put(DataType.BYTE, block.getCombatLevel());
    propertiesBuilder.put(DataType.SHORT, 0);

    builder.put(DataType.BYTE, DataTransformation.NEGATE, propertiesBuilder.getLength());
    builder.putBytes(propertiesBuilder);
  }

}
