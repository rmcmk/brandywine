package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.msg.Decodes;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.DummyMessage;

/**
 * A {@link MessageDecoder} which decodes dummy messages.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
// TODO: Find a way to rid of these magic-numbers
@Decodes({ 0, 36, 77, 78, 85, 121, 136, 148, 150, 152, 165, 183, 189, 200, 210, 226, 230, 238, 246 })
public final class DummyMessageDecoder implements MessageDecoder<DummyMessage> {

	/**
	 * An instance of a {@link DummyMessage}.
	 */
	private static final DummyMessage INSTANCE = new DummyMessage();

	@Override
	public DummyMessage decode(Frame frame) {
		return INSTANCE;
	}

}