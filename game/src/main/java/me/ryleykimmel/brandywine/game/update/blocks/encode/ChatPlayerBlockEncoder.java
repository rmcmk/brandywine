package me.ryleykimmel.brandywine.game.update.blocks.encode;

import me.ryleykimmel.brandywine.game.update.UpdateBlockEncoder;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;

public final class ChatPlayerBlockEncoder implements UpdateBlockEncoder<ChatPlayerBlock> {

  @Override
  public void encode(ChatPlayerBlock block, FrameBuilder builder) {
    ChatMessage chatMessage = block.getChatMessage();

    builder.put(DataType.BYTE, chatMessage.getTextEffects());
    builder.put(DataType.BYTE, chatMessage.getTextColor());
    builder.put(DataType.BYTE, block.getPrivilegeId());

    byte[] bytes = chatMessage.getCompressedMessage();
    builder.put(DataType.BYTE, DataTransformation.NEGATE, bytes.length);
    builder.putBytesReverse(bytes);
  }

}
