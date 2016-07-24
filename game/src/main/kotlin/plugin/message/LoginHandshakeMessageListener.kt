package plugin.message

import me.ryleykimmel.brandywine.game.msg.LoginHandshakeMessage
import me.ryleykimmel.brandywine.game.msg.LoginHandshakeResponseMessage
import me.ryleykimmel.brandywine.network.Session

class LoginHandshakeMessageListener : MessageListener<Session, LoginHandshakeMessage> {

    val STATUS_EXCHANGE_DATA = 0
    val EXPECTED_DUMMY = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)

    override fun handle(source: Session, message: LoginHandshakeMessage) {
        source.voidWriteAndFlush(LoginHandshakeResponseMessage(STATUS_EXCHANGE_DATA, EXPECTED_DUMMY, source.sessionKey))
    }

}
