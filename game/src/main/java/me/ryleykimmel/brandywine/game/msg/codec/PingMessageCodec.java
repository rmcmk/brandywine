package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.PingMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

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
