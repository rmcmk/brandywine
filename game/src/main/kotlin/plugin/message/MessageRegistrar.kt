package plugin.message

import me.ryleykimmel.brandywine.game.msg.*
import me.ryleykimmel.brandywine.network.frame.FrameMapping
import me.ryleykimmel.brandywine.network.frame.FrameMetadata
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet
import me.ryleykimmel.brandywine.network.msg.Message
import me.ryleykimmel.brandywine.network.msg.MessageCodec
import plugin.logger
import java.util.*
import kotlin.reflect.KClass

object MessageRegistrar {

    val gameMetadata = FrameMetadataSet()
    val loginMetadata = FrameMetadataSet()
    val messages = HashMap<KClass<*>, MessageListener<Any, Message>>()

    fun init() {
        loginMetadata.register(LoginHandshakeResponseMessage::class, 1, 17)
        loginMetadata.register(LoginResponseMessage::class, 2, 3)

        loginMetadata.register(LoginHandshakeMessage::class, 14, 1)
        loginMetadata.register(LoginMessage::class, 16, FrameMetadata.VARIABLE_SHORT_LENGTH)
        loginMetadata.register(LoginMessage::class, 18, FrameMetadata.VARIABLE_SHORT_LENGTH)

        gameMetadata.register(PingMessage::class, 0, 0)
        gameMetadata.register(FocusUpdateMessage::class, 3, 1)
        gameMetadata.register(ChatMessage::class, 4, FrameMetadata.VARIABLE_BYTE_LENGTH)
        gameMetadata.register(OpenTabInterfaceMessage::class, 71, 3)
        gameMetadata.register(RebuildRegionMessage::class, 73, 4)
        gameMetadata.register(ResetDestinationMessage::class, 78, 0)
        gameMetadata.register(PlayerUpdateMessage::class, 81, FrameMetadata.VARIABLE_SHORT_LENGTH)
        gameMetadata.register(ArrowKeyMessage::class, 86, 4)
        gameMetadata.register(MovementMessage::class, 98, FrameMetadata.VARIABLE_BYTE_LENGTH) // command movement
        gameMetadata.register(CommandMessage::class, 103, FrameMetadata.VARIABLE_BYTE_LENGTH)
        gameMetadata.register(CloseInterfaceMessage::class, 130, 0)
        gameMetadata.register(UpdateSkillMessage::class, 134, 6)
        gameMetadata.register(MovementMessage::class, 164, FrameMetadata.VARIABLE_BYTE_LENGTH) // game movement
        gameMetadata.register(MouseClickedMessage::class, 241, 4)
        gameMetadata.register(MovementMessage::class, 248, FrameMetadata.VARIABLE_BYTE_LENGTH) // minimap movement
        gameMetadata.register(InitializePlayerMessage::class, 249, 3)
        gameMetadata.register(ServerChatMessage::class, 253, FrameMetadata.VARIABLE_BYTE_LENGTH)
    }

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

}
