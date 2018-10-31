package me.ryleykimmel.brandywine.network.frame.codec;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * A codec for encoding and decoding Frames.
 */
public class FrameCodec extends ByteToMessageCodec<Frame> {

  /**
   * The Logger for this class.
   */
  protected static final Logger logger = LogManager.getLogger(FrameCodec.class);

  /**
   * The Session for this FrameCodec.
   */
  protected final Session session;

  /**
   * The metadata of the Frame we're decoding.
   */
  protected FrameMetadata metadata;

  /**
   * The amount of expected bytes in a Frames payload.
   */
  protected int payloadLength;

  /**
   * The current State of the Frame decoder.
   */
  private State state = State.DECODE_OPCODE;

  /**
   * Constructs a new {@link FrameCodec}.
   *
   * @param session The Session for this FrameCodec.
   */
  public FrameCodec(Session session) {
    this.session = session;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buffer) {
    if (session.isClosed()) {
      return;
    }

    buffer.writeBytes(frame.content());
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
    if (session.isClosed()) {
      return;
    }

    switch (state) {
      case DECODE_OPCODE:
        decodeOpcode(buffer, out);
        break;

      case DECODE_LENGTH:
        decodeLength(buffer, out);
        break;

      case DECODE_PAYLOAD:
        decodePayload(buffer, out);
        break;
    }
  }

  /**
   * Decodes the opcode of some Frame.
   *
   * @param buffer The Buffer to decode from.
   * @param out The List which decoded Frames are to be added.
   */
  protected void decodeOpcode(ByteBuf buffer, List<Object> out) {
    int opcode = buffer.readUnsignedByte();
    out.add(new Frame(session.getFrameMetadataSet().getMetadata(opcode),
            buffer.readBytes(buffer.readableBytes())));
  }

  /**
   * Decodes the length of some Frame.
   *
   * @param buffer The Buffer to decode from.
   * @param out The List which decoded Frames are to be added.
   */
  protected void decodeLength(ByteBuf buffer, List<Object> out) {
  }

  /**
   * Decodes the payload of some Frame.
   *
   * @param buffer The Buffer to decode from.
   * @param out The List which decoded Frames are to be added.
   */
  protected void decodePayload(ByteBuf buffer, List<Object> out) {
  }

  /**
   * Updates the State of the decoder.
   *
   * @param state The next State in the decoder.
   */
  protected final void checkpoint(State state) {
    this.state = Preconditions.checkNotNull(state, "State may not be null.");
  }

  /**
   * Represents the states of this FrameCodec.
   */
  protected enum State {

    /**
     * The state in which we decode the opcode of some Frame.
     */
    DECODE_OPCODE,

    /**
     * The state in which we decode the length, if it is a variable byte or short, of some Frame.
     */
    DECODE_LENGTH,

    /**
     * The state in which we decode the payload of some Frame.
     */
    DECODE_PAYLOAD

  }

}
