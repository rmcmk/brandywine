package me.ryleykimmel.brandywine.network.msg;

import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;

/**
 * A joint implementation of a MessageEncoder and MessageDecoder.
 *
 * @param <T> The Message type.
 */
public abstract class MessageCodec<T extends Message>
  implements MessageEncoder<T>, MessageDecoder<T> {

  @Override public T decode(FrameReader frame) {
    throw new IllegalStateException("decode not supported for " + frame.getMetadata());
  }

  @Override public void encode(T message, FrameBuilder builder) {
    throw new IllegalStateException("encode not supported for " + message.getClass());
  }

}
