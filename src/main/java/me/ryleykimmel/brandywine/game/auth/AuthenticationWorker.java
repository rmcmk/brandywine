package me.ryleykimmel.brandywine.game.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.model.World;
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
		World world = service.getWorld();

		try {
			AuthenticationResponse response = strategy.authenticate(player);

			if (response.getStatus() != LoginResponseMessage.STATUS_OK) {
				sendResponse(session, response.getStatus());
				return;
			}

			if (service.isQueued(player) || world.isOnline(player)) {
				sendResponse(session, LoginResponseMessage.STATUS_ACCOUNT_ONLINE);
				return;
			}

			if (!service.queuePlayer(player)) {
				sendResponse(session, LoginResponseMessage.STATUS_SERVER_FULL);
				return;
			}

		} catch (Exception cause) {
			logger.error("Error occured while authenticating.", cause);
			sendResponse(session, LoginResponseMessage.STATUS_COULD_NOT_COMPLETE);
		}
	}

	/**
	 * Sends the {@link LoginResponseMessage} with the specified status, should only be used to send invalid login requests.
	 *
	 * @param session The GameSession sending the response.
	 * @param status The status of the response.
	 */
	private void sendResponse(GameSession session, int status) {
		sendResponse(session, status, 0, false);
	}

	/**
	 * Sends the {@link LoginResponseMessgae} with the specified status, privilege and flagged state.
	 *
	 * @param session The GameSession sending the response.
	 * @param status The status of the response.
	 * @param privilege The privilege level of the Player.
	 * @param flagged Whether or not the Player is flagged and should be monitored.
	 */
	private void sendResponse(GameSession session, int status, int privilege, boolean flagged) {
		ChannelFuture future = session.writeAndFlush(new LoginResponseMessage(status, privilege, flagged));
		if (status != LoginResponseMessage.STATUS_OK) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

}