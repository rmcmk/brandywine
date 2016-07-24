package plugin.message

import me.ryleykimmel.brandywine.game.model.Position
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.msg.MovementMessage

class MovementMessageListener : MessageListener<Player, MovementMessage> {

    override fun handle(source: Player, message: MovementMessage) {
        val steps = message.steps

        with(source) {
            val step: Position? = steps.poll() ?: return

            movementQueue.addFirstStep(step)

            while (steps.isNotEmpty())
                movementQueue.addStep(steps.poll())

            movementQueue.setRunning(message.isRunning || isRunning)
        }
    }

}
