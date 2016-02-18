package me.ryleykimmel.brandywine.network.game;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.parser.impl.MessageCodecParser;

/**
 * Encodes Messages into Frames.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class MessageEncoder extends MessageToMessageEncoder<Message> {

  /**
   * A {@link PooledByteBufAllocator} responsible for allocating {@link ByteBuf buffers}.
   */
  private final ByteBufAllocator allocator = new PooledByteBufAllocator();

  /**
   * The GameSession we're encoding for.
   */
  private final GameSession session;

  /**
   * Constructs a new {@link MessageEncoder} with the specified GameSession.
   *
   * @param session The GameSession we're encoding for.
   */
  public MessageEncoder(GameSession session) {
    this.session = session;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) {
    MessageCodecParser parser = session.getContext().getParser(MessageCodecParser.class);
    out.add(parser.encode(message, allocator));
  }

}
