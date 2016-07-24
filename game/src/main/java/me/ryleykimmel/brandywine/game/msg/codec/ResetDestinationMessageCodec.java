package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.ResetDestinationMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the ResetDestinationMessage.
 */
public final class ResetDestinationMessageCodec extends MessageCodec<ResetDestinationMessage> {

  @Override
  public void encode(ResetDestinationMessage message, FrameBuilder builder) {
    // Frame has no payload, nothing to encode.
  }

}
