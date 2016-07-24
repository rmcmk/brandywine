package plugin.message

import me.ryleykimmel.brandywine.common.Strings
import me.ryleykimmel.brandywine.game.command.CommandArguments
import me.ryleykimmel.brandywine.game.command.CommandEvent
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.msg.CommandMessage
import plugin.world
import java.util.*

class CommandMessageListener : MessageListener<Player, CommandMessage> {

    override fun handle(source: Player, message: CommandMessage) {
        val components = message.command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val name = components[0]

        val filtered = Arrays.copyOfRange(components, 1, components.size)
        val arguments = Strings.split(filtered.joinToString(" "), '"')

        world.notify(CommandEvent(source, name, CommandArguments(arguments)))
    }

}
