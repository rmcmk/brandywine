package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * Encodes the {@link PlayerUpdateMessage}.
 */
public final class PlayerUpdateMessageEncoder implements MessageEncoder<PlayerUpdateMessage> {

  @Override
  public void encode(PlayerUpdateMessage message, FrameBuilder builder) {
    FrameBuilder blockBuilder = new FrameBuilder(builder.allocator());
    builder.switchToBitAccess();

    message.getDescriptor().encode(builder, blockBuilder);
    builder.putBits(8, message.getLocalPlayerCount());

    for (PlayerDescriptor descriptor : message.getDescriptors()) {
      descriptor.encode(builder, blockBuilder);
    }

    if (blockBuilder.getLength() > 0) {
      builder.putBits(11, 2047);
      builder.switchToByteAccess();
      builder.putBytes(blockBuilder);
    } else {
      builder.switchToByteAccess();
    }
  }

}
