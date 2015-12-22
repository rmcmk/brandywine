package me.ryleykimmel.brandywine.game.update.descriptor.encode;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.update.DescriptorEncoder.PlayerDescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.descriptor.AddPlayerDescriptor;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

public final class AddPlayerDescriptorEncoder implements PlayerDescriptorEncoder<AddPlayerDescriptor> {
	
	@Override
	public void encode(AddPlayerDescriptor descriptor, PlayerUpdateMessage message, FrameBuilder builder, FrameBuilder blockBuilder) {
		builder.putBits(11, descriptor.getIndex());
		builder.putBits(1, descriptor.isBlockUpdatedRequired() ? 1 : 0);
		builder.putBits(1, 1);

		Position position = descriptor.getPosition();
		builder.putBits(5, position.getY() - message.getPosition().getY());
		builder.putBits(5, position.getX() - message.getPosition().getX());
	}

}