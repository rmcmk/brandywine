package me.ryleykimmel.brandywine.network;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Random;

import com.google.common.base.MoreObjects;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.game.event.Event;
import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.event.EventConsumerChain;
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A session which is attached to some {@link SocketChannel}, every connected SocketChannel has
 * their own session in order to perform session specific functions, such as writing, flushing, etc.
 */
public abstract class Session {

  /**
   * A SecureRandom generator, used for generating session keys.
   */
  private static final Random RANDOM = new SecureRandom();

  /**
   * The key of this Session.
   */
  private final long sessionKey = RANDOM.nextLong();

  /**
   * The EventConsumerChainSet for this Session.
   */
  private final EventConsumerChainSet events = new EventConsumerChainSet();

  /**
   * The SocketChannel this Session is listening on.
   */
  private final SocketChannel channel;

  /**
   * Whether or not this Session has been closed.
   */
  private boolean closed;

  /**
   * Constructs a new {@link Session}.
   *
   * @param channel The SocketChannel this Session is listening on.
   */
  public Session(SocketChannel channel) {
    this.channel = channel;
  }

  /**
   * Gets the key of this Session.
   *
   * @return This Sessions key.
   */
  public final long getSessionKey() {
    return sessionKey;
  }

  /**
   * Notifies the appropriate {@link EventConsumerChain} that an {@link Event} has occurred.
   *
   * @param event The Event.
   * @return {@code true} if the Event should continue on with its outcome.
   */
  public <E extends Event> boolean notify(E event) {
    return events.notify(event);
  }

  /**
   * Places the {@link EventConsumerChain} into this set.
   *
   * @param clazz The {@link Class} to associate the EventListenerChain with.
   * @param consumer The EventListenerChain.
   */
  public <E extends Event> void addConsumer(Class<E> clazz, EventConsumer<E> consumer) {
    events.addConsumer(clazz, consumer);
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
  public final ChannelFuture write(Message message) {
    return channel.write(message);
  }

  /**
   * Writes and flushes the specified Message to the SocketChannel.
   *
   * @param message The Message to write.
   * @return The ChannelFuture instance.
   */
  public final ChannelFuture writeAndFlush(Message message) {
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
  public final void voidWrite(Message message) {
    channel.write(message, channel.voidPromise());
  }

  /**
   * Writes and flushes the specified Message to the SocketChannel.
   *
   * @param message The Message to write.
   */
  public final void voidWriteAndFlush(Message message) {
    channel.writeAndFlush(message, channel.voidPromise());
  }

  /**
   * Requests that all pending Messages be flushed from the SocketChannel.
   */
  public final void flush() {
    channel.flush();
  }

  /**
   * Closes this Session.
   * <p>
   * This method is not intended to provide additional functionality or be overridden. If you wish
   * to perform other events on channel close see {@link Session#onClose(ChannelFutureListener)}.
   * </p>
   */
  public final void close() {
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
  public final void onClose(ChannelFutureListener listener) {
    channel.closeFuture().addListener(listener);
  }

  /**
   * Tests whether or not this Session has been closed or is no longer active.
   * 
   * @return {@code true} if this Session is closed or is no longer active.
   */
  public final boolean isClosed() {
    return closed; // XXX: Other tests?
  }

  /**
   * Gets the connected address of this Session.
   *
   * @return The remote connected address of this Session.
   */
  public final InetSocketAddress getRemoteAddress() {
    return channel.remoteAddress();
  }

  /**
   * Gets the SocketChannel this session is listening on.
   *
   * @return The SocketChannel this Session is listening on.
   */
  public final SocketChannel getChannel() {
    return channel;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(sessionKey);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Session) {
      Session other = (Session) obj;
      return sessionKey == other.sessionKey;
    }

    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("sessionKey", sessionKey).toString();
  }

}
