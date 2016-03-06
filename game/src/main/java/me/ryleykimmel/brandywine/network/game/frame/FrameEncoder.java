package me.ryleykimmel.brandywine.network.game.frame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.ryleykimmel.brandywine.network.game.GameSession;

/**
 * Encodes Frames in a stream-like fashion from a message to {@link ByteBuf}.
 */
public final class FrameEncoder extends MessageToByteEncoder<Frame> {

  /**
   * The GameSession to encode Frames for.
   */
  private final GameSession session;

  /**
   * Constructs a new {@link FrameEncoder} with the specified GameSession.
   *
   * @param session The GameSession to encode Frames for.
   */
  public FrameEncoder(GameSession session) {
    this.session = session;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf out) {
    if (session.isClosed()) {
      return;
    }

    FrameMetadata metadata = frame.getMetadata();

    if (metadata.isCiphered() && !metadata.isHeadless()) {
      out.writeByte(session.encipherFrameOpcode(frame.getOpcode()));

      if (metadata.hasVariableLength()) {
        int length = metadata.getLength(), actual = frame.getLength();

        switch (length) {
          case FrameMetadata.VARIABLE_BYTE_LENGTH:
            out.writeByte(actual);
            break;

          case FrameMetadata.VARIABLE_SHORT_LENGTH:
            out.writeShort(actual);
            break;

          default:
            throw new UnsupportedOperationException(
                length + " is not a supported variable length.");
        }
      }
    }

    out.writeBytes(frame.content());
  }

}
