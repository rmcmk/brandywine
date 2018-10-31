package me.ryleykimmel.brandywine.network;

import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageReceivedListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listens for Messages and handles them immediately.
 */
public final class ImmediateMessageReceivedListener implements MessageReceivedListener {

    /**
     * The Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(ImmediateMessageReceivedListener.class);

    @Override
    public void messageReceived(Session session, Message message) {
        try {
            session.getFrameMetadataSet().handle(message, session);
        } catch (Exception cause) {
            logger.error("Uncaught exception while handling message: " + message, cause);
        }
    }

}