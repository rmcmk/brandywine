package me.ryleykimmel.brandywine.network;

import static me.ryleykimmel.brandywine.network.Session.MAXIMUM_QUEUED_MESSAGES;

import io.netty.util.internal.StringUtil;
import java.util.Queue;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageReceivedListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Listens for Messages and queues them to be handled later.
 */
public final class QueuedMessageReceivedListener implements MessageReceivedListener {

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LogManager.getLogger(QueuedMessageReceivedListener.class);

  /**
   * A {@link Queue} of {@link Message}'s. Capped at {@link Session#MAXIMUM_QUEUED_MESSAGES}.
   */
  private final Queue<Message> messages;

  /**
   * Constructs a new {@link QueuedMessageReceivedListener}.
   *
   * @param messages A {@link Queue} of {@link Message}'s
   */
  public QueuedMessageReceivedListener(Queue<Message> messages) {
    this.messages = messages;
  }

  @Override
  public void messageReceived(Session session, Message message) {
    if (messages.size() >= MAXIMUM_QUEUED_MESSAGES) {
      logger.warn("Maximum messages queued for session: {}, dropping next message: {}",
          session, StringUtil.simpleClassName(message));
    } else {
      messages.add(message);
    }
  }

}