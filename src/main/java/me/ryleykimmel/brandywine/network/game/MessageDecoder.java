package me.ryleykimmel.brandywine.network.game;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.parser.impl.MessageCodecParser;

/**
 * Decodes Frames into Messages.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
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
    MessageCodecParser parser = session.getContext().getParser(MessageCodecParser.class);
    out.add(parser.decode(frame));
  }

}
