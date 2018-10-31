package me.ryleykimmel.brandywine.game.message;

import me.ryleykimmel.brandywine.network.message.Message;

/**
 * A {@link Message} which performs a command-action.
 */
public final class CommandMessage extends Message {

  /**
   * The command.
   */
  private final String command;

  /**
   * Constructs a new {@link CommandMessage} with the specified command.
   *
   * @param command The command.
   */
  public CommandMessage(String command) {
    this.command = command;
  }

  /**
   * Gets the command.
   *
   * @return The command.
   */
  public String getCommand() {
    return command;
  }

}
