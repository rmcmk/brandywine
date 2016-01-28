package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.TeleportPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

public final class TeleportPlayerDescriptorEncoder
    implements DescriptorEncoder<TeleportPlayerDescriptor> {

  @Override
  public void encode(TeleportPlayerDescriptor descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    builder.putBits(1, 1);
    builder.putBits(2, 3);

    Position position = descriptor.getPosition();
    builder.putBits(2, position.getHeight());
    
    builder.putBits(1, descriptor.hasMapRegionChanged() ? 0 : 1);
    builder.putBits(1, descriptor.isBlockUpdatedRequired() ? 1 : 0);
    
    Position lastKnownRegion = descriptor.getLastKnownRegion();
    builder.putBits(7, position.getLocalY(lastKnownRegion));
    builder.putBits(7, position.getLocalX(lastKnownRegion));
  }

}
