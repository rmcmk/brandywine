package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

@FunctionalInterface
public interface DescriptorEncoder<T extends Message, D extends Descriptor<? extends Mob, T>> {

  void encode(D descriptor, T message, FrameBuilder builder, FrameBuilder blockBuilder);

  @FunctionalInterface
  public static interface PlayerDescriptorEncoder<D extends Descriptor<? extends Mob, PlayerUpdateMessage>>
      extends DescriptorEncoder<PlayerUpdateMessage, D> {
  }

}
