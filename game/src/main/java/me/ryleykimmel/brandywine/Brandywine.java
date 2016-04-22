package me.ryleykimmel.brandywine;

import java.io.IOException;

import org.sql2o.Sql2o;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.impl.SqlAuthenticationStrategy;
import me.ryleykimmel.brandywine.game.command.CommandEvent;
import me.ryleykimmel.brandywine.game.command.CommandEventConsumer;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.network.GenericSession;
import me.ryleykimmel.brandywine.network.GenericSessionHandler;
import me.ryleykimmel.brandywine.network.frame.FrameMapping;
import me.ryleykimmel.brandywine.network.frame.codec.FrameCodec;
import me.ryleykimmel.brandywine.network.frame.codec.FrameMessageCodec;
import me.ryleykimmel.brandywine.network.msg.codec.LoginHandshakeMessageCodec;
import me.ryleykimmel.brandywine.network.msg.event.LoginHandshakeMessageConsumer;
import me.ryleykimmel.brandywine.network.msg.impl.LoginHandshakeMessage;
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
        @Override
        protected void initChannel(SocketChannel channel) {
          GenericSession session = new GenericSession(channel);
          channel.pipeline()
              .addLast("frame_codec", new FrameCodec<>(session, server.getFrameMetadataSet()))
              .addLast("message_codec", new FrameMessageCodec(server.getFrameMetadataSet()))
              .addLast("handler", new GenericSessionHandler(server, session));
        }
      });

      server.registerFrame(FrameMapping.create(LoginHandshakeMessage.class,
          new LoginHandshakeMessageCodec(), 14, 1));

      server.addConsumer(LoginHandshakeMessage.class, new LoginHandshakeMessageConsumer(null));
      server.addConsumer(CommandEvent.class, new CommandEventConsumer());

      GameService gameService = new GameService(new World(server.getEvents()));
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
   * Sole private constructor to discourage instantiation of this class.
   */
  private Brandywine() {}

}
