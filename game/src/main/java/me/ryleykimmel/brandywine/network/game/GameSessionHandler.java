package me.ryleykimmel.brandywine.network.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A specialized {@link SimpleChannelInboundHandler} which only receives {@link Message messages}.
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
   * The GameService for this ChannelHandler.
   */
  private final GameService service;

  /**
   * The FrameMetadataSet for this ChannelHandler.
   */
  private final FrameMetadataSet metadata;

  /**
   * Constructs a new {@link GameSessionHandler}.
   *
   * @param session The GameSession for this ChannelHandler.
   * @param service The GameService for this ChannelHandler.
   * @param metadata The FrameMetadataSet for this ChannelHandler.
   */
  public GameSessionHandler(GameSession session, GameService service, FrameMetadataSet metadata) {
    this.session = Preconditions.checkNotNull(session, "GameSession may not be null.");
    this.service = Preconditions.checkNotNull(service, "GameService may not be null.");
    this.metadata = Preconditions.checkNotNull(metadata, "FrameMetadataSet may not be null.");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    session.destroy(service);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.error("An uncaught error has occured, closing session.", cause);
    session.close();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message message) {
    try {
      session.dispatch(metadata, message);
    } catch (Throwable cause) {
      logger.error("Error while handling game message, closing session.", cause);
      session.close();
    }
  }

}
