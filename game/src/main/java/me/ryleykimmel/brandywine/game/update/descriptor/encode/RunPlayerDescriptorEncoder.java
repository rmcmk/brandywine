package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.RunPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

public final class RunPlayerDescriptorEncoder implements DescriptorEncoder<RunPlayerDescriptor> {

  @Override
  public void encode(RunPlayerDescriptor descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 2);
    builder.putBits(3, descriptor.getFirstDirection().getValue());
    builder.putBits(3, descriptor.getSecondDirection().getValue());
    builder.putBits(1, descriptor.isBlockUpdatedRequired() ? 1 : 0);
  }

}
