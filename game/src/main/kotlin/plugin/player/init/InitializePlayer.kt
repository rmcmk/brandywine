package plugin.player.init

import me.ryleykimmel.brandywine.game.model.inter.TabInterface
import me.ryleykimmel.brandywine.game.model.inter.TabInterface.Tab
import me.ryleykimmel.brandywine.game.model.player.InitializePlayerEvent
import me.ryleykimmel.brandywine.game.model.skill.LevelUpSkillListener
import me.ryleykimmel.brandywine.game.model.skill.SynchronizationSkillListener
import me.ryleykimmel.brandywine.game.msg.InitializePlayerMessage
import me.ryleykimmel.brandywine.game.msg.RebuildRegionMessage
import me.ryleykimmel.brandywine.network.frame.codec.CipheredFrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair
import plugin.Plugin
import plugin.message.MessageRegistrar

class InitializePlayer : Plugin() {

    override fun run() {
        on(InitializePlayerEvent::class, { event ->
            with(event.player) {
                val pipeline = session.channel.pipeline()
                pipeline.replace(FrameCodec::class.java, "ciphered_frame_codec", CipheredFrameCodec(session, MessageRegistrar.gameMetadata, IsaacRandomPair.fromSeed(credentials.sessionKeys)))
                pipeline.replace(FrameMessageCodec::class.java, "message_codec", FrameMessageCodec(MessageRegistrar.gameMetadata))

                lastKnownRegion = position
                teleport(position)

                write(InitializePlayerMessage(isMember, index))
                write(RebuildRegionMessage(position))

                skills.addListener(SynchronizationSkillListener(this))
                skills.addListener(LevelUpSkillListener(this))
                skills.refresh()

                interfaceSet.open(TabInterface(2423, Tab.ATTACK_STYLE))
                interfaceSet.open(TabInterface(3917, Tab.SKILL))
                interfaceSet.open(TabInterface(638, Tab.QUEST))
                interfaceSet.open(TabInterface(3213, Tab.INVENTORY))
                interfaceSet.open(TabInterface(1644, Tab.EQUIPMENT))
                interfaceSet.open(TabInterface(5608, Tab.PRAYER))
                interfaceSet.open(TabInterface(1151, Tab.SPELL_BOOK))
                interfaceSet.open(TabInterface(-1, Tab.EMPTY))
                interfaceSet.open(TabInterface(5065, Tab.FRIENDS))
                interfaceSet.open(TabInterface(5715, Tab.IGNORES))
                interfaceSet.open(TabInterface(2449, Tab.LOGOUT))
                interfaceSet.open(TabInterface(904, Tab.SETTINGS))
                interfaceSet.open(TabInterface(147, Tab.CONTROLS))
                interfaceSet.open(TabInterface(962, Tab.MUSIC))

                message("Welcome to Brandywine.")
            }
        })
    }

}
