package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.update.DescriptorEncoder.PlayerDescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.IdlePlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

public final class IdlePlayerDescriptorEncoder
    implements PlayerDescriptorEncoder<IdlePlayerDescriptor> {

  @Override
  public void encode(IdlePlayerDescriptor descriptor, PlayerUpdateMessage message,
      FrameBuilder builder, FrameBuilder blockBuilder) {
    if (descriptor.isBlockUpdatedRequired()) {
      builder.putBits(1, 1);
      builder.putBits(2, 0);
    } else {
      builder.putBits(1, 0);
    }
  }

}
