package me.ryleykimmel.brandywine.network.msg.handler;

import io.netty.channel.ChannelFutureListener;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationRequest;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Handles;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.LoginMessage;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

/**
 * Handles the {@link LoginMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Handles(LoginMessage.class)
public final class LoginMessageHandler implements MessageHandler<LoginMessage> {

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

		GameService gameService = session.getContext().getService(GameService.class);
		if (!gameService.addChannel(session)) {
			closeWithResponse(session, LoginResponseMessage.STATUS_TOO_MANY_CONNECTIONS);
			return;
		}

		int[] sessionKeys = message.getSessionKeys();

		long sessionKey = (long) sessionKeys[2] << 32L | sessionKeys[3] & 0xFFFFFFFFL;
		if (session.getSessionKey() != sessionKey) {
			closeWithResponse(session, LoginResponseMessage.STATUS_BAD_SESSION_ID);
			return;
		}

		AuthenticationService authenticationService = session.getContext().getService(AuthenticationService.class);
		authenticationService.submit(new AuthenticationRequest(session, new PlayerCredentials(message.getUserId(), message.getUsername(), message.getPassword(), sessionKeys)));
	}

	/**
	 * Closes the specified GameSession after sending the specified response code.
	 * 
	 * @param session The GameSession to close.
	 * @param response The response to send.
	 */
	private void closeWithResponse(GameSession session, int response) {
		session.writeAndFlush(new LoginResponseMessage(response)).addListener(ChannelFutureListener.CLOSE);
	}

}