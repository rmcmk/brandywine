package me.ryleykimmel.brandywine.game.message.listener;

import me.ryleykimmel.brandywine.game.message.ChatMessage;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;

/**
 * Listener for the {@link ChatMessage}.
 */
public final class ChatMessageListener implements GameMessageListener<ChatMessage> {

  @Override
  public void handle(Player player, ChatMessage message) {
    player.flagUpdate(ChatPlayerBlock.create(player, message));
  }

}
