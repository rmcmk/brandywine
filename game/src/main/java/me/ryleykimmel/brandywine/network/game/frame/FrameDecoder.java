package me.ryleykimmel.brandywine.network.game.frame;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet.Type;

/**
 * Decodes Frames in a stream-like fashion from one {@link ByteBuf} to another, while maintaining a
 * state.
 */
public final class FrameDecoder extends ByteToMessageDecoder {

  /**
   * Represents the states of this FrameDecoder.
   */
  private enum State {

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

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(FrameDecoder.class);

  /**
   * The GameSession we're decoding for.
   */
  private final GameSession session;

  /**
   * The Frame metadata set.
   */
  private final FrameMetadataSet metadataSet;

  /**
   * The metadata of the Frame we're decoding.
   */
  private FrameMetadata metadata;

  /**
   * The amount of expected bytes in a Frames payload.
   */
  private int payloadLength;

  /**
   * The current State of the Frame decoder.
   */
  private State state = State.DECODE_OPCODE;

  /**
   * Constructs a new {@link FrameDecoder}.
   *
   * @param session The GameSession we're decoding for.
   * @param metadataSet The Frame metadata set.
   */
  public FrameDecoder(GameSession session, FrameMetadataSet metadataSet) {
    this.session = Preconditions.checkNotNull(session, "GameSession may not be null.");
    this.metadataSet = Preconditions.checkNotNull(metadataSet, "GameSession may not be null.");;
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
  private void decodeOpcode(ByteBuf buffer, List<Object> out) {
    if (!buffer.isReadable()) {
      return;
    }

    int cipheredOpcode = buffer.readUnsignedByte();
    int opcode =
        session.isCipheringFrames() ? session.decipherFrameOpcode(cipheredOpcode) : cipheredOpcode;

    if (!metadataSet.hasMetadata(Type.DOWNSTREAM, opcode)) {
      logger.warn(
          "The Frame {} is invalid (is it mapped within frames.toml?) and cannot be decoded, closing session...",
          opcode);
      session.close();
      return;
    }

    metadata = metadataSet.getMetadata(Type.DOWNSTREAM, opcode);
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
  private void decodeLength(ByteBuf buffer, List<Object> out) {
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
  private void decodePayload(ByteBuf buffer, List<Object> out) {
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
  private void checkpoint(State state) {
    this.state = Preconditions.checkNotNull(state, "State may not be null.");
  }

}
