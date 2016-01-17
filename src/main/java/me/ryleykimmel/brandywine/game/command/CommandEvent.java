package me.ryleykimmel.brandywine.game.command;

import me.ryleykimmel.brandywine.game.event.PlayerEvent;
import me.ryleykimmel.brandywine.game.model.player.Player;

public final class CommandEvent extends PlayerEvent {

  private final String name;
  private final CommandArguments arguments;

  public CommandEvent(Player player, String name, CommandArguments arguments) {
    super(player);
    this.name = name;
    this.arguments = arguments;
  }

  public String getName() {
    return name;
  }

  public CommandArguments getArguments() {
    return arguments;
  }

}
