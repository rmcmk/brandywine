package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.ResetDestinationMessage;

/**
 * Encodes the ResetDestinationMessage.
 */
public final class ResetDestinationMessageEncoder
    implements MessageEncoder<ResetDestinationMessage> {

  @Override
  public void encode(ResetDestinationMessage message, FrameBuilder builder) {
    // Frame has no payload, nothing to encode.
  }

}
