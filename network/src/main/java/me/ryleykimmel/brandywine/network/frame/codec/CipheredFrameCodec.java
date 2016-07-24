package me.ryleykimmel.brandywine.network.frame.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadata;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.isaac.IsaacRandom;
import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair;

import java.util.List;

public final class CipheredFrameCodec extends FrameCodec {

  private final IsaacRandomPair randomPair;

  public CipheredFrameCodec(Session session, FrameMetadataSet metadataSet,
    IsaacRandomPair randomPair) {
    super(session, metadataSet);
    this.randomPair = randomPair;
  }

  @Override protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buffer) {
    IsaacRandom random = randomPair.getEncodingRandom();
    FrameMetadata metadata = metadataSet.getMetadata(frame.getOpcode());

    buffer.writeByte(frame.getOpcode() + random.nextInt() & 0xFF);

    if (metadata.hasVariableLength()) {
      int length = metadata.getLength(), actual = frame.getLength();

      switch (length) {
        case FrameMetadata.VARIABLE_BYTE_LENGTH:
          buffer.writeByte(actual);
          break;

        case FrameMetadata.VARIABLE_SHORT_LENGTH:
          buffer.writeShort(actual);
          break;

        default:
          throw new UnsupportedOperationException(length + " is not a supported variable length.");
      }
    }

    buffer.writeBytes(frame.content());
  }

  @Override protected void decodeOpcode(ByteBuf buffer, List<Object> out) {
    IsaacRandom random = randomPair.getDecodingRandom();
    int opcode = buffer.readUnsignedByte() - random.nextInt() & 0xFF;

    if (!metadataSet.hasMapping(opcode)) {
      throw new IllegalStateException("Bad frame opcode: " + opcode);
    }

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

  @Override protected void decodeLength(ByteBuf buffer, List<Object> out) {
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

  @Override protected void decodePayload(ByteBuf buffer, List<Object> out) {
    if (!buffer.isReadable(payloadLength)) {
      logger.error("Not enough bytes available to decode frame {}'s payload, closing session...",
        metadata);
      session.close();
      return;
    }

    out.add(new Frame(metadata, buffer.readBytes(payloadLength)));
    checkpoint(State.DECODE_OPCODE);
  }

}
