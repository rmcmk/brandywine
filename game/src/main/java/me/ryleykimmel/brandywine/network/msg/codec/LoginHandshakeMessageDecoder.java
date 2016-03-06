package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeMessage;

/**
 * Decodes the {@link LoginHandshakeMessage}
 */
public final class LoginHandshakeMessageDecoder implements MessageDecoder<LoginHandshakeMessage> {

  @Override
  public LoginHandshakeMessage decode(GameSession session, FrameReader reader) {
    int nameHash = (int) reader.getUnsigned(DataType.BYTE);
    return new LoginHandshakeMessage(nameHash);
  }

}
