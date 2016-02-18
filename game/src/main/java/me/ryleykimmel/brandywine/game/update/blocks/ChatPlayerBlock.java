package me.ryleykimmel.brandywine.game.update.blocks;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.UpdateBlock;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;

/**
 * Encodes a Player's chat block.
 */
public class ChatPlayerBlock extends UpdateBlock {

  public static ChatPlayerBlock create(Player player, ChatMessage message) {
    return new ChatPlayerBlock(player.getPrivileges().getPrimaryId(), message);
  }

  private static final int MASK = 0x80;

  private final int privilegeId;
  private final ChatMessage chatMessage;

  public ChatPlayerBlock(int privilegeId, ChatMessage chatMessage) {
    super(MASK);
    this.privilegeId = privilegeId;
    this.chatMessage = chatMessage;
  }

  public ChatMessage getChatMessage() {
    return chatMessage;
  }

  public int getPrivilegeId() {
    return privilegeId;
  }

}
