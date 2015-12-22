package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

@FunctionalInterface
public interface UpdateBlockEncoder<M extends Message, B extends UpdateBlock> {

	void encode(B block, M message, FrameBuilder builder);

	@FunctionalInterface
	public static interface PlayerUpdateBlockEncoder<B extends UpdateBlock> extends UpdateBlockEncoder<PlayerUpdateMessage, B> {
	}

}