package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.RebuildRegionMessage;
import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link RebuildRegionMessage}.
 */
public final class RebuildRegionMessageCodec extends MessageCodec<RebuildRegionMessage> {

  @Override
  public void encode(RebuildRegionMessage message, FrameBuilder builder) {
    builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());
    builder.put(DataType.SHORT, message.getPosition().getCentralRegionY());
  }

}
