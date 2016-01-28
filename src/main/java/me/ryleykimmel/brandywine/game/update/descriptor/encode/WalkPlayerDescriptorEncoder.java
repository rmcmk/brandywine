package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.WalkPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

public final class WalkPlayerDescriptorEncoder
    implements DescriptorEncoder<WalkPlayerDescriptor> {

  @Override
  public void encode(WalkPlayerDescriptor descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 1);
    builder.putBits(3, descriptor.getDirection().getValue());
    builder.putBits(1, descriptor.isBlockUpdatedRequired() ? 1 : 0);
  }

}
