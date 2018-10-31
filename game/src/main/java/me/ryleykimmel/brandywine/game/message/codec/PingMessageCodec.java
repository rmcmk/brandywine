package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.PingMessage;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link PingMessage}.
 */
public final class PingMessageCodec extends MessageCodec<PingMessage> {

  @Override
  public PingMessage decode(FrameReader frame) {
    return new PingMessage();
  }

}
