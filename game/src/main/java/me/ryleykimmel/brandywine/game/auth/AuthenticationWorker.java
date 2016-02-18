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
 */
public final class AuthenticationWorker implements Runnable {

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationWorker.class);

  /**
   * The context of the Server.
   */
  private final ServerContext context;

  /**
   * The request to authenticate.
   */
  private final AuthenticationRequest request;

  /**
   * Constructs a new {@link AuthenticationWorker} with the specified and AuthenticationRequest.
   *
   * @param context The context of the Server.
   * @param request The request to authenticate.
   */
  public AuthenticationWorker(ServerContext context, AuthenticationRequest request) {
    this.context = context;
    this.request = request;
  }

  @Override
  public void run() {
    GameSession session = request.getSession();
    GameService service = context.getService(GameService.class);

    Player player = new Player(session, request.getCredentials(), service.getWorld(), context);

    try {
      AuthenticationResponse response = context.getAuthenticationStrategy().authenticate(player);

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
    session.writeAndFlush(new LoginResponseMessage(response))
        .addListener(ChannelFutureListener.CLOSE);
  }

}
