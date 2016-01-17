package me.ryleykimmel.brandywine.game.command;

import me.ryleykimmel.brandywine.game.event.PlayerEvent;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * A PlayerEvent which is listened for when a {@link Player} inputs a command.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class CommandEvent extends PlayerEvent {

  /**
   * The name of the command.
   */
  private final String name;

  /**
   * The command's arguments.
   */
  private final CommandArguments arguments;

  /**
   * Constructs a new {@link CommandEvent} with the specified Player, name and CommandArguments.
   * 
   * @param player The Player who is listening for commands.
   * @param name The name of the command.
   * @param arguments The command's arguments.
   */
  public CommandEvent(Player player, String name, CommandArguments arguments) {
    super(player);
    this.name = name;
    this.arguments = arguments;
  }

  /**
   * Gets the name of the command.
   * 
   * @return The name of the command.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the arguments for the command.
   * 
   * @return The arguments for the command.
   */
  public CommandArguments getArguments() {
    return arguments;
  }

}
