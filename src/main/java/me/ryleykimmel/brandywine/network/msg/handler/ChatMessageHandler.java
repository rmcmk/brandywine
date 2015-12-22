package me.ryleykimmel.brandywine.network.msg.handler;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Handles;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;

/**
 * Handles the {@link ChatMessage}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Handles(ChatMessage.class)
public final class ChatMessageHandler implements MessageHandler<ChatMessage> {

	@Override
	public void handle(GameSession session, ChatMessage message) {
		Player player = session.attr().get();
		player.flagUpdate(ChatPlayerBlock.create(player, message));
	}

}