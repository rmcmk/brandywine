package me.ryleykimmel.brandywine.game.command;

import me.ryleykimmel.brandywine.game.event.Event;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * An Event which is listened for when a {@link Player} inputs a command.
 */
public final class CommandEvent extends Event {

  /**
   * The Player who executed this Event.
   */
  private final Player player;

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
   * @param player The Player who executed this Event.
   * @param name The name of the command.
   * @param arguments The command's arguments.
   */
  public CommandEvent(Player player, String name, CommandArguments arguments) {
    this.player = player;
    this.name = name;
    this.arguments = arguments;
  }

  /**
   * Gets the Player who executed this Event.
   *
   * @return The Player who executed this Event.
   */
  public Player getPlayer() {
    return player;
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
