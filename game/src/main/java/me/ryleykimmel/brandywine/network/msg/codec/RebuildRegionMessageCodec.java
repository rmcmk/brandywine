package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.RebuildRegionMessage;

/**
 * MessageCodec for the {@link RebuildRegionMessage}.
 */
public final class RebuildRegionMessageCodec implements MessageCodec<RebuildRegionMessage> {

  @Override
  public void encode(RebuildRegionMessage message, FrameBuilder builder) {
    builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());
    builder.put(DataType.SHORT, message.getPosition().getCentralRegionY());
  }

  @Override
  public RebuildRegionMessage decode(FrameReader frame) {
    return null;
  }

}
