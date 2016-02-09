package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

@FunctionalInterface
public interface DescriptorEncoder<D extends Descriptor<? extends Mob>> {

  void encode(D descriptor, FrameBuilder builder, FrameBuilder blockBuilder);

}
