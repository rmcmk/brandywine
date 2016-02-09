package me.ryleykimmel.brandywine.network.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A specialized {@link SimpleChannelInboundHandler} which only receives {@link Message messages}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class GameSessionHandler extends SimpleChannelInboundHandler<Message> {

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
  public void channelInactive(ChannelHandlerContext ctx) {
    session.destroy();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.error("An uncaught error has occured, closing session.", cause);
    session.close();
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
