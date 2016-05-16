package script

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import me.ryleykimmel.brandywine.InitializationException
import me.ryleykimmel.brandywine.fs.FileSystem
import me.ryleykimmel.brandywine.game.GameService
import me.ryleykimmel.brandywine.game.GameSession
import me.ryleykimmel.brandywine.game.GameSessionHandler
import me.ryleykimmel.brandywine.game.auth.AuthenticationService
import me.ryleykimmel.brandywine.game.auth.impl.SqlAuthenticationStrategy
import me.ryleykimmel.brandywine.game.model.World
import me.ryleykimmel.brandywine.game.msg.LoginHandshakeMessage
import me.ryleykimmel.brandywine.game.msg.codec.LoginHandshakeMessageCodec
import me.ryleykimmel.brandywine.network.frame.FrameMapping
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.server.Server
import org.sql2o.Sql2o
import java.io.IOException
import java.util.concurrent.ThreadLocalRandom

val random = ThreadLocalRandom.current()
val world = World()

fun random(min: Int, max: Int): Int = random.nextInt(max - min) + min
fun random(range: Int): Int = random.nextInt(range)

fun main(args: Array<String>) {
    try {
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
    } catch (cause: IOException) {
        throw InitializationException(cause)
    }
}

/**
 * Creates a new [FrameMetadataSet].

 * @return The new FrameMetadataSet, never `null`.
 */
fun createFrameMetadataSet(): FrameMetadataSet {
    val metadata = FrameMetadataSet()

    metadata.register(
            FrameMapping.create(LoginHandshakeMessage::class.java, LoginHandshakeMessageCodec(), 14, 1))

    return metadata
}