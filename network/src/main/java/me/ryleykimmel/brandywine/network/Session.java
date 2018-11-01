package me.ryleykimmel.brandywine.network;

import com.google.common.base.MoreObjects;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageReceivedListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A session which is attached to some {@link SocketChannel}, every connected SocketChannel has their own session in order to perform session specific functions, such as writing,
 * flushing, etc.
 */
public final class Session {

  /**
   * The maximum amount of queued {@link Message}'s any single Session can contain at once.
   */
  static final int MAXIMUM_QUEUED_MESSAGES = 50;

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LogManager.getLogger(Session.class);

  /**
   * A SecureRandom generator, used for generating session ids.
   */
  private static final Random RANDOM = new SecureRandom();

  /**
   * The id of this Session.
   */
  private final long id = RANDOM.nextLong();

  /**
   * An {@link ArrayDeque} of {@link Message}'s. Capped at {@link Session#MAXIMUM_QUEUED_MESSAGES}.
   */
  private final Queue<Message> receivedMessages = new ArrayDeque<>(MAXIMUM_QUEUED_MESSAGES);

  /**
   * The SocketChannel this Session is listening on.
   */
  private final SocketChannel channel;

  /**
   * A {@link FrameMetadataSet} containing info about game related {@link Message}'s.
   */
  private FrameMetadataSet frameMetadataSet;

  /**
   * The MessageReceivedListener which listens for calls to {@link Session#messageReceived(Message)}.
   */
  private MessageReceivedListener messageReceivedListener;

  /**
   * Whether or not this Session has been closed.
   */
  private boolean closed;

  /**
   * Constructs a new {@link Session}.
   *
   * @param channel The SocketChannel this Session is listening on.
   * @param frameMetadataSet A {@link FrameMetadataSet} containing info about {@link Message}'s.
   * @param messageReceivedListener The MessageReceivedListener which listens for calls to {@link Session#messageReceived(Message)}.
   */
  public Session(SocketChannel channel, FrameMetadataSet frameMetadataSet,
      MessageReceivedListener messageReceivedListener) {
    this.channel = channel;
    this.frameMetadataSet = frameMetadataSet;
    this.messageReceivedListener = messageReceivedListener;
  }

  /**
   * Gets the id of this Session.
   *
   * @return This Sessions id.
   */
  public long getId() {
    return id;
  }

  /**
   * Writes the specified Message to the SocketChannel, without flushing.
   * <p>
   * Ensure to flush the Channel once you are ready to flush all pending written data.
   * </p>
   *
   * @param message The Message to write.
   * @return The ChannelFuture instance.
   */
  public ChannelFuture write(Message message) {
    return channel.write(message);
  }

  /**
   * Writes and flushes the specified Message to the SocketChannel.
   *
   * @param message The Message to write.
   * @return The ChannelFuture instance.
   */
  public ChannelFuture writeAndFlush(Message message) {
    return channel.writeAndFlush(message);
  }

  /**
   * Writes the specified Message to the SocketChannel, without flushing.
   * <p>
   * Ensure to flush the Channel once you are ready to flush all pending written data.
   * </p>
   *
   * @param message The Message to write.
   */
  public void voidWrite(Message message) {
    channel.write(message, channel.voidPromise());
  }

  /**
   * Writes and flushes the specified Message to the SocketChannel.
   *
   * @param message The Message to write.
   */
  public void voidWriteAndFlush(Message message) {
    channel.writeAndFlush(message, channel.voidPromise());
  }

  /**
   * Requests that all pending Messages be flushed from the SocketChannel.
   */
  public void flush() {
    channel.flush();
  }

  /**
   * Queues a received Message from the SocketChannel.
   *
   * @param message The Message to queue.
   */
  public void messageReceived(Message message) {
    messageReceivedListener.messageReceived(this, message);
  }

  /**
   * Handles all pending {@link Message}'s in the {@link Session#receivedMessages} queue.
   * <p>
   * The Message's polled here must belong to the {@link Session#frameMetadataSet} otherwise nothing will happen.
   * </p>
   */
  public void dequeueReceivedMessages() {
    Message message;
    while ((message = receivedMessages.poll()) != null) {
      try {
        frameMetadataSet.handle(message, this);
      } catch (Exception cause) {
        logger.error("Uncaught exception while handling message: " + message, cause);
      }
    }
  }

  /**
   * Grabs the active {@link FrameMetadataSet} from this Session based on its state.
   *
   * @return An instance of {@link FrameMetadataSet}, never {@code null}.
   */
  public FrameMetadataSet getFrameMetadataSet() {
    return frameMetadataSet;
  }

  /**
   * Closes this Session.
   * <p>
   * This method is not intended to provide additional functionality or be overridden. If you wish to perform other events on channel close see {@link
   * Session#onClose(ChannelFutureListener)}.
   * </p>
   */
  public void close() {
    if (closed) {
      return;
    }

    channel.close();
    closed = true;
  }

  /**
   * Adds the specified {@link ChannelFutureListener} to be performed when this Session is closed.
   *
   * @param listener The ChannelFutureListener to perform when this Session is closed.
   */
  public void onClose(ChannelFutureListener listener) {
    channel.closeFuture().addListener(listener);
  }

  /**
   * Tests whether or not this Session has been closed or is no longer active.
   *
   * @return {@code true} if this Session is closed or is no longer active.
   */
  public boolean isClosed() {
    return closed; // TODO: Other tests?
  }

  /**
   * Gets the connected address of this Session.
   *
   * @return The remote connected address of this Session.
   */
  public InetSocketAddress getRemoteAddress() {
    return channel.remoteAddress();
  }

  /**
   * Gets the SocketChannel this session is listening on.
   *
   * @return The SocketChannel this Session is listening on.
   */
  public SocketChannel getChannel() {
    return channel;
  }

  /**
   * Tests if the specified {@link AttributeKey} exists.
   *
   * @param key The AttributeKey.
   * @param <T> The Attributes type.
   * @return {@code true} iff the specified {@link AttributeKey} exists.
   */
  public <T> boolean hasAttr(AttributeKey<T> key) {
    return channel.hasAttr(key);
  }

  /**
   * Gets the Attribute for the specified AttributeKey.
   *
   * @param key The AttributeKey.
   * @param <T> The Attributes type.
   * @return The Attribute for the specified key.
   */
  public <T> Attribute<T> attr(AttributeKey<T> key) {
    return channel.attr(key);
  }

  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Session) {
      Session other = (Session) obj;
      return id == other.id;
    }

    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("remote_addr", getRemoteAddress())
        .toString();
  }

}
