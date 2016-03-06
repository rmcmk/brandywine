package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.OpenTabWidgetMessage;

/**
 * Encodes the {@link OpenTabWidgetMessage}.
 */
public final class OpenTabWidgetMessageEncoder implements MessageEncoder<OpenTabWidgetMessage> {

  @Override
  public void encode(OpenTabWidgetMessage message, FrameBuilder builder) {
    builder.put(DataType.SHORT, message.getWidgetId());
    builder.put(DataType.BYTE, DataTransformation.ADD, message.getTabId());
  }

}
