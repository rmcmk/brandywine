package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

@FunctionalInterface
public interface UpdateBlockEncoder<B extends UpdateBlock> {

  void encode(B block, FrameBuilder builder);

}
