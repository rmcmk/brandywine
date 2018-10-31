package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.ResetDestinationMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the ResetDestinationMessage.
 */
public final class ResetDestinationMessageCodec extends MessageCodec<ResetDestinationMessage> {

  @Override
  public void encode(ResetDestinationMessage message, FrameBuilder builder) {
    // Frame has no payload, nothing to encode.
  }

}
