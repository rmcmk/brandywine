package me.ryleykimmel.brandywine.network.msg.codec;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.game.frame.FrameType;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

/**
 * Encodes the {@link ServerChatMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Encodes(ServerChatMessage.class)
public class ServerChatMessageEncoder implements MessageEncoder<ServerChatMessage> {

  @Override
  public Frame encode(ServerChatMessage message, ByteBufAllocator alloc) {
    FrameBuilder builder = new FrameBuilder(253, FrameType.VARIABLE_BYTE, alloc);
    builder.putString(message.getMessage());
    return builder.build();
  }

}
