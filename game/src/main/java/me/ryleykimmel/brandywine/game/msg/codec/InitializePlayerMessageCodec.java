package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.InitializePlayerMessage;
import me.ryleykimmel.brandywine.network.frame.DataOrder;
import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link InitializePlayerMessage}.
 */
public final class InitializePlayerMessageCodec implements MessageCodec<InitializePlayerMessage> {

  @Override
  public void encode(InitializePlayerMessage message, FrameBuilder builder) {
    builder.put(DataType.BYTE, DataTransformation.ADD, message.isMember() ? 1 : 0);
    builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getIndex());
  }

  @Override
  public InitializePlayerMessage decode(FrameReader frame) {
    return null;
  }

}
