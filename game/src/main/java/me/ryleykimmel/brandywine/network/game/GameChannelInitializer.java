package me.ryleykimmel.brandywine.network.game;

import com.google.common.base.Preconditions;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.network.game.frame.FrameDecoder;
import me.ryleykimmel.brandywine.network.game.frame.FrameEncoder;
import me.ryleykimmel.brandywine.network.game.frame.FrameMetadataSet;

/**
 * Initializes a {@link SocketChannel} for the GameService.
 */
public final class GameChannelInitializer extends ChannelInitializer<SocketChannel> {

  /**
   * The GameService for the {@link ChannelHandler}s.
   */
  private final GameService service;

  /**
   * The FrameMetadataSet for the {@link ChannelHandler}s.
   */
  private final FrameMetadataSet metadata;

  /**
   * Constructs a new {@link GameChannelInitializer}.
   * 
   * @param service The GameService for the {@link ChannelHandler}s.
   * @param metadata The FrameMetadataSet for the {@link ChannelHandler}s.
   */
  public GameChannelInitializer(GameService service, FrameMetadataSet metadata) {
    this.service = Preconditions.checkNotNull(service, "GameService may not be null.");
    this.metadata = Preconditions.checkNotNull(metadata, "FrameMetadataSet may not be null.");
  }

  @Override
  protected void initChannel(SocketChannel channel) {
    GameSession session = new GameSession(channel);

    channel.pipeline().addLast(new FrameEncoder(session), new MessageEncoder(metadata),
        new FrameDecoder(session, metadata), new MessageDecoder(metadata),
        new GameSessionHandler(session, service, metadata));
  }

}
