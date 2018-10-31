package me.ryleykimmel.brandywine.network.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.ryleykimmel.brandywine.network.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple upstream delegate for {@link Message}s which forwards them to a Session for future handling.
 */
public final class MessageHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * The Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(MessageHandler.class);

    /**
     * The Session this MessageHandler is for.
     */
    private final Session session;

    /**
     * Constructs a new {@link MessageHandler}.
     *
     * @param session The Session this MessageHandler is for.
     */
    public MessageHandler(Session session) {
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) {
        session.messageReceived(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Uncaught exception occurred.", cause);
        session.close();
    }

}