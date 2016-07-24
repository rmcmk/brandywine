package plugin.message

import io.netty.channel.ChannelFutureListener
import me.ryleykimmel.brandywine.game.auth.AuthenticationRequest
import me.ryleykimmel.brandywine.game.auth.AuthenticationService
import me.ryleykimmel.brandywine.game.model.player.PlayerCredentials
import me.ryleykimmel.brandywine.game.msg.LoginMessage
import me.ryleykimmel.brandywine.game.msg.LoginResponseMessage
import me.ryleykimmel.brandywine.network.ResponseCode
import me.ryleykimmel.brandywine.network.Session
import plugin.getService
import plugin.server

class LoginMessageListener : MessageListener<Session, LoginMessage> {

    val CLIENT_VERSION = 317
    val EXPECTED_DUMMY = 255
    val EXPECTED_BLOCK_OPCODE = 10

    override fun handle(source: Session, message: LoginMessage) {
        with(message) {
            if (dummy != EXPECTED_DUMMY) {
                source.closeWithResponse(ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION)
                return
            }

            if (clientVersion != CLIENT_VERSION) {
                source.closeWithResponse(ResponseCode.STATUS_GAME_UPDATED)
                return
            }

            if (detail != 0 && detail != 1) {
                source.closeWithResponse(ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION)
                return
            }

            if (blockOperationCode != EXPECTED_BLOCK_OPCODE) {
                source.closeWithResponse(ResponseCode.STATUS_LOGIN_SERVER_REJECTED_SESSION)
                return
            }

            if (sessionKeys.size != 4) { // TODO: Rid of magic
                source.closeWithResponse(ResponseCode.STATUS_BAD_SESSION_ID)
                return
            }

            val sessionKey = sessionKeys[2].toLong() shl 32 or (sessionKeys[3].toLong() and 0xFFFFFFFFL)
            if (sessionKey !== sessionKey) {
                source.closeWithResponse(ResponseCode.STATUS_BAD_SESSION_ID)
                return
            }

            server.getService(AuthenticationService::class).submit(AuthenticationRequest(source, PlayerCredentials(userId, username, password, sessionKeys)))
        }
    }

    fun Session.closeWithResponse(response: ResponseCode) = this.writeAndFlush(LoginResponseMessage(response)).addListener(ChannelFutureListener.CLOSE)!!

}
