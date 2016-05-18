package plugin

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import me.ryleykimmel.brandywine.InitializationException
import me.ryleykimmel.brandywine.fs.FileSystem
import me.ryleykimmel.brandywine.game.GameService
import me.ryleykimmel.brandywine.game.GameSession
import me.ryleykimmel.brandywine.game.GameSessionHandler
import me.ryleykimmel.brandywine.game.auth.AuthenticationService
import me.ryleykimmel.brandywine.game.auth.impl.SqlAuthenticationStrategy
import me.ryleykimmel.brandywine.game.event.EventConsumer
import me.ryleykimmel.brandywine.game.model.World
import me.ryleykimmel.brandywine.game.msg.LoginHandshakeMessage
import me.ryleykimmel.brandywine.network.frame.FrameMapping
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.network.msg.Message
import me.ryleykimmel.brandywine.network.msg.MessageCodec
import me.ryleykimmel.brandywine.server.Server
import org.reflections.Reflections
import org.slf4j.LoggerFactory
import org.sql2o.Sql2o
import java.io.IOException
import kotlin.reflect.KClass

val logger = LoggerFactory.getLogger("plugin-bootstrap")
val world = World()

fun main(args: Array<String>) = try {
    val server = Server()
    val metadata = createFrameMetadataSet()

    server.initializer(object : ChannelInitializer<SocketChannel>() {
        override fun initChannel(channel: SocketChannel) {
            val session = GameSession(channel, world.events)

            channel.pipeline().addLast("frame_codec", FrameCodec(session, metadata)).
                    addLast("message_codec", FrameMessageCodec(metadata)).
                    addLast("handler", GameSessionHandler(session))
        }
    })

    val gameService = GameService(world)
    server.setFileSystem(FileSystem.create("data/fs/")).
            setSql2o(Sql2o("jdbc:mysql://localhost/game_server", "root", "")).
            setAuthenticationStrategy(SqlAuthenticationStrategy(server.sql2o)).
            registerService(gameService).registerService(AuthenticationService(gameService, server.authenticationStrategy)).
            init(43594)

    loadPluginEvents()
} catch (cause: IOException) {
    throw InitializationException(cause)
}

/**
 * Loads Kotlin plugin events.
 */
fun loadPluginEvents() {
    val reflections = Reflections("plugin")
    val classes = reflections.getSubTypesOf(EventConsumer::class.java)
    classes.forEach { world.addConsumer(it.newInstance()) }
    logger.info("Loaded {} Kotlin plugin event" + if (classes.size == 1) "." else "s.", classes.size)
}

/**
 * Creates a new [FrameMetadataSet].

 * @return The new FrameMetadataSet, never `null`.
 */
fun createFrameMetadataSet(): FrameMetadataSet {
    val metadata = FrameMetadataSet()

    metadata.register(LoginHandshakeMessage::class, 14, 1)

    return metadata
}

@Suppress("unchecked_cast")
fun <T : Message> FrameMetadataSet.register(message: KClass<T>, opcode: Int, length: Int) {
    val packageName = message.java.`package`.name
    val codec = Class.forName("$packageName.codec.${message.simpleName}Codec").newInstance() as MessageCodec<T>
    this.register(FrameMapping.create(message.java, codec, opcode, length))
}