package me.ryleykimmel.brandywine;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.game.GamePulseHandler;

/**
 * Represents a Service that is executed every {@code n} milliseconds.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class Service {

	/**
	 * The current delay until this Service is executed.
	 */
	private int currentDelay;

	/**
	 * The context of the Server.
	 */
	protected final ServerContext context;

	/**
	 * The delay (number of intervals) until this Service is executed.
	 */
	private final int delay;

	/**
	 * This Service's interval, cached for debugging purposes.
	 */
	private final long interval;

	/**
	 * This Services internal Stopwatch, used for debugging purposes.
	 */
	private final Stopwatch stopwatch = Stopwatch.createUnstarted();

	/**
	 * Constructs a new {@link Service} with the specified ServerContext and a default interval of {@link GamePulseHandler#PULSE_DELAY}.
	 *
	 * @param context The context of the Server.
	 */
	public Service(ServerContext context) {
		this(context, GamePulseHandler.PULSE_DELAY);
	}

	/**
	 * Constructs a new {@link Service} with the specified ServerContext and interval.
	 *
	 * @param context The context of the Server.
	 * @param interval The interval, in milliseconds representing how often this Service will be executed.
	 */
	public Service(ServerContext context, long interval) {
		this.context = context;
		this.interval = interval;

		delay = (int) (interval / GamePulseHandler.PULSE_DELAY);
		currentDelay = Math.max(1, delay);
	}

	/**
	 * Pulses this Service, counting down the {@code currentDelay} until it reaches <tt>0</tt> then executes this Service.
	 *
	 * @throws Throwable If some exception occurs.
	 * @return How long this Service took to pulse this iteration, in milliseconds.
	 */
	public final long pulse() throws Throwable {
		try {
			stopwatch.start();

			if (currentDelay-- == 0) {
				execute();
				currentDelay = delay;
			}

			return stopwatch.elapsed(TimeUnit.MILLISECONDS);
		} finally {
			stopwatch.reset();
		}
	}

	/**
	 * Gets this Service's pulse interval.
	 * 
	 * @return This Service's pulse interval.
	 */
	public final long getInterval() {
		return interval;
	}

	@Override
	public final String toString() {
		return StringUtil.simpleClassName(this);
	}

	/**
	 * Executes logic for this Service.
	 *
	 * @throws Throwable If some exception occurs.
	 */
	public abstract void execute() throws Throwable;

}