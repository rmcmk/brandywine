package me.ryleykimmel.brandywine.game.message.listener;

import me.ryleykimmel.brandywine.game.message.LoginHandshakeMessage;
import me.ryleykimmel.brandywine.game.message.LoginHandshakeResponseMessage;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.message.MessageListener;

/**
 * Listener for the {@link LoginHandshakeMessage}.
 */
public final class LoginHandshakeMessageListener implements MessageListener<LoginHandshakeMessage> {

  /**
   * The status sent upon successful handshake.
   */
  private static final int STATUS_EXCHANGE_DATA = 0;

  /**
   * The client expects a dummy byte array of 8 bytes followed by the session id.
   */
  private static final byte[] EXPECTED_DUMMY = {0, 0, 0, 0, 0, 0, 0, 0};

  @Override
  public void handle(Session session, LoginHandshakeMessage message) {
    session.voidWriteAndFlush(
        new LoginHandshakeResponseMessage(STATUS_EXCHANGE_DATA, EXPECTED_DUMMY, session.getId()));
  }

}