package me.ryleykimmel.brandywine;

import dagger.Module;
import dagger.Provides;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javax.inject.Singleton;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet;
import me.ryleykimmel.brandywine.game.message.GameMessageRegistrar;
import me.ryleykimmel.brandywine.game.message.LoginMessageRegistrar;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.network.message.GameMessages;
import me.ryleykimmel.brandywine.network.message.LoginMessages;
import me.ryleykimmel.brandywine.network.message.MessageRegistrar;

@Module
public final class ServerModule {

  @Provides
  @Singleton
  EventConsumerChainSet providesEventConsumerChainSet() {
    return new EventConsumerChainSet();
  }

  @Provides
  @Singleton
  World providesWorld(EventConsumerChainSet events) {
    return new World(events);
  }

  @Provides
  @Singleton
  @GameMessages
  MessageRegistrar providesGameMessageRegistrar(World world) {
    return new GameMessageRegistrar(world);
  }

  @Provides
  @Singleton
  @LoginMessages
  MessageRegistrar providesLoginMessageRegistrar(World world) {
    return new LoginMessageRegistrar(world);
  }

  @Provides
  @Singleton
  AuthenticationStrategy providesAuthenticationStrategy() {
    return AuthenticationService.DEFAULT_STRATEGY;
  }

  @Provides
  @Singleton
  AuthenticationService providesAuthenticationService(GameService gameService, AuthenticationStrategy strategy) {
    return new AuthenticationService(gameService, strategy);
  }

  @Provides
  @Singleton
  GameService providesGameService(World world) {
    return new GameService(world);
  }

  @Provides
  @Singleton
  GamePulseHandler providesGamePulseHandler(World world) {
    return new GamePulseHandler(world.getServices());
  }

  @Provides
  @Singleton
  ServerChannelInitializer providesChannelInitializer(
      @LoginMessages MessageRegistrar loginMessageRegistrar,
      @GameMessages MessageRegistrar gameMessageRegistrar) {
    return new ServerChannelInitializer(gameMessageRegistrar.build(), loginMessageRegistrar.build());
  }

  @Provides
  @Singleton
  ServerBootstrap providesBootstrap(ServerChannelInitializer initializer) {
    EventLoopGroup parentGroup = new NioEventLoopGroup();
    EventLoopGroup childGroup = new NioEventLoopGroup();

    return new ServerBootstrap()
        .channel(NioServerSocketChannel.class)
        .group(parentGroup, childGroup)
        .childHandler(initializer);
  }

}