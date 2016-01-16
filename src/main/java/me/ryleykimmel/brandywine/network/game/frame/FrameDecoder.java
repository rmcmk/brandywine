package me.ryleykimmel.brandywine.network.game.frame;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.parser.impl.FrameParser;

/**
 * Decodes Frames in a stream-like fashion from one {@link ByteBuf} to another, while maintaining a
 * state.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class FrameDecoder extends ByteToMessageDecoder {

  /**
   * Represents the states of this FrameDecoder.
   *
   * @author Ryley Kimmel <ryley.kimmel@live.com>
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
   * The opcode of the Frame currently being decoded.
   */
  private int opcode;

  /**
   * The type of the Frame currently being decoded.
   */
  private FrameType type;

  /**
   * The length of the Frame currently being decoded.
   */
  private int length;

  /**
   * The GameSession we're decoding for.
   */
  private final GameSession session;

  /**
   * The current State of the Frame decoder.
   */
  private State state = State.DECODE_OPCODE;

  /**
   * Constructs a new {@link FrameDecoder} with the specified GameSession.
   *
   * @param session The GameSession we're decoding for.
   */
  public FrameDecoder(GameSession session) {
    this.session = session;
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
  private void decodeOpcode(ByteBuf buffer, List<Object> out) {
    if (!buffer.isReadable()) {
      return;
    }

    FrameParser parser = session.getContext().getParser(FrameParser.class);

    boolean ciphered = session.isCipheringFrames();
    opcode = ciphered ? session.decipherFrameOpcode(buffer.readUnsignedByte())
        : buffer.readUnsignedByte();
    length = parser.getLength(ciphered, opcode);
    type = parser.getType(ciphered, opcode);

    switch (type) {
      case INVALID:
        logger.warn(
            "The Frame {} is invalid (is it mapped within frames.toml?) and cannot be decoded, closing session...",
            opcode);
        session.close();
        break;

      case EMPTY:
        out.add(new Frame(opcode, type, Unpooled.EMPTY_BUFFER));
        break;

      case VARIABLE_BYTE:
      case VARIABLE_SHORT:
        checkpoint(State.DECODE_LENGTH);
        break;

      case FIXED:
        checkpoint(State.DECODE_PAYLOAD);
        break;
    }
  }

  /**
   * Decodes the length of some Frame.
   * 
   * @param buffer The Buffer to decode from.
   * @param out The List which decoded Frames are to be added.
   */
  private void decodeLength(ByteBuf buffer, List<Object> out) {
    int check = type == FrameType.VARIABLE_BYTE ? Byte.BYTES : Short.BYTES;
    if (!buffer.isReadable(check)) {
      logger.error("Not enough bytes available to read frame {}'s length, closing session...",
          opcode);
      session.close();
      return;
    }

    length = 0;
    for (int i = 0; i < check; i++) {
      length |= buffer.readUnsignedByte() << 8 * (check - 1 - i);
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
    if (!buffer.isReadable(length)) {
      logger.error("Not enough bytes available to decode frame {}'s payload, closing session...",
          opcode);
      session.close();
      return;
    }

    out.add(new Frame(opcode, type, buffer.readBytes(length)));
    checkpoint(State.DECODE_OPCODE);
  }

  /**
   * Updates the State of the decoder.
   * 
   * @param state The next State in the decoder.
   */
  private void checkpoint(State state) {
    this.state = state;
  }

}
