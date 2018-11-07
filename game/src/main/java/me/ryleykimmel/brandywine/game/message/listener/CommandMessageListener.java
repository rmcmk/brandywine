package me.ryleykimmel.brandywine.game.message.listener;

import java.util.Arrays;
import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.command.CommandArguments;
import me.ryleykimmel.brandywine.game.command.CommandEvent;
import me.ryleykimmel.brandywine.game.message.CommandMessage;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Listener for the {@link CommandMessage}.
 */
public final class CommandMessageListener extends GameMessageListener<CommandMessage> {

  /**
   * Constructs a new {@link CommandMessageListener}.
   *
   * @param world The World listening to this {@link CommandMessage}.
   */
  public CommandMessageListener(World world) {
    super(world);
  }

  @Override
  public void handle(Player player, CommandMessage message) {
    String[] components = message.getCommand().split(" ");
    String name = components[0];

    String[] filtered = Arrays.copyOfRange(components, 1, components.length);
    String[] arguments = Strings.split(String.join(" ", filtered), '"');

    world.notify(new CommandEvent(player, name, new CommandArguments(arguments)));
  }

}