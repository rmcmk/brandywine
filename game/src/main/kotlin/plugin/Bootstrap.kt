package plugin

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.SocketChannel
import me.ryleykimmel.brandywine.InitializationException
import me.ryleykimmel.brandywine.fs.FileSystem
import me.ryleykimmel.brandywine.game.GameService
import me.ryleykimmel.brandywine.game.auth.AuthenticationService
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet
import me.ryleykimmel.brandywine.game.model.World
import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.msg.*
import me.ryleykimmel.brandywine.network.ResponseCode
import me.ryleykimmel.brandywine.network.Session
import me.ryleykimmel.brandywine.network.frame.FrameMapping
import me.ryleykimmel.brandywine.network.frame.FrameMetadata.VARIABLE_BYTE_LENGTH
import me.ryleykimmel.brandywine.network.frame.FrameMetadata.VARIABLE_SHORT_LENGTH
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.network.msg.Message
import me.ryleykimmel.brandywine.network.msg.MessageCodec
import me.ryleykimmel.brandywine.server.Server
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.sql2o.Sql2o
import plugin.command.CommandEventConsumer
import plugin.login.InitializePlayerEventConsumer
import plugin.message.MessageListener
import java.io.IOException
import java.util.*
import kotlin.reflect.KClass

val world = World(EventConsumerChainSet())
val server = Server()
val gameMetadata = FrameMetadataSet()
val loginMetadata = FrameMetadataSet()
val messages = HashMap<KClass<*>, MessageListener<Any, Message>>()
val logger: Logger = LoggerFactory.getLogger("plugin-bootstrap")

// Required extension method, Kotlin does not natively support fetching object.getClass()
val Any.kt: KClass<out Any>
    get() = javaClass.kotlin

@Suppress("UNCHECKED_CAST")
fun <T : Message> FrameMetadataSet.register(message: KClass<T>, opcode: Int, length: Int) {
    val packageName = message.java.`package`.name
    val codec = Class.forName("$packageName.codec.${message.simpleName}Codec").newInstance() as MessageCodec<T>

    try {
        val listener = Class.forName("plugin.message.${message.simpleName}Listener")
        messages[message] = listener.newInstance() as MessageListener<Any, Message>
    } catch (cause: ClassNotFoundException) {
        logger.debug("{} has no message listener.", message.simpleName)
    }

    this.register(FrameMapping.create(message.java, codec, opcode, length))
}

fun main(args: Array<String>) = try {
    createFrameMetadata()

    // TODO: Recursive event parsing
    world.addConsumer(InitializePlayerEventConsumer())
    world.addConsumer(CommandEventConsumer())

    server.initializer(object : ChannelInitializer<SocketChannel>() {
        override fun initChannel(channel: SocketChannel) {
            val session = Session(channel)
            channel.pipeline().addLast("frame_codec", FrameCodec(session, loginMetadata)).
                    addLast("message_codec", FrameMessageCodec(loginMetadata)).
                    addLast("handler", GameSessionHandler(session))
        }
    })

    val gameService = GameService(world)
    server.setFileSystem(FileSystem.create("data/fs/")).
            setSql2o(Sql2o("jdbc:mysql://localhost/game_server", "root", "")).
            setAuthenticationStrategy({ ResponseCode.STATUS_OK }).
            registerService(gameService).registerService(AuthenticationService(gameService, server.authenticationStrategy)).
            init(43594)
} catch (cause: IOException) {
    throw InitializationException(cause)
}

fun createFrameMetadata() {
    loginMetadata.register(LoginHandshakeResponseMessage::class, 1, 17)
    loginMetadata.register(LoginResponseMessage::class, 2, 3)

    loginMetadata.register(LoginHandshakeMessage::class, 14, 1)
    loginMetadata.register(LoginMessage::class, 16, VARIABLE_SHORT_LENGTH)
    loginMetadata.register(LoginMessage::class, 18, VARIABLE_SHORT_LENGTH)

    gameMetadata.register(PingMessage::class, 0, 0)
    gameMetadata.register(FocusUpdateMessage::class, 3, 1)
    gameMetadata.register(ChatMessage::class, 4, VARIABLE_BYTE_LENGTH)
    gameMetadata.register(OpenTabWidgetMessage::class, 71, 3)
    gameMetadata.register(RebuildRegionMessage::class, 73, 4)
    gameMetadata.register(ResetDestinationMessage::class, 78, 0)
    gameMetadata.register(PlayerUpdateMessage::class, 81, VARIABLE_SHORT_LENGTH)
    gameMetadata.register(ArrowKeyMessage::class, 86, 4)
    gameMetadata.register(MovementMessage::class, 98, VARIABLE_BYTE_LENGTH) // command movement
    gameMetadata.register(CommandMessage::class, 103, VARIABLE_BYTE_LENGTH)
    gameMetadata.register(UpdateSkillMessage::class, 134, 6)
    gameMetadata.register(MovementMessage::class, 164, VARIABLE_BYTE_LENGTH) // game movement
    gameMetadata.register(MouseClickedMessage::class, 241, 4)
    gameMetadata.register(MovementMessage::class, 248, VARIABLE_BYTE_LENGTH) // minimap movement
    gameMetadata.register(InitializePlayerMessage::class, 249, 3)
    gameMetadata.register(ServerChatMessage::class, 253, VARIABLE_BYTE_LENGTH)
}

class GameSessionHandler(val session: Session) : SimpleChannelInboundHandler<Message>() {

    override fun channelInactive(ctx: ChannelHandlerContext) {
        val player = session.attr(Player.ATTRIBUTE_KEY).get()
        if (player != null) {
            server.getService(GameService::class).removePlayer(player)
        }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, message: Message) {
        val player = session.attr(Player.ATTRIBUTE_KEY).get()
        val listener = messages[message.kt]
        listener?.handle(player ?: session, message)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        logger.error("Uncaught exception occurred upstream", cause)
        session.close()
    }

}
