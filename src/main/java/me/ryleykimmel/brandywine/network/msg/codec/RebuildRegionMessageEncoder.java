package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.RebuildRegionMessage;

/**
 * Encodes the {@link RebuildRegionMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Encodes(RebuildRegionMessage.class)
public final class RebuildRegionMessageEncoder implements MessageEncoder<RebuildRegionMessage> {

	@Override
	public Frame encode(RebuildRegionMessage message) {
		FrameBuilder builder = new FrameBuilder(73);
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());
		builder.put(DataType.SHORT, message.getPosition().getCentralRegionY());
		return builder.build();
	}

}