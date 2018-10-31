//package script.player_init
//
//import me.ryleykimmel.brandywine.game.model.inter.TabInterface
//import me.ryleykimmel.brandywine.game.model.inter.TabInterface.Tab
//import me.ryleykimmel.brandywine.game.model.player.InitializePlayerEvent
//import me.ryleykimmel.brandywine.game.model.skill.LevelUpSkillListener
//import me.ryleykimmel.brandywine.game.model.skill.SynchronizationSkillListener
//import me.ryleykimmel.brandywine.game.message.InitializePlayerMessage
//import me.ryleykimmel.brandywine.game.message.RebuildRegionMessage
//import me.ryleykimmel.brandywine.network.frame.codec.CipheredFrameCodec
//import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec
//import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec
//import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair
//import script.message.MessageRegistrar
//import sun.audio.AudioPlayer.player
//
//println("We're live!")
//on { InitializePlayerEvent::class }.then {
//    val session = player.session
//    val pipeline = session.channel.pipeline()
//    pipeline.replace(FrameCodec::class.java, "ciphered_frame_codec", CipheredFrameCodec(session, MessageRegistrar.gameMetadata, IsaacRandomPair.fromSeed(player.credentials.sessionKeys)))
//    pipeline.replace(FrameMessageCodec::class.java, "message_codec", FrameMessageCodec(MessageRegistrar.gameMetadata))
//
//    player.lastKnownRegion = player.position
//    player.teleport(player.position)
//
//    player.write(InitializePlayerMessage(player.isMember, player.index))
//    player.write(RebuildRegionMessage(player.position))
//
//    player.skills.addListener(SynchronizationSkillListener(player))
//    player.skills.addListener(LevelUpSkillListener(player))
//    player.skills.refresh()
//
//    player.interfaceSet.open(TabInterface(2423, Tab.ATTACK_STYLE))
//    player.interfaceSet.open(TabInterface(3917, Tab.SKILL))
//    player.interfaceSet.open(TabInterface(638, Tab.QUEST))
//    player.interfaceSet.open(TabInterface(3213, Tab.INVENTORY))
//    player.interfaceSet.open(TabInterface(1644, Tab.EQUIPMENT))
//    player.interfaceSet.open(TabInterface(5608, Tab.PRAYER))
//    player.interfaceSet.open(TabInterface(1151, Tab.SPELL_BOOK))
//    player.interfaceSet.open(TabInterface(-1, Tab.EMPTY))
//    player.interfaceSet.open(TabInterface(5065, Tab.FRIENDS))
//    player.interfaceSet.open(TabInterface(5715, Tab.IGNORES))
//    player.interfaceSet.open(TabInterface(2449, Tab.LOGOUT))
//    player.interfaceSet.open(TabInterface(904, Tab.SETTINGS))
//    player.interfaceSet.open(TabInterface(147, Tab.CONTROLS))
//    player.interfaceSet.open(TabInterface(962, Tab.MUSIC))
//
//    player.message("Welcome to Brandywine.")
//}
