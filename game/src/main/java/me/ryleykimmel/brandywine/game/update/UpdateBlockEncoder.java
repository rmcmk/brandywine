package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;

@FunctionalInterface
public interface UpdateBlockEncoder<B extends UpdateBlock> {

  void encode(B block, FrameBuilder builder);

}
