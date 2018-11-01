package me.ryleykimmel.brandywine.network.message;

import me.ryleykimmel.brandywine.network.Session;

/**
 * Listens for {@link Message}s from a {@link Session}.
 */
public interface MessageReceivedListener {

  /**
   * Invoked when a Message has been received by a Session.
   *
   * @param session The Session that the {@code message} was received from.
   * @param message The specific Message which called this callback.
   */
  void messageReceived(Session session, Message message);

}