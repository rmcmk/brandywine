package me.ryleykimmel.brandywine.network.msg.codec;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

/**
 * Encodes the {@link LoginResponseMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Encodes(LoginResponseMessage.class)
public final class LoginResponseMessageEncoder implements MessageEncoder<LoginResponseMessage> {

  @Override
  public Frame encode(LoginResponseMessage message, ByteBufAllocator alloc) {
    FrameBuilder builder = new FrameBuilder(alloc);
    builder.put(DataType.BYTE, message.getStatus());
    if (message.getStatus() == LoginResponseMessage.STATUS_OK) {
      builder.put(DataType.BYTE, message.getPrivilege());
      builder.put(DataType.BYTE, message.isFlagged() ? 1 : 0);
    }
    return builder.build();
  }

}
