package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeResponseMessage;

/**
 * Encodes the {@link LoginHandshakeResponseMessage}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Encodes(LoginHandshakeResponseMessage.class)
public final class LoginHandshakeResponseMessageEncoder implements MessageEncoder<LoginHandshakeResponseMessage> {

	@Override
	public Frame encode(LoginHandshakeResponseMessage message) {
		FrameBuilder builder = new FrameBuilder();
		builder.put(DataType.BYTE, message.getStatus());
		builder.putBytes(message.getDummy());
		builder.put(DataType.LONG, message.getSessionKey());
		return builder.build();
	}

}