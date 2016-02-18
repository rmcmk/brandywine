package me.ryleykimmel.brandywine.network.msg.codec;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.game.frame.DataOrder;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.InitializePlayerMessage;

/**
 * Encodes the InitializePlayerMessage.
 */
@Encodes(InitializePlayerMessage.class)
public final class InitializePlayerMessageEncoder
    implements MessageEncoder<InitializePlayerMessage> {

  @Override
  public Frame encode(InitializePlayerMessage message, ByteBufAllocator alloc) {
    FrameBuilder builder = new FrameBuilder(249, alloc);
    builder.put(DataType.BYTE, DataTransformation.ADD, message.isMember() ? 1 : 0);
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getIndex());
    return builder.build();
  }

}
