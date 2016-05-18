package plugin.command

import me.ryleykimmel.brandywine.game.command.CommandEvent
import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import me.ryleykimmel.brandywine.game.model.EntityType
import me.ryleykimmel.brandywine.game.model.player.Player
import plugin.message
import plugin.surrounding
import plugin.teleport
import plugin.world

@Consumes(CommandEvent::class)
class CommandEventConsumer : EventConsumer<CommandEvent> {

    override fun accept(event: CommandEvent) {
        val player = event.mob
        val args = event.arguments

        when (event.name) {
            "pos" -> player.message("You are standing at: %s", player.position);
            "print-surrounding" -> player.surrounding<Player>(EntityType.PLAYER).forEach { player.message("Player in your region: %s", it.username) }

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
