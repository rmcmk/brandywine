package me.ryleykimmel.brandywine.game.auth;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.sql2o.Sql2o;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.Service;
import me.ryleykimmel.brandywine.game.auth.impl.SQLAuthenticationStrategy;

/**
 * Services AuthenticationRequests every pulse.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class AuthenticationService extends Service {

	/**
	 * How often the requests are serviced, in milliseconds.
	 */
	private static final long PULSE_INTERVAL = 100L;

	/**
	 * The maximum number of requests allowed to be serviced per pulse.
	 */
	private static final int REQUESTS_PER_PULSE = 50;

	/**
	 * A Queue of AuthenticationRequests.
	 */
	private final Queue<AuthenticationRequest> requests = new ConcurrentLinkedQueue<>();

	/**
	 * An {@link ExecutorService} for executing {@link AuthenticationWorker}s.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat(StringUtil.simpleClassName(this)).build());

	/**
	 * A jdbc-SQL AuthenticationStrategy.
	 */
	private final AuthenticationStrategy sqlStrategy;

	/**
	 * Constructs a new {@link AuthenticationService} with the specified ServerContext.
	 *
	 * @param context The context of the Server.
	 */
	public AuthenticationService(ServerContext context) {
		super(context, PULSE_INTERVAL);

		sqlStrategy = new SQLAuthenticationStrategy(new Sql2o(context.getDatabaseAddress(), context.getDatabaseUsername(), context.getDatabasePassword()));
	}

	/**
	 * Submits the specified AuthenticationRequest.
	 *
	 * @param request The AuthenticationRequest to submit.
	 */
	public void submit(AuthenticationRequest request) {
		requests.offer(request);
	}

	@Override
	public void execute() {
		for (int count = 0; count < REQUESTS_PER_PULSE; count++) {
			AuthenticationRequest request = requests.poll();
			if (request == null) {
				break;
			}

			executor.submit(new AuthenticationWorker(context, sqlStrategy, request));
		}
	}

}