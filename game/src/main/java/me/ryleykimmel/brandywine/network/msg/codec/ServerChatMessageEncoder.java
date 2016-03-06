package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

/**
 * Encodes the {@link ServerChatMessage}.
 */
public final class ServerChatMessageEncoder implements MessageEncoder<ServerChatMessage> {

  @Override
  public void encode(ServerChatMessage message, FrameBuilder builder) {
    builder.putString(message.getMessage());
  }

}
