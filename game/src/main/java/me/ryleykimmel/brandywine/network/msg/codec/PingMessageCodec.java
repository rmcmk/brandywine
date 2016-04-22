package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.PingMessage;

/**
 * MessageCodec for the {@link PingMessage}.
 */
public final class PingMessageCodec implements MessageCodec<PingMessage> {

  @Override
  public PingMessage decode(FrameReader frame) {
    return new PingMessage();
  }

  @Override
  public void encode(PingMessage message, FrameBuilder builder) {

  }

}
