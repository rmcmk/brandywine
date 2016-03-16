package me.ryleykimmel.brandywine.network.game;

import java.util.List;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * Encodes Messages into Frames.
 */
public final class MessageEncoder extends MessageToMessageEncoder<Message> {

  /**
   * A {@link PooledByteBufAllocator} responsible for allocating {@link ByteBuf buffers}.
   */
  private final ByteBufAllocator allocator = new PooledByteBufAllocator();

  /**
   * The metadata of the Frame we are encoding.
   */
  private final FrameMetadataSet metadata;

  /**
   * Constructs a new {@link MessageEncoder}.
   *
   * @param metadata The metadata of the Frame we are encoding.
   */
  public MessageEncoder(FrameMetadataSet metadata) {
    this.metadata = Preconditions.checkNotNull(metadata, "FrameMetadataSet may not be null.");
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) {
    out.add(metadata.encode(message, allocator));
  }

}
