package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.game.model.skill.Skill;
import me.ryleykimmel.brandywine.network.game.frame.DataOrder;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.UpdateSkillMessage;

/**
 * Encodes the {@link UpdateSkillMessage}.
 */
public final class UpdateSkillMessageEncoder implements MessageEncoder<UpdateSkillMessage> {

  @Override
  public void encode(UpdateSkillMessage message, FrameBuilder builder) {
    Skill skill = message.getSkill();
    builder.put(DataType.BYTE, skill.getId());
    builder.put(DataType.INT, DataOrder.MIDDLE, (int) skill.getExperience());
    builder.put(DataType.BYTE, skill.getCurrentLevel());
  }

}
