package me.ryleykimmel.brandywine.game.message;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.network.message.Message;

/**
 * A {@link Message} which sends the specified chat message.
 */
public final class ServerChatMessage extends Message {

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
