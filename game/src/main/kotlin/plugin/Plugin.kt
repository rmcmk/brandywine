package plugin

import me.ryleykimmel.brandywine.game.model.*
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.msg.ServerChatMessage
import me.ryleykimmel.brandywine.server.Server
import me.ryleykimmel.brandywine.service.Service
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Stream
import kotlin.reflect.KClass

// val tasks = Tasks()

val random = ThreadLocalRandom.current()

fun random(min: Int, max: Int): Int = random.nextInt(max - min) + min
fun random(range: Int): Int = random.nextInt(range)

fun Player.message(message: String, vararg objects: Any) = this.write(ServerChatMessage(message, *objects))
fun Player.teleport(x: Int, y: Int, height: Int = this.position.height) = this.teleport(Position(x, y, height))

fun Player.region() = world.regionRepository.get(this.position.regionCoordinates)
fun <T : Entity> Player.surrounding(type: EntityType): Stream<T> = this.region().getEntities<T>(type)

fun World.each(type: EntityType, action: (Int, Mob) -> Unit) {
    val collection = when (type) {
        EntityType.NPC -> world.npcs
        EntityType.PLAYER -> world.players
    }

    collection.filterNotNull().forEachIndexed(action)
}

fun <T : Service> Server.getService(clazz: KClass<T>) = this.getService(clazz.java)
