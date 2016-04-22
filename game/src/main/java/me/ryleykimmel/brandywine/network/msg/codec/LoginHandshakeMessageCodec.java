package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeMessage;

/**
 * MessageCodec for the {@link LoginHandshakeMessage}.
 */
public final class LoginHandshakeMessageCodec implements MessageCodec<LoginHandshakeMessage> {

  @Override
  public LoginHandshakeMessage decode(FrameReader reader) {
    int nameHash = (int) reader.getUnsigned(DataType.BYTE);
    return new LoginHandshakeMessage(nameHash);
  }

  @Override
  public void encode(LoginHandshakeMessage message, FrameBuilder builder) {

  }

}
