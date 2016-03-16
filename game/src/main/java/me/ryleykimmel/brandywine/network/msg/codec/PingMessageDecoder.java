package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.PingMessage;

/**
 * Decodes the {@link PingMessage}.
 */
public final class PingMessageDecoder implements MessageDecoder<PingMessage> {

  @Override
  public PingMessage decode(FrameReader frame) {
    return new PingMessage();
  }

}
