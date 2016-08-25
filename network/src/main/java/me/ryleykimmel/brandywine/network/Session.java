package me.ryleykimmel.brandywine.network;

import com.google.common.base.MoreObjects;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import me.ryleykimmel.brandywine.network.msg.Message;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Random;

/**
 * A session which is attached to some {@link SocketChannel}, every connected SocketChannel has
 * their own session in order to perform session specific functions, such as writing, flushing, etc.
 */
public final class Session {

  /**
   * A SecureRandom generator, used for generating session keys.
   */
  private static final Random RANDOM = new SecureRandom();

  /**
   * The key of this Session.
   */
  private final long sessionKey = RANDOM.nextLong();

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
  public long getSessionKey() {
    return sessionKey;
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
   * Closes this Session.
   * <p>
   * This method is not intended to provide additional functionality or be overridden. If you wish
   * to perform other events on channel close see {@link Session#onClose(ChannelFutureListener)}.
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
    return closed; // XXX: Other tests?
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
