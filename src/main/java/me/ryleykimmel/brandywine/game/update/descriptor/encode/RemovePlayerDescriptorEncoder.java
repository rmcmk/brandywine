package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.update.DescriptorEncoder.PlayerDescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.RemovePlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

public final class RemovePlayerDescriptorEncoder
    implements PlayerDescriptorEncoder<RemovePlayerDescriptor> {

  @Override
  public void encode(RemovePlayerDescriptor descriptor, PlayerUpdateMessage message,
      FrameBuilder builder, FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 3);
  }

}
