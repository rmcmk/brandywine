package plugin

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.SocketChannel
import me.ryleykimmel.brandywine.Server
import me.ryleykimmel.brandywine.fs.FileSystem
import me.ryleykimmel.brandywine.game.GameService
import me.ryleykimmel.brandywine.game.auth.AuthenticationService
import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet
import me.ryleykimmel.brandywine.game.model.World
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.network.ResponseCode
import me.ryleykimmel.brandywine.network.Session
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.network.msg.Message
import org.reflections.Reflections
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.sql2o.Sql2o
import plugin.message.MessageRegistrar
import plugin.task.Tasks
import java.io.IOException
import java.io.UncheckedIOException
import kotlin.reflect.KClass

val world = World(EventConsumerChainSet())
val server = Server()
val logger: Logger = LoggerFactory.getLogger("plugin-bootstrap")

fun main(vararg args: String) = try {
    Tasks.schedule()
    MessageRegistrar.init()

    val reflections = Reflections("plugin")
    val events = reflections.getTypesAnnotatedWith(Consumes::class.java)
    events.forEach { world.addConsumer(it.newInstance() as EventConsumer<*>) }

    server.initializer(object : ChannelInitializer<SocketChannel>() {
        override fun initChannel(channel: SocketChannel) {
            val session = Session(channel)
            channel.pipeline().addLast("frame_codec", FrameCodec(session, MessageRegistrar.loginMetadata)).
                    addLast("message_codec", FrameMessageCodec(MessageRegistrar.loginMetadata)).
                    addLast("handler", SessionHandler(session))
        }
    })

    val gameService = GameService(world)
    server.setFileSystem(FileSystem.create("data/fs/")).
            setSql2o(Sql2o("jdbc:mysql://localhost/game_server", "root", "")).
            setAuthenticationStrategy({ ResponseCode.STATUS_OK }).
            registerService(gameService).registerService(AuthenticationService(gameService, server.authenticationStrategy)).
            init(43594)
} catch (cause: IOException) {
    throw UncheckedIOException(cause)
}

class SessionHandler(val session: Session) : SimpleChannelInboundHandler<Message>() {

    // Required extension method, Kotlin does not natively support fetching object.getClass()
    val Any.kt: KClass<out Any>
        get() = javaClass.kotlin

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val player = session.attr(Player.ATTRIBUTE_KEY).get()
        if (player != null) {
            server.getService(GameService::class).removePlayer(player)
        }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, message: Message) {
        val player = session.attr(Player.ATTRIBUTE_KEY).get()
        val listener = MessageRegistrar.messages[message.kt]
        listener?.handle(player ?: session, message)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.error("Uncaught exception occurred upstream", cause)
        session.close()
    }

}
