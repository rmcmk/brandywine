package me.ryleykimmel.brandywine.game.message.listener;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.command.CommandArguments;
import me.ryleykimmel.brandywine.game.command.CommandEvent;
import me.ryleykimmel.brandywine.game.message.CommandMessage;
import me.ryleykimmel.brandywine.game.model.player.Player;

import java.util.Arrays;

/**
 * Listener for the {@link CommandMessage}.
 */
public final class CommandMessageListener implements GameMessageListener<CommandMessage> {

    @Override
    public void handle(Player player, CommandMessage message) {
        String[] components = message.getCommand().split(" ");
        String name = components[0];

        String[] filtered = Arrays.copyOfRange(components, 1, components.length);
        String[] arguments = Strings.split(String.join(" ", filtered), '"');

        player.getWorld().notify(new CommandEvent(player, name, new CommandArguments(arguments)));
    }

}