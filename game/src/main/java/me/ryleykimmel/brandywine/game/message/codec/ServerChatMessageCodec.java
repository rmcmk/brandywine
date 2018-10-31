package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.ServerChatMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link ServerChatMessage}.
 */
public final class ServerChatMessageCodec extends MessageCodec<ServerChatMessage> {

  @Override
  public void encode(ServerChatMessage message, FrameBuilder builder) {
    builder.putString(message.getMessage());
  }

}
