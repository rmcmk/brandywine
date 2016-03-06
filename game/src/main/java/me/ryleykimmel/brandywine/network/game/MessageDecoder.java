package me.ryleykimmel.brandywine.network.game;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.ryleykimmel.brandywine.network.game.frame.Frame;

/**
 * Decodes Frames into Messages.
 */
public final class MessageDecoder extends MessageToMessageDecoder<Frame> {

  /**
   * The GameSession we're decoding for.
   */
  private final GameSession session;

  /**
   * Constructs a new {@link MessageDecoder} with the specified GameSession.
   *
   * @param session The GameSession we're decoding for.
   */
  public MessageDecoder(GameSession session) {
    this.session = session;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, Frame frame, List<Object> out) {
    out.add(session.getContext().getFrameMetadataSet().decode(session, frame));
  }

}
