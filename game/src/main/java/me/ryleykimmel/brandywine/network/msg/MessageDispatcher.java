package me.ryleykimmel.brandywine.network.msg;

/**
 * Dispatches {@link Message}s to their respective {@link MessageHandler}s.
 * 
 * @param <T> The MessageHandler type.
 */
@FunctionalInterface
public interface MessageDispatcher<T extends MessageHandler<Message>> {

  /**
   * Dispatches the specified MessageHandler.
   * 
   * @param handler The MessageHandler to dispatch.
   * @param message The Message.
   */
  void dispatch(T handler, Message message);

}
