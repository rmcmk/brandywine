package me.ryleykimmel.brandywine.game.message.listener;

import io.netty.channel.ChannelFutureListener;
import me.ryleykimmel.brandywine.game.auth.AuthenticationRequest;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.message.LoginMessage;
import me.ryleykimmel.brandywine.game.message.LoginResponseMessage;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials;
import me.ryleykimmel.brandywine.network.ResponseCode;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.message.MessageListener;

/**
 * Listener for the {@link LoginMessage}.
 */
public final class LoginMessageListener implements MessageListener<LoginMessage> {

  /**
   * The expected client version received.
   */
  private static final int CLIENT_VERSION = 317;

  /**
   * The expected value of a dummy byte sent at the start of the login protocol.
   */
  private static final int EXPECTED_DUMMY = 255;

  /**
   * The expected opcode which indicates successful our secure RSA buffer is using the correct key pair.
   */
  private static final int EXPECTED_BLOCK_OPCODE = 10;

  /**
   * The World for submitting authentication requests.
   */
  private final World world;

  /**
   * Constructs a new {@link LoginMessageListener}.
   *
   * @param world The World for submitting authentication requests.
   */
  public LoginMessageListener(World world) {
    this.world = world;
  }

  @Override
  public void handle(Session session, LoginMessage message) {
    if (message.getDummy() != EXPECTED_DUMMY) {
      closeWithResponse(session, ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    if (message.getClientVersion() != CLIENT_VERSION) {
      closeWithResponse(session, ResponseCode.STATUS_GAME_UPDATED);
      return;
    }

    if (message.getDetail() != 0 && message.getDetail() != 1) {
      closeWithResponse(session, ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    if (message.getBlockOperationCode() != EXPECTED_BLOCK_OPCODE) {
      closeWithResponse(session, ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    long serverSessionId = message.getServerSessionId();
    long clientSessionId = message.getClientSessionId();

    if (serverSessionId != session.getId()) {
      closeWithResponse(session, ResponseCode.STATUS_BAD_SESSION_ID);
      return;
    }

    int[] sessionIds = {
        (int) (clientSessionId >> 32),
        (int) clientSessionId,
        (int) (serverSessionId >> 32),
        (int) serverSessionId
    };

    world.getService(AuthenticationService.class).submit(new AuthenticationRequest(session,
        new PlayerCredentials(message.getUserId(), message.getUsername(), message.getPassword(),
            sessionIds)));
  }

  /**
   * Closes the specified Session and notifies the client with the specified ResponseCode.
   *
   * @param session The Session to close.
   * @param response The ResponseCode sent to the client.
   */
  private void closeWithResponse(Session session, ResponseCode response) {
    session.writeAndFlush(new LoginResponseMessage(response))
        .addListener(ChannelFutureListener.CLOSE);
  }

}
