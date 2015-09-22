package me.ryleykimmel.brandywine.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.Service;

/**
 * Pulses game functions at a fixed rate.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
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
	 * A ScheduledExecutorService for scheduling this task.
	 */
	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(StringUtil.simpleClassName(this)).build());

	/**
	 * The context of the Server.
	 */
	private final ServerContext context;

	/**
	 * Constructs a new {@link GamePulseHandler} with the specified ServerContext.
	 *
	 * @param context The context of the Server.
	 */
	public GamePulseHandler(ServerContext context) {
		this.context = context;
	}

	/**
	 * Initializes this GamePulseHandler.
	 */
	public void init() {
		executor.scheduleAtFixedRate(this, PULSE_DELAY, PULSE_DELAY, TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		for (Service service : context.getServices()) {
			try {
				long elapsed = service.pulse();
				long diff = service.getInterval() - elapsed;
				if (diff < 0) {
					logger.warn("{} is being overloaded by {}% total {}ms", service.toString(), Math.abs(diff), elapsed);
				}
			} catch (Throwable cause) {
				logger.error("Erorr occured while executing service.", cause);
			}
		}
	}

}