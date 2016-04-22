package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.ResetDestinationMessage;

/**
 * MessageCodec for the ResetDestinationMessage.
 */
public final class ResetDestinationMessageCodec implements MessageCodec<ResetDestinationMessage> {

  @Override
  public void encode(ResetDestinationMessage message, FrameBuilder builder) {
    // Frame has no payload, nothing to encode.
  }

  @Override
  public ResetDestinationMessage decode(FrameReader frame) {
    return null;
  }

}
