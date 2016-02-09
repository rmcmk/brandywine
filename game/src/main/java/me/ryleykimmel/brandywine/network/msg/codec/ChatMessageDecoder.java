package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.common.util.TextUtil;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.Decodes;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;

/**
 * Decodes the {@link ChatMessage}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Decodes(4)
public final class ChatMessageDecoder implements MessageDecoder<ChatMessage> {

  @Override
  public ChatMessage decode(GameSession session, FrameReader reader) {
    int effects = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
    int color = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
    int length = reader.getLength() - 2;
    byte[] originalCompressed = reader.getBytesReverse(DataTransformation.ADD, length);

    String uncompressed = TextUtil.decompress(originalCompressed, length);
    uncompressed = TextUtil.filterInvalidCharacters(uncompressed);
    uncompressed = Strings.capitalize(uncompressed);

    byte[] recompressed = new byte[length];
    TextUtil.compress(uncompressed, recompressed);

    return new ChatMessage(uncompressed, recompressed, color, effects);
  }

}
