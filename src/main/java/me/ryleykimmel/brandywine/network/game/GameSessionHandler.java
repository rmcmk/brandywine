package me.ryleykimmel.brandywine.network.game;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A specialized {@link SimpleChannelInboundHandler} which only receives {@link Message messages}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class GameSessionHandler extends SimpleChannelInboundHandler<Message> {

  /**
   * A {@link ChannelFutureListener} performed when a GameSession has closed.
   * 
   * @author Ryley Kimmel <ryley.kimmel@live.com>
   */
  private static final class GameSessionCloseFutureListener implements ChannelFutureListener {

    /**
     * The GameSession that has closed.
     */
    private final GameSession session;

    /**
     * Constructs a new {@link GameSessionCloseFutureListener} with the specified GameSession.
     * 
     * @param session The GameSession that has closed.
     */
    public GameSessionCloseFutureListener(GameSession session) {
      this.session = session;
    }

    @Override
    public void operationComplete(ChannelFuture future) {
      GameService service = session.getContext().getService(GameService.class);
      Optional<Player> player = Optional.ofNullable(session.attr().getAndRemove());
      player.ifPresent(service::removePlayer);
    }

  }

  /**
   * The logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(GameSessionHandler.class);

  /**
   * The GameSession for this ChannelHandler.
   */
  private final GameSession session;

  /**
   * Constructs a new {@link GameSessionHandler} with the specified GameSession.
   *
   * @param session The GameSession for this ChannelHandler.
   */
  public GameSessionHandler(GameSession session) {
    this.session = session;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    // When the Channel becomes active inform it of what we want to do when it closes.
    ctx.channel().closeFuture().addListener(new GameSessionCloseFutureListener(session));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.error("An uncaught error has occured, closing session.", cause);
    session.close();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message message) {
    try {
      session.dispatch(message);
    } catch (Throwable cause) {
      logger.error("Error while handling game message, closing session.", cause);
      session.close();
    }
  }

}
