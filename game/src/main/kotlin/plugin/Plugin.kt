package plugin

import me.ryleykimmel.brandywine.game.model.Position
import me.ryleykimmel.brandywine.game.model.World
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.msg.ServerChatMessage
import java.util.concurrent.ThreadLocalRandom

val random = ThreadLocalRandom.current()

fun random(min: Int, max: Int): Int = random.nextInt(max - min) + min
fun random(range: Int): Int = random.nextInt(range)

fun Player.message(message: String, vararg objects: Any) = this.write(ServerChatMessage(message, objects))
fun Player.teleport(x: Int, y: Int, height: Int = this.position.height) = this.teleport(Position(x, y, height))

fun World.each(action: (Int, Player) -> Unit) = this.players.filterNotNull().forEachIndexed(action)