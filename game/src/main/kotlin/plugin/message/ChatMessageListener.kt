package plugin.message

import me.ryleykimmel.brandywine.game.model.player.Player
import me.ryleykimmel.brandywine.game.msg.ChatMessage
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock

class ChatMessageListener : MessageListener<Player, ChatMessage> {

    override fun handle(source: Player, message: ChatMessage) {
        source.flagUpdate(ChatPlayerBlock.create(source, message))
    }

}
