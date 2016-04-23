package me.ryleykimmel.brandywine.game.msg.event;

import java.util.Arrays;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.command.CommandArguments;
import me.ryleykimmel.brandywine.game.command.CommandEvent;
import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.CommandMessage;

/**
 * Handles the {@link CommandMessage}.
 */
public final class CommandMessageConsumer implements EventConsumer<CommandMessage> {

  private final Player player;

  public CommandMessageConsumer(Player player) {
    this.player = player;
  }

  @Override
  public void accept(CommandMessage message) {
    String[] components = message.getCommand().split(" ");
    String name = components[0];

    String[] filtered = Arrays.copyOfRange(components, 1, components.length);
    String[] arguments = Strings.split(String.join(" ", filtered), '"');

    player.notify(new CommandEvent(player, name, new CommandArguments(arguments)));
  }

}
