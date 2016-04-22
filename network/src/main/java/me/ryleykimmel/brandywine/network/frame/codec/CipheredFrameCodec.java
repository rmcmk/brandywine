package me.ryleykimmel.brandywine.network.frame.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadata;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.isaac.IsaacRandom;
import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair;

public class CipheredFrameCodec<T extends Session> extends FrameCodec<T> {

  protected final IsaacRandomPair randomPair;

  public CipheredFrameCodec(T session, FrameMetadataSet metadataSet, IsaacRandomPair randomPair) {
    super(session, metadataSet);
    this.randomPair = randomPair;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Frame frame, ByteBuf buffer) {
    IsaacRandom random = randomPair.getEncodingRandom();
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

  @Override
  protected void decodeOpcode(ByteBuf buffer, List<Object> out) {
    IsaacRandom random = randomPair.getDecodingRandom();
    int opcode = buffer.readUnsignedByte() - random.nextInt() & 0xFF;

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

}
