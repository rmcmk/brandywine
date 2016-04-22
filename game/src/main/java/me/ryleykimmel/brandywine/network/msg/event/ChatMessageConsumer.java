package me.ryleykimmel.brandywine.network.msg.event;

import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;

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
