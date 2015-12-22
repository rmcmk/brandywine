package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.Decodes;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeMessage;

/**
 * Decodes the {@link LoginHandshakeMessage}
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Decodes(14)
public final class LoginHandshakeMessageDecoder implements MessageDecoder<LoginHandshakeMessage> {

  @Override
  public LoginHandshakeMessage decode(Frame frame) {
    FrameReader reader = new FrameReader(frame);
    int nameHash = (int) reader.getUnsigned(DataType.BYTE);
    return new LoginHandshakeMessage(nameHash);
  }

}
