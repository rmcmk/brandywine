package me.ryleykimmel.brandywine.network.frame.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.message.Message;

import java.util.List;

/**
 * A standard codec used for decoding and encoding {@link Frame} and {@link Message} objects.
 */
public final class FrameMessageCodec extends MessageToMessageCodec<Frame, Message> {

  /**
   * A {@link PooledByteBufAllocator} responsible for allocating {@link ByteBuf buffers}.
   */
  private final ByteBufAllocator allocator = new PooledByteBufAllocator();

  /**
   * The Session for this FrameMessageCodec.
   */
  protected final Session session;

  /**
   * Constructs a new {@link FrameMessageCodec}.
   *
   * @param session The Session for this FrameMessageCodec.
   */
  public FrameMessageCodec(Session session) {
    this.session = session;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) {
    if (session.isClosed()) {
      return;
    }

    FrameMetadataSet metadata = session.getFrameMetadataSet();
    out.add(metadata.encode(message, allocator));
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, Frame frame, List<Object> out) {
    if (session.isClosed()) {
      return;
    }

    FrameMetadataSet metadata = session.getFrameMetadataSet();
    out.add(metadata.decode(frame));
  }

}
