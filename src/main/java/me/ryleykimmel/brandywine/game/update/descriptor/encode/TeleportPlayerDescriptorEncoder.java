package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.update.DescriptorEncoder.PlayerDescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.TeleportPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

public final class TeleportPlayerDescriptorEncoder implements PlayerDescriptorEncoder<TeleportPlayerDescriptor> {

	@Override
	public void encode(TeleportPlayerDescriptor descriptor, PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(1, 1);
		builder.putBits(2, 3);

		Position position = message.getPosition();
		builder.putBits(2, position.getHeight());
		builder.putBits(1, descriptor.hasMapRegionChanged() ? 0 : 1);
		builder.putBits(1, descriptor.isBlockUpdatedRequired() ? 1 : 0);
		builder.putBits(7, position.getLocalY(message.getLastKnownRegion()));
		builder.putBits(7, position.getLocalX(message.getLastKnownRegion()));		
	}

}