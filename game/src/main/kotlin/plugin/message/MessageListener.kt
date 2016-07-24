package plugin.message

import me.ryleykimmel.brandywine.network.msg.Message

interface MessageListener<in T, in M : Message> {

    fun handle(source: T, message: M)

}
