package me.ryleykimmel.brandywine.network.frame.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadata;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;

public class FrameCodec<T extends Session> extends ByteToMessageCodec<Frame> {

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

  protected static final Logger logger = LoggerFactory.getLogger(FrameCodec.class);

  protected final T session;
  protected final FrameMetadataSet metadataSet;

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

  public FrameCodec(T session, FrameMetadataSet metadataSet) {
    this.session = session;
    this.metadataSet = metadataSet;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buffer) {
    buffer.writeBytes(frame.content());
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
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
    int opcode = buffer.readUnsignedByte(); // Short circuit if no available bytes

    metadata = metadataSet.getMetadata(opcode);
    payloadLength = metadata.getLength();

    if (metadata.hasVariableLength()) {
      checkpoint(State.DECODE_LENGTH);
    } else if (payloadLength == 0) {
      out.add(new Frame(metadata));
    } else {
      checkpoint(State.DECODE_PAYLOAD);
    }
  }

  /**
   * Decodes the length of some Frame.
   * 
   * @param buffer The Buffer to decode from.
   * @param out The List which decoded Frames are to be added.
   */
  protected void decodeLength(ByteBuf buffer, List<Object> out) {
    int expected = Math.abs(metadata.getLength());
    if (!buffer.isReadable(expected)) {
      logger.error("Not enough bytes available to read frame {}'s length, closing session...",
          metadata);
      session.close();
      return;
    }

    payloadLength = 0;
    for (int i = 0; i < expected; i++) {
      payloadLength |= buffer.readUnsignedByte() << 8 * (expected - 1 - i);
    }

    checkpoint(State.DECODE_PAYLOAD);
  }

  /**
   * Decodes the payload of some Frame.
   * 
   * @param buffer The Buffer to decode from.
   * @param out The List which decoded Frames are to be added.
   */
  protected void decodePayload(ByteBuf buffer, List<Object> out) {
    if (!buffer.isReadable(payloadLength)) {
      logger.error("Not enough bytes available to decode frame {}'s payload, closing session...",
          metadata);
      session.close();
      return;
    }

    out.add(new Frame(metadata, buffer.readBytes(payloadLength)));
    checkpoint(State.DECODE_OPCODE);
  }

  /**
   * Updates the State of the decoder.
   * 
   * @param state The next State in the decoder.
   */
  protected final void checkpoint(State state) {
    this.state = Preconditions.checkNotNull(state, "State may not be null.");
  }

}
