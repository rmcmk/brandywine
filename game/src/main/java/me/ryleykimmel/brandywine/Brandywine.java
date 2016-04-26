package me.ryleykimmel.brandywine;

import java.io.IOException;

import org.sql2o.Sql2o;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.GameSession;
import me.ryleykimmel.brandywine.game.GameSessionHandler;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.impl.SqlAuthenticationStrategy;
import me.ryleykimmel.brandywine.game.msg.LoginHandshakeMessage;
import me.ryleykimmel.brandywine.game.msg.codec.LoginHandshakeMessageCodec;
import me.ryleykimmel.brandywine.network.frame.FrameMapping;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec;
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec;
import me.ryleykimmel.brandywine.server.Server;

/**
 * Manages configuration and initialization of a Server.
 */
final class Brandywine {

  /**
   * The entry point of this application, which implements the command-line interface.
   * 
   * @param args The command-line arguments.
   */
  public static void main(String[] args) {
    try {
      Server server = new Server();

      server.initializer(new ChannelInitializer<SocketChannel>() {

        private final FrameMetadataSet metadata = createFrameMetadataSet();

        @Override
        protected void initChannel(SocketChannel channel) {
          GameSession session = new GameSession(channel);

          channel.pipeline().addLast("frame_codec", new FrameCodec<>(session, metadata))
              .addLast("message_codec", new FrameMessageCodec(metadata))
              .addLast("handler", new GameSessionHandler(session));
        }
    });

      GameService gameService = new GameService();
      server.setName("RuneScape").setFileSystem(FileSystem.create("data/fs"))
          .setSql2o(new Sql2o("jdbc:mysql://localhost/game_server", "root", ""))
          .setAuthenticationStrategy(new SqlAuthenticationStrategy(server.getSql2o()))
          .registerService(gameService)
          .registerService(
              new AuthenticationService(gameService, server.getAuthenticationStrategy()))
          .init(43594);
    } catch (IOException cause) {
      throw new InitializationException(cause);
    }
  }

  /**
   * Creates a new {@link FrameMetadataSet}.
   * 
   * @return The new FrameMetadataSet, never {@code null}.
   */
  private static FrameMetadataSet createFrameMetadataSet() {
    FrameMetadataSet metadata = new FrameMetadataSet();

    metadata.register(
        FrameMapping.create(LoginHandshakeMessage.class, new LoginHandshakeMessageCodec(), 14, 1));

    return metadata;
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private Brandywine() {}

}
