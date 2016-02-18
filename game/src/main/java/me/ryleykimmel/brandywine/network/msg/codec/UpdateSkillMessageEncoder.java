package me.ryleykimmel.brandywine.network.msg.codec;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.game.model.skill.Skill;
import me.ryleykimmel.brandywine.network.game.frame.DataOrder;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.UpdateSkillMessage;

/**
 * Encodes the {@link UpdateSkillMessage}.
 */
@Encodes(UpdateSkillMessage.class)
public final class UpdateSkillMessageEncoder implements MessageEncoder<UpdateSkillMessage> {

  @Override
  public Frame encode(UpdateSkillMessage message, ByteBufAllocator alloc) {
    FrameBuilder builder = new FrameBuilder(134, alloc);
    Skill skill = message.getSkill();
    builder.put(DataType.BYTE, skill.getId());
    builder.put(DataType.INT, DataOrder.MIDDLE, (int) skill.getExperience());
    builder.put(DataType.BYTE, skill.getCurrentLevel());
    return builder.build();
  }

}
