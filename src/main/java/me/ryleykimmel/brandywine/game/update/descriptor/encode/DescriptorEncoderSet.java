package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import java.util.HashMap;
import java.util.Map;

import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.update.Descriptor;
import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.AddPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.IdlePlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.RemovePlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.RunPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.TeleportPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.WalkPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Message;

public final class DescriptorEncoderSet {

  private final Map<Class<? extends Descriptor<? extends Mob>>, DescriptorEncoder<? extends Descriptor<? extends Mob>>> descriptors =
      new HashMap<>();

  public DescriptorEncoderSet() {
    descriptors.put(AddPlayerDescriptor.class, new AddPlayerDescriptorEncoder());
    descriptors.put(IdlePlayerDescriptor.class, new IdlePlayerDescriptorEncoder());
    descriptors.put(RemovePlayerDescriptor.class, new RemovePlayerDescriptorEncoder());
    descriptors.put(RunPlayerDescriptor.class, new RunPlayerDescriptorEncoder());
    descriptors.put(WalkPlayerDescriptor.class, new WalkPlayerDescriptorEncoder());
    descriptors.put(TeleportPlayerDescriptor.class, new TeleportPlayerDescriptorEncoder());
  }

  @SuppressWarnings("unchecked")
  public <D extends Descriptor<? extends Mob>> void encode(D descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    DescriptorEncoder<D> encoder = (DescriptorEncoder<D>) descriptors.get(descriptor.getClass());
    encoder.encode(descriptor, builder, blockBuilder);
  }

}
