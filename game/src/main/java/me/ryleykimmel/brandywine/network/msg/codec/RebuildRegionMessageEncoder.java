package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.RebuildRegionMessage;

/**
 * Encodes the {@link RebuildRegionMessage}.
 */
public final class RebuildRegionMessageEncoder implements MessageEncoder<RebuildRegionMessage> {

  @Override
  public void encode(RebuildRegionMessage message, FrameBuilder builder) {
    builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());
    builder.put(DataType.SHORT, message.getPosition().getCentralRegionY());
  }

}
