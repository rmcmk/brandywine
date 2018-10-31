package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.common.util.TextUtil;
import me.ryleykimmel.brandywine.game.message.ChatMessage;
import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link ChatMessage}.
 */
public final class ChatMessageCodec extends MessageCodec<ChatMessage> {

  @Override
  public ChatMessage decode(FrameReader reader) {
    int effects = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
    int color = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
    int length = reader.readableBytes();
    byte[] originalCompressed = reader.getBytesReverse(DataTransformation.ADD, length);

    String uncompressed = TextUtil.decompress(originalCompressed, length);
    uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
    uncompressed = Strings.capitalize(uncompressed);

    byte[] recompressed = new byte[length];
    TextUtil.compress(uncompressed, recompressed);

    return new ChatMessage(uncompressed, recompressed, color, effects);
  }

}
