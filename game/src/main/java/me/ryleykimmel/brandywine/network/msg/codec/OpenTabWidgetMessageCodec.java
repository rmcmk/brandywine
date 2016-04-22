package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.OpenTabWidgetMessage;

/**
 * MessageCodec for the {@link OpenTabWidgetMessage}.
 */
public final class OpenTabWidgetMessageCodec implements MessageCodec<OpenTabWidgetMessage> {

  @Override
  public void encode(OpenTabWidgetMessage message, FrameBuilder builder) {
    builder.put(DataType.SHORT, message.getWidgetId());
    builder.put(DataType.BYTE, DataTransformation.ADD, message.getTabId());
  }

  @Override
  public OpenTabWidgetMessage decode(FrameReader frame) {
    return null;
  }

}
