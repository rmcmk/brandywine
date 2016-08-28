package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.CloseInterfaceMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link CloseInterfaceMessage}.
 */
public final class CloseInterfaceMessageCodec extends MessageCodec<CloseInterfaceMessage> {

  @Override
  public void encode(CloseInterfaceMessage message, FrameBuilder builder) {
    // Frame has no payload, nothing to encode.
  }

}
