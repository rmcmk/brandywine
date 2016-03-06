package me.ryleykimmel.brandywine.network.msg.handler;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.msg.PlayerMessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;

/**
 * Handles the {@link ChatMessage}.
 */
public final class ChatMessageHandler implements PlayerMessageHandler<ChatMessage> {

  @Override
  public void handle(Player player, ChatMessage message) {
    player.flagUpdate(ChatPlayerBlock.create(player, message));
  }

}
