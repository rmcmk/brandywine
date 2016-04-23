package me.ryleykimmel.brandywine.game.msg.event;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelFutureListener;
import me.ryleykimmel.brandywine.game.GameSession;
import me.ryleykimmel.brandywine.game.auth.AuthenticationRequest;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials;
import me.ryleykimmel.brandywine.game.msg.LoginMessage;
import me.ryleykimmel.brandywine.game.msg.LoginResponseMessage;
import me.ryleykimmel.brandywine.network.ResponseCode;

/**
 * Handles the {@link LoginMessage}.
 */
public final class LoginMessageConsumer implements EventConsumer<LoginMessage> {

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
   * The GameSession.
   */
  private final GameSession session;

  /**
   * The AuthenticationService used to forward login requests.
   */
  private final AuthenticationService service;

  /**
   * Constructs a new {@link LoginMessageConsumer}.
   * 
   * @param session The GameSession.
   * @param service The AuthenticationService used to forward login requests.
   */
  public LoginMessageConsumer(GameSession session, AuthenticationService service) {
    this.session = Preconditions.checkNotNull(session, "LoginSession may not be null.");
    this.service = Preconditions.checkNotNull(service, "AuthenticationService may not be null.");
  }

  /**
   * Closes the specified LoginSession after sending the specified response code.
   * 
   * @param response The response to send.
   */
  private void closeWithResponse(ResponseCode response) {
    session.writeAndFlush(new LoginResponseMessage(response))
        .addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void accept(LoginMessage message) {
    if (message.getDummy() != EXPECTED_DUMMY) {
      closeWithResponse(ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    if (message.getClientVersion() != CLIENT_VERSION) {
      closeWithResponse(ResponseCode.STATUS_GAME_UPDATED);
      return;
    }

    if (message.getDetail() != 0 && message.getDetail() != 1) {
      closeWithResponse(ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    if (message.getBlockOperationCode() != EXPECTED_BLOCK_OPCODE) {
      closeWithResponse(ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION);
      return;
    }

    int[] sessionKeys = message.getSessionKeys();

    if (sessionKeys.length != 4) { // TODO: Rid of magic
      closeWithResponse(ResponseCode.STATUS_BAD_SESSION_ID);
      return;
    }

    long sessionKey = (long) sessionKeys[2] << 32L | sessionKeys[3] & 0xFFFFFFFFL;
    if (session.getSessionKey() != sessionKey) {
      closeWithResponse(ResponseCode.STATUS_BAD_SESSION_ID);
      return;
    }

    service.submit(new AuthenticationRequest(session, new PlayerCredentials(message.getUserId(),
        message.getUsername(), message.getPassword(), sessionKeys)));
  }

}
