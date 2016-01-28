package me.ryleykimmel.brandywine.game.update.blocks.encode;

import java.util.HashMap;
import java.util.Map;

import me.ryleykimmel.brandywine.game.update.UpdateBlock;
import me.ryleykimmel.brandywine.game.update.UpdateBlockEncoder;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Message;

public final class UpdateBlockEncoderSet {

  private final Map<Class<? extends UpdateBlock>, UpdateBlockEncoder<? extends UpdateBlock>> blocks =
      new HashMap<>();

  public UpdateBlockEncoderSet() {
    blocks.put(ChatPlayerBlock.class, new ChatPlayerBlockEncoder());
    blocks.put(AppearancePlayerBlock.class, new AppearancePlayerBlockEncoder());
  }

  @SuppressWarnings("unchecked")
  public <B extends UpdateBlock> void encode(B block,
      FrameBuilder builder) {
    UpdateBlockEncoder<B> encoder = (UpdateBlockEncoder<B>) blocks.get(block.getClass());
    encoder.encode(block, builder);
  }

}
