package me.ryleykimmel.brandywine.network.msg.handler;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelFutureListener;
import me.ryleykimmel.brandywine.game.auth.AuthenticationRequest;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.GameMessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.LoginMessage;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

/**
 * Handles the {@link LoginMessage}.
 */
public final class LoginMessageHandler implements GameMessageHandler<LoginMessage> {

  /**
   * The expected version of the client.
   */
  private static final int CLIENT_VERSION = 317;

  /**
   * The expected value of the received dummy.
   */
  private static final int EXPECTED_DUMMY = 255;

  /**
   * The expected value of the received block operation code.
   */
  private static final int EXPECTED_BLOCK_OPCODE = 10;

  /**
   * The AuthenticationService used to forward login requests.
   */
  private final AuthenticationService service;

  /**
   * Constructs a new {@link LoginMessageHandler}.
   * 
   * @param service The AuthenticationService used to forward login requests.
   */
  public LoginMessageHandler(AuthenticationService service) {
    this.service = Preconditions.checkNotNull(service, "AuthenticationService may not be null.");
  }

  @Override
  public void handle(GameSession session, LoginMessage message) {
    if (message.getDummy() != EXPECTED_DUMMY) {
      closeWithResponse(session, LoginResponseMessage.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    if (message.getClientVersion() != CLIENT_VERSION) {
      closeWithResponse(session, LoginResponseMessage.STATUS_GAME_UPDATED);
      return;
    }

    if (message.getDetail() != 0 && message.getDetail() != 1) {
      closeWithResponse(session, LoginResponseMessage.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    if (message.getBlockOperationCode() != EXPECTED_BLOCK_OPCODE) {
      closeWithResponse(session, LoginResponseMessage.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    int[] sessionKeys = message.getSessionKeys();

    if (sessionKeys.length != 4) { // TODO: Rid of magic
      closeWithResponse(session, LoginResponseMessage.STATUS_BAD_SESSION_ID);
      return;
    }

    long sessionKey = (long) sessionKeys[2] << 32L | sessionKeys[3] & 0xFFFFFFFFL;
    if (session.getSessionKey() != sessionKey) {
      closeWithResponse(session, LoginResponseMessage.STATUS_BAD_SESSION_ID);
      return;
    }

    service.submit(new AuthenticationRequest(session, new PlayerCredentials(message.getUserId(),
        message.getUsername(), message.getPassword(), sessionKeys)));
  }

  /**
   * Closes the specified GameSession after sending the specified response code.
   * 
   * @param session The GameSession to close.
   * @param response The response to send.
   */
  private void closeWithResponse(GameSession session, int response) {
    session.writeAndFlush(new LoginResponseMessage(response))
        .addListener(ChannelFutureListener.CLOSE);
  }

}
