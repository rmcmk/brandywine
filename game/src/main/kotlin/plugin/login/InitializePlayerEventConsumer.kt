package plugin.login

import me.ryleykimmel.brandywine.game.event.Consumes
import me.ryleykimmel.brandywine.game.event.EventConsumer
import me.ryleykimmel.brandywine.game.model.player.InitializePlayerEvent
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials
import me.ryleykimmel.brandywine.game.model.skill.LevelUpSkillListener
import me.ryleykimmel.brandywine.game.model.skill.SynchronizationSkillListener
import me.ryleykimmel.brandywine.game.msg.InitializePlayerMessage
import me.ryleykimmel.brandywine.game.msg.RebuildRegionMessage
import me.ryleykimmel.brandywine.network.frame.codec.CipheredFrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.network.isaac.IsaacRandom
import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair
import plugin.message
import plugin.message.MessageRegistrar
import plugin.server
import plugin.task.Tasks
import plugin.task.continuous

@Consumes(InitializePlayerEvent::class)
class InitializePlayerEventConsumer : EventConsumer<InitializePlayerEvent> {

    override fun accept(event: InitializePlayerEvent) {
        with(event.player) {
            // TODO: Better solution for this, context switching looks terrible :-(
            val pipeline = session.channel.pipeline()
            pipeline.replace(FrameCodec::class.java, "ciphered_frame_codec", CipheredFrameCodec(session, MessageRegistrar.gameMetadata, credentials.getRandomPair()))
            pipeline.replace(FrameMessageCodec::class.java, "message_codec", FrameMessageCodec(MessageRegistrar.gameMetadata))

            lastKnownRegion = position
            teleport(position)

            write(InitializePlayerMessage(isMember, index))
            write(RebuildRegionMessage(position))

            skills.addListener(SynchronizationSkillListener(this))
            skills.addListener(LevelUpSkillListener(this))
            skills.refresh()

            message("Welcome to %s.", "Brandywine") // TODO: Add config for this
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
