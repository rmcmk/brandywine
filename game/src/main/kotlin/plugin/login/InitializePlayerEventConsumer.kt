package plugin.login

import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import me.ryleykimmel.brandywine.game.model.player.InitializePlayerEvent
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials
import me.ryleykimmel.brandywine.game.msg.InitializePlayerMessage
import me.ryleykimmel.brandywine.game.msg.RebuildRegionMessage
import me.ryleykimmel.brandywine.network.frame.codec.CipheredFrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.network.isaac.IsaacRandom
import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair
import plugin.gameMetadata

@Consumes(InitializePlayerEvent::class)
class InitializePlayerEventConsumer : EventConsumer<InitializePlayerEvent> {

    override fun accept(event: InitializePlayerEvent) {
        with(event.player) {
            // Unsure of this...
            session.channel.pipeline().replace("frame_codec", "ciphered_frame_codec", CipheredFrameCodec(session, gameMetadata, credentials.getRandomPair()))
            session.channel.pipeline().replace("message_codec", "message_codec", FrameMessageCodec(gameMetadata))

            write(InitializePlayerMessage(isMember, index))

            lastKnownRegion = position
            teleport(position)

            write(RebuildRegionMessage(position))
        }
    }

    fun PlayerCredentials.getRandomPair(): IsaacRandomPair {
        val copy = sessionKeys.clone()

        val decodingRandom = IsaacRandom(copy)
        copy.forEachIndexed { index, i -> copy[index] += 50 }
        val encodingRandom = IsaacRandom(copy)

        return IsaacRandomPair(encodingRandom, decodingRandom)
    }

}
