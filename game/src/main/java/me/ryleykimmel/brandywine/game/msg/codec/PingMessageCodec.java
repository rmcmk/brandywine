package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.PingMessage;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link PingMessage}.
 */
public final class PingMessageCodec extends MessageCodec<PingMessage> {

  @Override
  public PingMessage decode(FrameReader frame) {
    return new PingMessage();
  }

}
