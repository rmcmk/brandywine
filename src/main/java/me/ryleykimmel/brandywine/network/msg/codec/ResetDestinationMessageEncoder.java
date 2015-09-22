package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.ResetDestinationMessage;

/**
 * Encodes the ResetDestinationMessage.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Encodes(ResetDestinationMessage.class)
public class ResetDestinationMessageEncoder implements MessageEncoder<ResetDestinationMessage> {

	@Override
	public Frame encode(ResetDestinationMessage message) {
		return new Frame(78);
	}

}