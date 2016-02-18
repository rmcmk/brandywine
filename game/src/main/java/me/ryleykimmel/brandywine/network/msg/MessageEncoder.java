package me.ryleykimmel.brandywine.network.msg;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.game.frame.Frame;

/**
 * An encoder which encodes some Message into a {@link Frame}.
 *
 * @param <T> The Message type.
 */
public interface MessageEncoder<T extends Message> {

  /**
   * Encodes the specified Message into a Frame.
   *
   * @param message The Message to encode.
   * @param alloc The ByteBufAllocator used for allocating new ByteBufs.
   * @return The encoded Frame.
   */
  Frame encode(T message, ByteBufAllocator alloc);

}
