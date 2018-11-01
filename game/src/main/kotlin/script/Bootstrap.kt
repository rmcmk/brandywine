package script

import me.ryleykimmel.brandywine.game.message.ServerChatMessage
import me.ryleykimmel.brandywine.game.model.*
import me.ryleykimmel.brandywine.game.model.player.Player
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Stream

val random = ThreadLocalRandom.current()!!

fun random(min: Int, max: Int): Int = random.nextInt(max - min) + min
fun random(range: Int): Int = random.nextInt(range)

fun Player.message(message: String, vararg objects: Any) = this.write(ServerChatMessage(message, *objects))
fun Player.teleport(x: Int, y: Int, height: Int = this.position.height) = this.teleport(Position(x, y, height))

fun Player.region() = world.regionRepository.get(this.position.regionCoordinates)!!
fun <T : Entity> Player.surrounding(type: EntityType): Stream<T> = this.region().getEntities<T>(type)

inline fun World.each(type: EntityType, crossinline action: (Int, Mob) -> Unit) {
    val collection = when (type) {
        EntityType.NPC -> this.npcs
        EntityType.PLAYER -> this.players
    }

    collection.filterNotNull().forEachIndexed(action)
}
