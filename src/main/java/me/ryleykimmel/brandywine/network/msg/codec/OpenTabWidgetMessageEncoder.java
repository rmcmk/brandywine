package me.ryleykimmel.brandywine.network.msg.codec;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.OpenTabWidgetMessage;

/**
 * Encodes the {@link OpenTabWidgetMessage}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Encodes(OpenTabWidgetMessage.class)
public final class OpenTabWidgetMessageEncoder implements MessageEncoder<OpenTabWidgetMessage> {

  @Override
  public Frame encode(OpenTabWidgetMessage message, ByteBufAllocator alloc) {
    FrameBuilder builder = new FrameBuilder(71, alloc);
    builder.put(DataType.SHORT, message.getWidgetId());
    builder.put(DataType.BYTE, DataTransformation.ADD, message.getTabId());
    return builder.build();
  }

}
