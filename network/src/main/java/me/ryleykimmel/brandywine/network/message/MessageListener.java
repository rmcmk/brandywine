package me.ryleykimmel.brandywine.network.message;

import me.ryleykimmel.brandywine.network.Session;

/**
 * A listener which listens for verified {@link Message}'s and acts on them.
 *
 * @param <T> The Message type.
 */
public interface MessageListener<T extends Message> {

  /**
   * Handles the specified Message for the specified Session.
   *
   * @param session The Session which received the Message.
   * @param message The Message to process.
   */
  void handle(Session session, T message);

}