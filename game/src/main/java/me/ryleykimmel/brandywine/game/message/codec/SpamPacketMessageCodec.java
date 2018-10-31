package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.SpamPacketMessage;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link SpamPacketMessage}.
 */
public final class SpamPacketMessageCodec extends MessageCodec<SpamPacketMessage> {

	@Override
	public SpamPacketMessage decode(FrameReader frame) {
		return new SpamPacketMessage();
	}

}