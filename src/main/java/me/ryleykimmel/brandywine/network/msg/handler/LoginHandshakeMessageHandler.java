package me.ryleykimmel.brandywine.network.msg.handler;

import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Handles;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeMessage;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeResponseMessage;

/**
 * Handles the {@link LoginHandshakeMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Handles(LoginHandshakeMessage.class)
public final class LoginHandshakeMessageHandler implements MessageHandler<LoginHandshakeMessage> {

  /**
   * The status which indicates that the server is ready to exchange data with the client.
   */
  private static final int STATUS_EXCHANGE_DATA = 0;

  /**
   * The 8 dummy bytes the client expects to initiate login.
   */
  private static final byte[] EXPECTED_DUMMY = {0, 0, 0, 0, 0, 0, 0, 0};

  @Override
  public void handle(GameSession session, LoginHandshakeMessage message) {
    session.voidWrite(new LoginHandshakeResponseMessage(STATUS_EXCHANGE_DATA, EXPECTED_DUMMY,
        session.getSessionKey()));
  }

}
