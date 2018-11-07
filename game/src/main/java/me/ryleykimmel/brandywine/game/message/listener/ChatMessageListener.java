package me.ryleykimmel.brandywine.game.message.listener;

import me.ryleykimmel.brandywine.game.message.ChatMessage;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;

/**
 * Listener for the {@link ChatMessage}.
 */
public final class ChatMessageListener extends GameMessageListener<ChatMessage> {

  /**
   * Constructs a new {@link ChatMessageListener}.
   *
   * @param world The World listening to this {@link ChatMessage}.
   */
  public ChatMessageListener(World world) {
    super(world);
  }

  @Override
  public void handle(Player player, ChatMessage message) {
    player.flagUpdate(ChatPlayerBlock.create(player, message));
  }

}
