package me.ryleykimmel.brandywine.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import io.netty.util.internal.StringUtil;

/**
 * A representation of registered {@link Service}s.
 */
public final class ServiceSet {

  /**
   * A mapping of registered Services.
   */
  private final Map<Class<? extends Service>, Service> services = new HashMap<>();

  /**
   * Gets a Service from its type.
   *
   * @param clazz The type of the Service, may not be {@code null}.
   * @return The instance of the Service, never {@code null}.
   */
  @SuppressWarnings("unchecked")
  public <T extends Service> T get(Class<T> clazz) {
    Preconditions.checkNotNull(clazz, "Service type may not be null.");
    return (T) Preconditions.checkNotNull(services.get(clazz),
        "Service for: " + StringUtil.simpleClassName(clazz) + " does not exist.");
  }

  /**
   * Registers the specified Service.
   * 
   * @param service The Service to register, may not be {@code null}.
   */
  public void register(Service service) {
    Preconditions.checkNotNull(service, "Service may not be null.");
    services.put(service.getClass(), service);
  }

  /**
   * Gets an immutable shallow-copy {@link Set} of all {@link Service}s.
   *
   * @return An immutable {@link Set} of Services.
   */
  public Set<Service> getServices() {
    return ImmutableSet.copyOf(services.values());
  }

}
