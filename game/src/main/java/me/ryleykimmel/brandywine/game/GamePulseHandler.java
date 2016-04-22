package me.ryleykimmel.brandywine.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ryleykimmel.brandywine.service.Service;
import me.ryleykimmel.brandywine.service.ServiceSet;

/**
 * Pulses game functions at a fixed rate.
 */
public final class GamePulseHandler implements Runnable {

  /**
   * The delay in milliseconds between pulses.
   */
  public static final long PULSE_DELAY = 100L;

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(GamePulseHandler.class);

  /**
   * A mapping of {@link Service}s.
   */
  private final ServiceSet services;

  /**
   * Constructs a new {@link GamePulseHandler}.
   * 
   * @param services The mapping of Services.
   */
  public GamePulseHandler(ServiceSet services) {
    this.services = services;
  }

  @Override
  public void run() {
    for (Service service : services.getServices()) {
      try {
        long elapsed = service.pulse();
        long diff = service.getInterval() - elapsed;
        if (diff < 0) {
          logger.warn("{} is being overloaded by {}% total {}ms", service.toString(),
              Math.abs(diff), elapsed);
        }
      } catch (Exception cause) {
        logger.error("Erorr occured while executing service.", cause);
      }
    }
  }

}
