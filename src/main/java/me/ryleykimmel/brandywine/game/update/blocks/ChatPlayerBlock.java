package me.ryleykimmel.brandywine.game.update.blocks;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerBlock;
import me.ryleykimmel.brandywine.network.game.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.game.frame.DataType;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * Encodes a Player's chat block.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public class ChatPlayerBlock extends PlayerBlock {

	/**
	 * The ChatMessage to encode.
	 */
	private final ChatMessage chatMessage;

	/**
	 * Constructs a new {@link ChatPlayerBlock} with the specified Player.
	 * 
	 * @param player The Player we are updating chat for.
	 */
	public ChatPlayerBlock(Player player) {
		super(player, 0x80);
		chatMessage = player.getChatMessage();
	}

	@Override
	public void encode(PlayerUpdateMessage message, FrameBuilder builder) {
		byte[] bytes = chatMessage.getCompressedMessage();

		builder.put(DataType.BYTE, chatMessage.getTextEffects());
		builder.put(DataType.BYTE, chatMessage.getTextColor());
		builder.put(DataType.BYTE, 0); // TODO: Privileges!
		builder.put(DataType.BYTE, DataTransformation.NEGATE, bytes.length);
		builder.putBytesReverse(bytes);
	}

}