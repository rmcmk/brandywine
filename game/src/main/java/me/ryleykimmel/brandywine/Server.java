package me.ryleykimmel.brandywine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import me.ryleykimmel.brandywine.common.util.ThreadFactoryUtil;
import me.ryleykimmel.brandywine.game.GamePulseHandler;
import me.ryleykimmel.brandywine.game.model.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The core class of the Server.
 */
public final class Server {

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LogManager.getLogger(Server.class);

  /**
   * The entry point of this application.
   *
   * @param args The command-line arguments.
   */
  public static void main(String[] args) {
    ServerComponent server = DaggerServerComponent.create();
    World world = server.world();

    try {
      world.registerService(server.gameService());
      world.registerService(server.authenticationService());

      GamePulseHandler pulseHandler = server.gamePulseHandler();
      ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(ThreadFactoryUtil.create(pulseHandler).build());
      executor.scheduleAtFixedRate(pulseHandler, GamePulseHandler.PULSE_DELAY, GamePulseHandler.PULSE_DELAY, TimeUnit.MILLISECONDS);

      server.bootstrap()
          .bind(43594)
          .sync();
    } catch (InterruptedException cause) {
      logger.error("Unexpected error while starting Brandywine!", cause);
    }
  }

}