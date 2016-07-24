package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.model.skill.Skill;
import me.ryleykimmel.brandywine.game.msg.UpdateSkillMessage;
import me.ryleykimmel.brandywine.network.frame.DataOrder;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link UpdateSkillMessage}.
 */
public final class UpdateSkillMessageCodec extends MessageCodec<UpdateSkillMessage> {

  @Override
  public void encode(UpdateSkillMessage message, FrameBuilder builder) {
    Skill skill = message.getSkill();
    builder.put(DataType.BYTE, skill.getId());
    builder.put(DataType.INT, DataOrder.MIDDLE, (int) skill.getExperience());
    builder.put(DataType.BYTE, skill.getCurrentLevel());
  }

}
