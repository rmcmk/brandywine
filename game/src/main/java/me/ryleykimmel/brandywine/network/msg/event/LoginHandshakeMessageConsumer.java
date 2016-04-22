package me.ryleykimmel.brandywine.network.msg.event;

import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.network.GenericSession;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeMessage;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeResponseMessage;

/**
 * Handles the {@link LoginHandshakeMessage}.
 */
public final class LoginHandshakeMessageConsumer implements EventConsumer<LoginHandshakeMessage> {

  /**
   * The GenericSession which intercepted this MessageReceivedEvent.
   */
  private final GenericSession session;

  /**
   * Constructs a new {@link LoginHandshakeMessageConsumer}.
   * 
   * @param session The GenericSession which intercepted this MessageReceivedEvent.
   */
  public LoginHandshakeMessageConsumer(GenericSession session) {
    this.session = session;
  }

  /**
   * The status which indicates that the server is ready to exchange data with the client.
   */
  private static final int STATUS_EXCHANGE_DATA = 0;

  /**
   * The 8 dummy bytes the client expects to initiate login.
   */
  private static final byte[] EXPECTED_DUMMY = {0, 0, 0, 0, 0, 0, 0, 0};

  @Override
  public void accept(LoginHandshakeMessage event) {
    session.voidWriteAndFlush(new LoginHandshakeResponseMessage(STATUS_EXCHANGE_DATA,
        EXPECTED_DUMMY, session.getSessionKey()));
  }

}
