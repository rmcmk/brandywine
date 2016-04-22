package me.ryleykimmel.brandywine.network.frame;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * A collection of FrameMetadata for upstream and downstream Frames.
 */
public final class FrameMetadataSet {

  private final Map<Class<? extends Message>, FrameMapping<? extends Message>> framesByType =
      new HashMap<>();
  private final Map<Integer, FrameMapping<? extends Message>> framesByOpcode = new HashMap<>();

  public <T extends Message> void register(FrameMapping<T> mapping) {
    Preconditions.checkNotNull(mapping, "FrameMapping may not be null.");
    FrameMetadata metadata = mapping.getMetadata();
    framesByType.put(mapping.getMessageClass(), mapping);
    framesByOpcode.put(metadata.getOpcode(), mapping);
  }

  /**
   * Decodes the specified Frame into a Message.
   * 
   * @param frame The Frame to decode.
   * @return The decoded Message, never {@code null}.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> T decode(Frame frame) {
    MessageCodec<T> codec = (MessageCodec<T>) getMapping(frame.getOpcode()).getCodec();
    FrameReader reader = new FrameReader(frame);
    return codec.decode(reader);
  }

  /**
   * Encodes the specified Message into a Frame.
   * 
   * @param message The Message to encode.
   * @param allocator The ByteBufAllocator, for allocating ByteBufs.
   * @return The encoded Message, as a Frame. Never {@code null}.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> Frame encode(T message, ByteBufAllocator allocator) {
    FrameMapping<T> mapping = (FrameMapping<T>) getMapping(message.getClass());
    FrameMetadata metadata = mapping.getMetadata();
    FrameBuilder builder = new FrameBuilder(metadata, allocator);
    mapping.getCodec().encode(message, builder);
    return builder.build();
  }

  public <T extends Message> FrameMetadata getMetadata(Class<T> clazz) {
    return getMapping(clazz).getMetadata();
  }

  public FrameMetadata getMetadata(int opcode) {
    return getMapping(opcode).getMetadata();
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> FrameMapping<T> getMapping(Class<T> clazz) {
    Preconditions.checkNotNull(clazz, "Type may not be null.");
    Optional<FrameMapping<T>> mapping =
        Optional.ofNullable((FrameMapping<T>) framesByType.get(clazz));
    return mapping
        .orElseThrow(() -> new IllegalArgumentException("Mapping for " + clazz + " not found."));
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> FrameMapping<T> getMapping(int opcode) {
    Optional<FrameMapping<T>> mapping =
        Optional.ofNullable((FrameMapping<T>) framesByOpcode.get(opcode));
    return mapping
        .orElseThrow(() -> new IllegalArgumentException("Mapping for " + opcode + " not found."));
  }

}
