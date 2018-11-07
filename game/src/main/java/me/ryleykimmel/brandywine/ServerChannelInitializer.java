package me.ryleykimmel.brandywine;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec;
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageHandler;

/**
 * Our {@link ChannelInitializer} implementation. Used to configure {@link Channel}s once they have been registered in the event loop.
 */
public final class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

  /**
   * A {@link FrameMetadataSet} containing info about game related {@link Message}'s.
   */
  private final FrameMetadataSet gameFrameMetadataSet;

  /**
   * A {@link FrameMetadataSet} containing info about login related {@link Message}'s.
   */
  private final FrameMetadataSet loginFrameMetadataSet;

  /**
   * Constructs a new {@link ServerChannelInitializer}.
   *
   * @param gameFrameMetadataSet A {@link FrameMetadataSet} containing info about game {@link Message}'s.
   * @param loginFrameMetadataSet A {@link FrameMetadataSet} containing info about login {@link Message}'s.
   */
  ServerChannelInitializer(FrameMetadataSet gameFrameMetadataSet, FrameMetadataSet loginFrameMetadataSet) {
    this.gameFrameMetadataSet = Preconditions.checkNotNull(gameFrameMetadataSet);
    this.loginFrameMetadataSet = Preconditions.checkNotNull(loginFrameMetadataSet);
  }

  @Override
  protected void initChannel(SocketChannel channel) {
    Session session = new Session(channel, gameFrameMetadataSet, loginFrameMetadataSet);

    channel.pipeline()
        .addLast(new FrameCodec(session))
        .addLast(new FrameMessageCodec(session))
        .addLast(new MessageHandler(session));
  }

}