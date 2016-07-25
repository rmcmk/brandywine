package plugin.task

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Tasks {

    const val INTERVAL = 600L

    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val tasks = ArrayList<Task>()

    fun schedule() = executor.scheduleAtFixedRate({
        try {
            tick()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }, INTERVAL, INTERVAL, TimeUnit.MILLISECONDS)

    fun execute(runnable: Runnable) = executor.execute(runnable)

    fun tick() {
        val iterator = tasks.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.finish()) iterator.remove()
        }
    }

    operator fun plusAssign(task: Task) {
        tasks.add(task)
    }

}

operator fun Task.unaryPlus() {
    Tasks += this
}

inline fun delayed(ticks: Int = 1, crossinline body: () -> Any) = object : TickTask(ticks) {
    override fun run() {
        body()
        stop()
    }
}

inline fun repeating(ticks: Int = 1, crossinline body: () -> Boolean) = object : TickTask(ticks) {
    override fun run() {
        if (body()) stop()
    }
}

inline fun continuous(ticks: Int = 1, crossinline body: () -> Any) = repeating(ticks) { body(); false }
