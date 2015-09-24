package me.ryleykimmel.brandywine.game.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFutureListener;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

/**
 * A {@link Runnable} worker which manages {@link AuthenticationRequest}s.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AuthenticationWorker implements Runnable {

	/**
	 * The Logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationWorker.class);

	/**
	 * Represents the default {@link AuthenticationStrategy}.
	 */
	private static final AuthenticationStrategy DEFAULT_STRATEGY = p -> AuthenticationResponse.SUCCESS;

	/**
	 * The context of the Server.
	 */
	private final ServerContext context;

	/**
	 * The AuthenticationStrategy to use when authenticating requests.
	 */
	private final AuthenticationStrategy strategy;

	/**
	 * The request to authenticate.
	 */
	private final AuthenticationRequest request;

	/**
	 * Constructs a new {@link AuthenticationWorker} with the specified AuthenticationStrategy and AuthenticationRequest.
	 *
	 * @param context The context of the Server.
	 * @param strategy The AuthenticationStrategy to use when authenticating requests.
	 * @param request The request to authenticate.
	 */
	public AuthenticationWorker(ServerContext context, AuthenticationStrategy strategy, AuthenticationRequest request) {
		this.context = context;
		this.strategy = strategy;
		this.request = request;
	}

	/**
	 * Constructs a new {@link AuthenticationWorker} with the specified AuthenticationRequest.
	 *
	 * @param context The context of the Server.
	 * @param request The request to authenticate.
	 */
	public AuthenticationWorker(ServerContext context, AuthenticationRequest request) {
		this(context, DEFAULT_STRATEGY, request);
	}

	@Override
	public void run() {
		GameSession session = request.getSession();
		GameService service = context.getService(GameService.class);

		Player player = new Player(session, request.getCredentials());

		try {
			AuthenticationResponse response = strategy.authenticate(player);

			if (response.getStatus() != LoginResponseMessage.STATUS_OK) {
				closeWithResponse(session, response.getStatus());
				return;
			}

			if (service.isPlayerOnline(player)) {
				closeWithResponse(session, LoginResponseMessage.STATUS_ACCOUNT_ONLINE);
				return;
			}

			if (!service.queuePlayer(player)) {
				closeWithResponse(session, LoginResponseMessage.STATUS_SERVER_FULL);
				return;
			}

		} catch (Exception cause) {
			logger.error("Error occured while authenticating.", cause);
			closeWithResponse(session, LoginResponseMessage.STATUS_COULD_NOT_COMPLETE);
		}
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