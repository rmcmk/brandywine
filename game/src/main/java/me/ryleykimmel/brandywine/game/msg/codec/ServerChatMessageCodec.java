package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.ServerChatMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link ServerChatMessage}.
 */
public final class ServerChatMessageCodec extends MessageCodec<ServerChatMessage> {

  @Override
  public void encode(ServerChatMessage message, FrameBuilder builder) {
    builder.putString(message.getMessage());
  }

}
