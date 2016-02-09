package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which performs a command-action.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class CommandMessage implements Message {

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
