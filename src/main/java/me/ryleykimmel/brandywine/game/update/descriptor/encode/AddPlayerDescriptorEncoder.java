package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.AddPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

public final class AddPlayerDescriptorEncoder implements DescriptorEncoder<AddPlayerDescriptor> {

  @Override
  public void encode(AddPlayerDescriptor descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    builder.putBits(11, descriptor.getIndex());
    builder.putBits(1, descriptor.isBlockUpdatedRequired() ? 1 : 0);
    builder.putBits(1, 1);

    Position position = descriptor.getPosition();
    Position otherPosition = descriptor.getOtherPosition();
    
    builder.putBits(5, position.getDeltaY(otherPosition));
    builder.putBits(5, position.getDeltaX(otherPosition));
  }

}
