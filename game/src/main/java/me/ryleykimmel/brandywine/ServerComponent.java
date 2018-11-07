package me.ryleykimmel.brandywine;

import dagger.Component;
import io.netty.bootstrap.ServerBootstrap;
import javax.inject.Singleton;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationService;
import me.ryleykimmel.brandywine.game.auth.AuthenticationStrategy;
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.network.message.GameMessages;
import me.ryleykimmel.brandywine.network.message.LoginMessages;
import me.ryleykimmel.brandywine.network.message.MessageReceivedListener;
import me.ryleykimmel.brandywine.network.message.MessageRegistrar;

@Singleton
@Component(modules = ServerModule.class)
public interface ServerComponent {
  EventConsumerChainSet eventConsumerChainSet();
  World world();

  @GameMessages MessageRegistrar gameMessageRegistrar();
  @LoginMessages MessageRegistrar loginMessageRegistrar();

  AuthenticationStrategy authenticationStrategy();
  AuthenticationService authenticationService();

  GameService gameService();
  GamePulseHandler gamePulseHandler();

  ServerBootstrap bootstrap();
  ServerChannelInitializer initializer();

}