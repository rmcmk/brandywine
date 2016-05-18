package plugin.command

import me.ryleykimmel.brandywine.game.command.CommandEvent
import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import plugin.message
import plugin.teleport

@Consumes(CommandEvent::class)
class CommandEventConsumer : EventConsumer<CommandEvent> {

    override fun accept(event: CommandEvent) {
        val player = event.mob
        val args = event.arguments

        when (event.name) {
            "pos" -> {
                player.message("You are standing at: %s", player.position);
            }

            "tele-to" -> {
                if (!args.hasRemaining(2)) {
                    player.message(
                            "There are 2 required arguments: ::tele-to [x, y, optional-height]")
                    return
                }

                try {
                    val x = args.nextInteger
                    val y = args.nextInteger
                    val height = if (args.hasRemaining(1)) args.nextInteger else player.position.height

                    player.teleport(x, y, height)
                } catch (cause: NumberFormatException) {
                    player.message("The arguments for this command may only be numeric.")
                }
            }
        }
    }

}
