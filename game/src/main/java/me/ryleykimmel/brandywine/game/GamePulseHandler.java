package me.ryleykimmel.brandywine.game;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import me.ryleykimmel.brandywine.Service;

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
   * A mapping of {@link Service} types to {@link Service}s.
   */
  private final Map<Class<? extends Service>, Service> services;

  /**
   * Constructs a new {@link GamePulseHandler}.
   * 
   * @param services The mapping of Services.
   */
  public GamePulseHandler(Map<Class<? extends Service>, Service> services) {
    this.services =
        ImmutableMap.copyOf(Preconditions.checkNotNull(services, "Service map may not be null."));
  }

  @Override
  public void run() {
    for (Service service : services.values()) {
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
