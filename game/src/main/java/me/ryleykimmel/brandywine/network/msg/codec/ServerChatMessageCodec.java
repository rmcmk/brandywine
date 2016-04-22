package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

/**
 * MessageCodec for the {@link ServerChatMessage}.
 */
public final class ServerChatMessageCodec implements MessageCodec<ServerChatMessage> {

  @Override
  public void encode(ServerChatMessage message, FrameBuilder builder) {
    builder.putString(message.getMessage());
  }

  @Override
  public ServerChatMessage decode(FrameReader frame) {
    return null;
  }

}
