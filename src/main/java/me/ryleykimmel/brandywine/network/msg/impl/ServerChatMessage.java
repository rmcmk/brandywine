package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which sends the specified chat message.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ServerChatMessage implements Message {

  /**
   * The chat message.
   */
  private final String message;

  /**
   * Constructs a new {@link ServerChatMessage} with the specified message.
   *
   * @param message The chat message.
   * @param args The arguments referenced from the formatter.
   */
  public ServerChatMessage(String message, Object... args) {
    this.message = Strings.format(message, args);
  }

  /**
   * Gets the chat message.
   *
   * @return The chat message.
   */
  public String getMessage() {
    return message;
  }

}
