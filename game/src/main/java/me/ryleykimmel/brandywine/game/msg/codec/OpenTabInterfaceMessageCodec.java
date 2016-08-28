package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.model.inter.TabInterface;
import me.ryleykimmel.brandywine.game.msg.OpenTabInterfaceMessage;
import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link OpenTabInterfaceMessage}.
 */
public final class OpenTabInterfaceMessageCodec extends MessageCodec<OpenTabInterfaceMessage> {

  @Override
  public void encode(OpenTabInterfaceMessage message, FrameBuilder builder) {
    TabInterface inter = message.getTabInterface();
    builder.put(DataType.SHORT, inter.getId());
    builder.put(DataType.BYTE, DataTransformation.ADD, inter.getTab().getId());
  }

}
