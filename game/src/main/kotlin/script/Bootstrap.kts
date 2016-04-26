package script

import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import org.reflections.Reflections
import java.util.concurrent.ThreadLocalRandom

val random = ThreadLocalRandom.current()

fun random(min: Int, max: Int): Int = random.nextInt(max - min) + min
fun random(range: Int): Int = random.nextInt(range)

fun main(args: Array<String>) {
    val reflections = Reflections("script")
    val classes = reflections.getSubTypesOf(EventConsumer::class.java)
    classes.filter { it.isAnnotationPresent(Consumes::class.java) }.forEach { }
}