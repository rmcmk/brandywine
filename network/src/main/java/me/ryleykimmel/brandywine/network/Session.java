package me.ryleykimmel.brandywine.network;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
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
   * An {@link AttributeKey} representing an active players identifier within the game world.
   * <p>
   * A Session effectively has two states which is can process messages. Pre-game and in-game. The pre-game state occurs when this attribute is <strong>not</strong> set otherwise,
   * if this attribute is set, this Session has been authenticated and is considered in-game.
   * </p>
   */
  private static final AttributeKey<Long> ACTIVE_PLAYER = AttributeKey.valueOf("active.player");

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
   * Represents a {@link MessageReceivedListener} which queues {@link Message}s so they can be dealt with later.
   */
  private final MessageReceivedListener queuedMessageReceivedListener = new QueuedMessageReceivedListener(receivedMessages);

  /**
   * Represents a {@link MessageReceivedListener} which immediately processes {@link Message}s.
   */
  private final MessageReceivedListener immediateMessageReceivedListener = new ImmediateMessageReceivedListener();

  /**
   * The SocketChannel this Session is listening on.
   */
  private final SocketChannel channel;

  /**
   * A {@link FrameMetadataSet} containing info about game related {@link Message}'s.
   */
  private final FrameMetadataSet gameFrameMetadataSet;

  /**
   * A {@link FrameMetadataSet} containing info about login related {@link Message}'s.
   */
  private final FrameMetadataSet loginFrameMetadataSet;

  /**
   * Whether or not this Session has been closed.
   */
  private boolean closed;

  /**
   * Constructs a new {@link Session}.
   *
   * @param channel The SocketChannel this Session is listening on.
   * @param gameFrameMetadataSet A {@link FrameMetadataSet} containing info about game {@link Message}'s.
   * @param loginFrameMetadataSet A {@link FrameMetadataSet} containing info about login {@link Message}'s.
   */
  public Session(SocketChannel channel, FrameMetadataSet gameFrameMetadataSet, FrameMetadataSet loginFrameMetadataSet) {
    this.channel = channel;
    this.gameFrameMetadataSet = Preconditions.checkNotNull(gameFrameMetadataSet);
    this.loginFrameMetadataSet = Preconditions.checkNotNull(loginFrameMetadataSet);
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
    getMessageReceivedListener().messageReceived(this, message);
  }

  /**
   * Handles all pending {@link Message}'s in the {@link Session#receivedMessages} queue.
   */
  public void dequeueReceivedMessages() {
    Message message;
    while ((message = receivedMessages.poll()) != null) {
      try {
        getFrameMetadataSet().handle(message, this);
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
    return channel.hasAttr(ACTIVE_PLAYER) ? gameFrameMetadataSet : loginFrameMetadataSet;
  }

  /**
   * Grabs the active {@link MessageReceivedListener} from this Session based on its state.
   *
   * @return An instance of {@link MessageReceivedListener}, never {@code null}.
   */
  public MessageReceivedListener getMessageReceivedListener() {
    return channel.hasAttr(ACTIVE_PLAYER) ? queuedMessageReceivedListener : immediateMessageReceivedListener;
  }

  /**
   * Closes this Session.
   * <p>
   * This method is not intended to provide additional functionality. If you wish to perform other events on channel close see {@link Session#onClose(ChannelFutureListener)}.
   * </p>
   */
  public void close() {
    if (closed) {
      return;
    }

    closed = true;
    channel.close();
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
    return closed;
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
   * Sets the {@link Session#ACTIVE_PLAYER} attribute for this Session.
   *
   * @param identifier The identifier of the active player.
   * @throws IllegalStateException Iff the active player is already present.
   */
  public void setActivePlayer(long identifier) {
    if (channel.hasAttr(ACTIVE_PLAYER)) {
      throw new IllegalStateException("Active player for " + toString() + " already set.");
    }
    channel.attr(ACTIVE_PLAYER).setIfAbsent(identifier);
  }

  /**
   * Gets the active player from this Session, if it exists.
   *
   * @return The active player from this session, if it exists.
   * @throws IllegalStateException Iff the active player is not present.
   */
  public long getActivePlayer() {
    if (channel.hasAttr(ACTIVE_PLAYER)) {
      return channel.attr(ACTIVE_PLAYER).get();
    }
    throw new IllegalStateException(toString() + " has no active player set.");
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
