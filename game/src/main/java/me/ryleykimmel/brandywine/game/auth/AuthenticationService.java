package me.ryleykimmel.brandywine.game.auth;

import com.google.common.base.Preconditions;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import me.ryleykimmel.brandywine.service.Service;
import me.ryleykimmel.brandywine.common.util.ThreadFactoryUtil;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.network.ResponseCode;

/**
 * Services AuthenticationRequests every pulse.
 */
public final class AuthenticationService extends Service {

  /**
   * Represents the default {@link AuthenticationStrategy}.
   */
  public static final AuthenticationStrategy DEFAULT_STRATEGY = p -> ResponseCode.STATUS_OK;

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
  private final ExecutorService executor = Executors.newCachedThreadPool(
      ThreadFactoryUtil.create(this).build());

  /**
   * The service used to queue game requests.
   */
  private final GameService service;

  /**
   * The strategy used to authenticate {@link AuthenticationRequest}s.
   */
  private final AuthenticationStrategy strategy;

  /**
   * Constructs a new {@link AuthenticationService}.
   *
   * @param service The service used to queue game requests.
   * @param strategy The strategy used to authenticate {@link AuthenticationRequest}s.
   */
  public AuthenticationService(GameService service, AuthenticationStrategy strategy) {
    super(PULSE_INTERVAL);
    this.service = Preconditions.checkNotNull(service, "GameService may not be null.");
    this.strategy = Preconditions.checkNotNull(strategy, "AuthenticationStrategy may not be null.");
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

      executor.submit(new AuthenticationWorker(service, strategy, request));
    }
  }

}
