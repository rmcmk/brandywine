package me.ryleykimmel.brandywine.game.command;

import me.ryleykimmel.brandywine.game.event.EventListener;

public final class CommandEventListener implements EventListener<CommandEvent> {

  @Override
  public void handle(CommandEvent event) {
    CommandArguments args = event.getArguments();

    switch (event.getName()) {
      case "ban":
        if (!args.remainingExact(3)) {
          // notify too many/too little arguments
          return;
        }

        String name = args.getNext();
        int duration = args.getNextInteger();
        String reason = args.getNext();
        // ...
        break;
    }
  }

}
