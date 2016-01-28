package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.RemovePlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

public final class RemovePlayerDescriptorEncoder
    implements DescriptorEncoder<RemovePlayerDescriptor> {

  @Override
  public void encode(RemovePlayerDescriptor descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 3);
  }

}
