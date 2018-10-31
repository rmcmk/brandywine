package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.CloseInterfaceMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link CloseInterfaceMessage}.
 */
public final class CloseInterfaceMessageCodec extends MessageCodec<CloseInterfaceMessage> {

  @Override
  public void encode(CloseInterfaceMessage message, FrameBuilder builder) {
    // Frame has no payload, nothing to encode.
  }

}
