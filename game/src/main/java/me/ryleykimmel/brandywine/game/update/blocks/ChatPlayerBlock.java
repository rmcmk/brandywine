package me.ryleykimmel.brandywine.game.update.blocks;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.ChatMessage;
import me.ryleykimmel.brandywine.game.update.UpdateBlock;
import me.ryleykimmel.brandywine.network.frame.DataTransformation;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * Encodes a Player's chat block.
 */
public class ChatPlayerBlock extends UpdateBlock {

  /**
   * The mask of this UpdateBlock.
   */
  private static final int MASK = 0x80;

  /**
   * The Player's privilege id.
   */
  private final int privilegeId;

  /**
   * The Player's chat message.
   */
  private final ChatMessage chatMessage;

  /**
   * Constructs a new ChatPlayerBlock.
   *
   * @param privilegeId The Player's privilege id.
   * @param chatMessage The Player's chat message.
   */
  public ChatPlayerBlock(int privilegeId, ChatMessage chatMessage) {
    super(MASK);
    this.privilegeId = privilegeId;
    this.chatMessage = chatMessage;
  }

  /**
   * Creates a new {@link ChatPlayerBlock} from the specified Player.
   *
   * @param player The Player to create an ChatPlayerBlock from.
   * @param message The Player's chat message.
   * @return A new ChatPlayerBlock, never {@code null}.
   */
  public static ChatPlayerBlock create(Player player, ChatMessage message) {
    return new ChatPlayerBlock(player.getPrivileges().getCrownId(), message);
  }

  @Override
  public void encode(FrameBuilder builder) {
    builder.put(DataType.BYTE, chatMessage.getTextEffects());
    builder.put(DataType.BYTE, chatMessage.getTextColor());
    builder.put(DataType.BYTE, privilegeId);

    byte[] bytes = chatMessage.getCompressedMessage();
    builder.put(DataType.BYTE, DataTransformation.NEGATE, bytes.length);
    builder.putBytesReverse(bytes);
  }

}
