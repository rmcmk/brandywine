package me.ryleykimmel.brandywine.network.game;

import java.util.List;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;

/**
 * Decodes Frames into Messages.
 */
public final class MessageDecoder extends MessageToMessageDecoder<Frame> {

  /**
   * The metadata of the Frame we are decoding.
   */
  private final FrameMetadataSet metadata;

  /**
   * Constructs a new {@link MessageDecoder}.
   *
   * @param metadata The metadata of the Frame we are decoding.
   */
  public MessageDecoder(FrameMetadataSet metadata) {
    this.metadata = Preconditions.checkNotNull(metadata, "FrameMetadataSet may not be null.");
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, Frame frame, List<Object> out) {
    out.add(metadata.decode(frame));
  }

}
