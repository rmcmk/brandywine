package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.LoginHandshakeMessage;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

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