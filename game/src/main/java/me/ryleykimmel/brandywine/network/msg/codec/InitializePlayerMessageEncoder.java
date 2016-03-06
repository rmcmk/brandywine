package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataOrder;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.InitializePlayerMessage;

/**
 * Encodes the InitializePlayerMessage.
 */
public final class InitializePlayerMessageEncoder
    implements MessageEncoder<InitializePlayerMessage> {

  @Override
  public void encode(InitializePlayerMessage message, FrameBuilder builder) {
    builder.put(DataType.BYTE, DataTransformation.ADD, message.isMember() ? 1 : 0);
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getIndex());
  }

}
