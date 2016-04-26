package me.ryleykimmel.brandywine.game.command;

import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.ServerChatMessage;

/**
 * Listens for CommandEvent's.
 */
public final class CommandEventConsumer implements EventConsumer<CommandEvent> {

  @Override
  public void accept(CommandEvent event) {
    Player player = event.getMob();
    CommandArguments args = event.getArguments();

    switch (event.getName()) {
      case "pos":
        player.write(new ServerChatMessage("You are standing at: %s", player.getPosition()));
        return;

      case "tele-to":
        if (!args.hasRemaining(2)) {
          player.write(new ServerChatMessage(
              "There are 2 required arguments: ::tele-to [x, y, optional-height]"));
          return;
        }

        try {
          int x = args.getNextInteger();
          int y = args.getNextInteger();
          int height =
              args.hasRemaining(1) ? args.getNextInteger() : player.getPosition().getHeight();

          player.teleport(new Position(x, y, height));
        } catch (NumberFormatException cause) {
          player
              .write(new ServerChatMessage("The arguments for this command may only be numeric."));
        }

        return;
    }

  }

}