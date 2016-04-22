package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * An encoder which encodes some Message into a {@link Frame}.
 *
 * @param <T> The Message type.
 */
interface MessageEncoder<T extends Message> {

  /**
   * Encodes the specified Message into a Frame.
   *
   * @param message The Message to encode.
   * @param builder The FrameBuilder used to build the Frame from the specified Message.
   */
  void encode(T message, FrameBuilder builder);

}
