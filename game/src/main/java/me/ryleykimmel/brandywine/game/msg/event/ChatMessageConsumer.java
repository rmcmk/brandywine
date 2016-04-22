package me.ryleykimmel.brandywine.game.msg.event;

import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.ChatMessage;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;

/**
 * Handles the {@link ChatMessage}.
 */
public final class ChatMessageConsumer implements EventConsumer<ChatMessage> {

  private final Player player;

  public ChatMessageConsumer(Player player) {
    this.player = player;
  }

  @Override
  public void accept(ChatMessage message) {
    player.flagUpdate(ChatPlayerBlock.create(player, message));
  }

}
