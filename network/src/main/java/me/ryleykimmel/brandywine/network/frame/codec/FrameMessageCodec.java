package me.ryleykimmel.brandywine.network.frame.codec;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.msg.Message;

import java.util.List;

/**
 * A standard codec used for decoding and encoding {@link Frame} and {@link Message} objects.
 */
public class FrameMessageCodec extends MessageToMessageCodec<Frame, Message> {

  /**
   * A {@link PooledByteBufAllocator} responsible for allocating {@link ByteBuf buffers}.
   */
  protected final ByteBufAllocator allocator = new PooledByteBufAllocator();

  /**
   * The metadata of the Frame.
   */
  protected final FrameMetadataSet metadata;

  /**
   * Constructs a new {@link FrameMessageCodec}.
   *
   * @param metadata The metadata of the Frame.
   */
  public FrameMessageCodec(FrameMetadataSet metadata) {
    this.metadata = Preconditions.checkNotNull(metadata, "FrameMetadataSet may not be null.");
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) {
    out.add(metadata.encode(message, allocator));
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, Frame frame, List<Object> out) {
    out.add(metadata.decode(frame));
  }

}
