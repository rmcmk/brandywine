package me.ryleykimmel.brandywine.network.game;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.isaac.IsaacRandom;
import me.ryleykimmel.brandywine.network.isaac.IsaacRandomPair;
import me.ryleykimmel.brandywine.network.msg.GameMessageDispatcher;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageDispatcher;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;

/**
 * A Session which is attached to some {@link SocketChannel}, every connected SocketChannel has
 * their own Session in order to perform Session specific functions, such as writing, flushing, etc.
 */
public final class GameSession {

  /**
   * An AttributeKey representing a Player attribute.
   */
  private static final AttributeKey<Player> PLAYER_KEY = AttributeKey.valueOf("player");

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
   * The IsaacRandom cipher pair, used for ciphering Frame opcodes.
   */
  private IsaacRandomPair randomPair;

  /**
   * The MessageDispatcher for this GameSession.
   */
  @SuppressWarnings("rawtypes")
  private MessageDispatcher dispatcher = new GameMessageDispatcher(this);

  /**
   * Whether or not this GameSession has been closed.
   */
  private boolean closed;

  /**
   * Constructs a new {@link GameSession} with the specified ServerContext and SocketChannel.
   *
   * @param channel The SocketChannel this Session is listening on.
   */
  public GameSession(SocketChannel channel) {
    this.channel = channel;
  }

  /**
   * Seeds the IsaacRandom ciphers from the Session keys received from the client.
   *
   * @param sessionKeys The Session keys we build the seed from.
   */
  public void seedCiphers(int[] sessionKeys) {
    Preconditions.checkState(randomPair == null,
        "The ciphers have already been seeded for this GameSession.");

    int[] copy = sessionKeys.clone();

    IsaacRandom decodingRandom = new IsaacRandom(copy);
    Arrays.setAll(copy, index -> copy[index] += 50);
    IsaacRandom encodingRandom = new IsaacRandom(copy);

    randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);
  }

  /**
   * Deciphers the specified Frame opcode.
   *
   * @param opcode The opcode to decipher.
   * @return The deciphered opcode.
   */
  public int decipherFrameOpcode(int opcode) {
    Preconditions.checkNotNull(randomPair,
        "The ciphers were not seeded, ensure GameSession.seedCiphers(int[]) was called.");

    IsaacRandom random = randomPair.getDecodingRandom();
    return opcode - random.nextInt() & 0xFF;
  }

  /**
   * Enciphers the specified Frame opcode.
   *
   * @param opcode The opcode to encipher.
   * @return The enciphered opcode.
   */
  public int encipherFrameOpcode(int opcode) {
    Preconditions.checkNotNull(randomPair,
        "The ciphers were not seeded, ensure GameSession.seedCiphers(int[]) was called.");

    IsaacRandom random = randomPair.getEncodingRandom();
    return opcode + random.nextInt() & 0xFF;
  }

  /**
   * Sets this GameSessions MessageDispatcher.
   * 
   * @param dispatcher The MessageDispatcher for this GameSession.
   */
  public void setMessageDispatcher(@SuppressWarnings("rawtypes") MessageDispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  /**
   * Returns {@code true} if this GameSession is currently ciphering Frames.
   * <p>
   * If this GameSession is ciphering Frames they have passed the stage of login and the IsaacRandom
   * ciphers were seeded.
   * </p>
   *
   * @return {@code true} if and only if this GameSession is currently ciphering Frames otherwise
   * {@code false}.
   */
  public boolean isCipheringFrames() {
    return randomPair != null;
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
   * Dispatches the specified Message for this GameSession.
   * 
   * @param metadata The metadata for the specified Frame.
   * @param message The Message to dispatch.
   */
  @SuppressWarnings("unchecked")
  public void dispatch(FrameMetadataSet metadata, Message message) {
    MessageHandler<Message> handler = metadata.getHandler(message);
    if (handler != null) {
      dispatcher.dispatch(handler, message);
    }
  }

  /**
   * Requests that all pending Messages be flushed from the SocketChannel.
   */
  public void flush() {
    channel.flush();
  }

  /**
   * Closes this GameSession.
   */
  public void close() {
    if (closed) {
      return;
    }

    channel.close();
    closed = true;
  }

  /**
   * Tests whether or not this GameSession has been closed or is no longer active.
   * 
   * @return {@code true} if this GameSession is closed or is no longer active, otherwise
   * {@code false}.
   */
  public boolean isClosed() {
    return closed || !channel.isActive();
  }

  /**
   * Destroys this GameSession.
   * 
   * @param service The GameService, used to queue the removal of this GameSession's Player.
   */
  public void destroy(GameService service) {
    Optional<Player> player = Optional.ofNullable(attr().getAndRemove());
    player.ifPresent(service::removePlayer);
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
   * Gets this sessions Player Attribute for the Player attribute key.
   * 
   * @return The Attribute instance for the specified AttributeKey.
   */
  public Attribute<Player> attr() {
    return channel.attr(PLAYER_KEY);
  }

  @Override
  public int hashCode() {
    return Long.hashCode(sessionKey);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof GameSession) {
      GameSession other = (GameSession) obj;
      return sessionKey == other.sessionKey;
    }

    return false;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("sessionKey", sessionKey).toString();
  }

}
