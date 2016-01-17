package me.ryleykimmel.brandywine.network.msg.handler;

import java.util.Arrays;

import me.ryleykimmel.brandywine.common.Strings;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.command.CommandArguments;
import me.ryleykimmel.brandywine.game.command.CommandEvent;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Handles;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;
import me.ryleykimmel.brandywine.network.msg.impl.CommandMessage;

/**
 * Handles the {@link CommandMessage}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Handles(CommandMessage.class)
public final class CommandMessageHandler implements MessageHandler<CommandMessage> {

  @Override
  public void handle(GameSession session, CommandMessage message) {
    Player player = session.attr().get();
    GameService service = session.getContext().getService(GameService.class);

    if (player != null) {
      String[] components = message.getCommand().split(" ");
      String name = components[0];

      String[] filtered = Arrays.copyOfRange(components, 1, components.length);
      String[] arguments = Strings.split(String.join(" ", filtered), '"');

      service.getWorld().notify(new CommandEvent(player, name, new CommandArguments(arguments)));
    }
  }

}
