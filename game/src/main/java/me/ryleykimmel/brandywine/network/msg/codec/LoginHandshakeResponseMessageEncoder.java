package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeResponseMessage;

/**
 * Encodes the {@link LoginHandshakeResponseMessage}.
 */
public final class LoginHandshakeResponseMessageEncoder
    implements MessageEncoder<LoginHandshakeResponseMessage> {

  @Override
  public void encode(LoginHandshakeResponseMessage message, FrameBuilder builder) {
    builder.put(DataType.BYTE, message.getStatus());
    builder.putBytes(message.getDummy());
    builder.put(DataType.LONG, message.getSessionKey());
  }

}
