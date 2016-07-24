package me.ryleykimmel.brandywine.network.frame;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBufAllocator;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A collection of FrameMetadata for upstream and downstream Frames.
 */
public final class FrameMetadataSet {

  /**
   * A mapping of Message types to Frames.
   */
  private final Map<Class<? extends Message>, FrameMapping<? extends Message>> framesByType =
    new HashMap<>();

  /**
   * A mapping of opcodes to Frames.
   */
  private final Map<Integer, FrameMapping<? extends Message>> framesByOpcode = new HashMap<>();

  /**
   * Registers the specified FrameMapping.
   *
   * @param mapping The FrameMapping to register.
   */
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
  @SuppressWarnings("unchecked") public <T extends Message> T decode(Frame frame) {
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
  @SuppressWarnings("unchecked") public <T extends Message> Frame encode(T message,
    ByteBufAllocator allocator) {
    FrameMapping<T> mapping = (FrameMapping<T>) getMapping(message.getClass());
    FrameMetadata metadata = mapping.getMetadata();
    FrameBuilder builder = new FrameBuilder(metadata, allocator);
    mapping.getCodec().encode(message, builder);
    return builder.build();
  }

  /**
   * Gets the FrameMetadata for a Message class.
   *
   * @param clazz The Message type.
   * @return The FrameMetadata for the specified Message.
   */
  public <T extends Message> FrameMetadata getMetadata(Class<T> clazz) {
    return getMapping(clazz).getMetadata();
  }

  /**
   * Gets the FrameMetadata for an opcode.
   *
   * @param opcode The opcode.
   * @return The FrameMetadata for the specified opcode.
   */
  public FrameMetadata getMetadata(int opcode) {
    return getMapping(opcode).getMetadata();
  }

  /**
   * Gets a FrameMapping for a Message class.
   *
   * @param clazz The Message type.
   * @return The FrameMapping for the specified Message.
   */
  @SuppressWarnings("unchecked") public <T extends Message> FrameMapping<T> getMapping(
    Class<T> clazz) {
    Optional<FrameMapping<T>> mapping =
      Optional.ofNullable((FrameMapping<T>) framesByType.get(clazz));
    return mapping
      .orElseThrow(() -> new IllegalArgumentException("Mapping for " + clazz + " not found."));
  }

  /**
   * Gets a FrameMapping for an opcode.
   *
   * @param opcode The opcode.
   * @return The FrameMapping for the specified opcode.
   */
  @SuppressWarnings("unchecked") public <T extends Message> FrameMapping<T> getMapping(int opcode) {
    Optional<FrameMapping<T>> mapping =
      Optional.ofNullable((FrameMapping<T>) framesByOpcode.get(opcode));
    return mapping
      .orElseThrow(() -> new IllegalArgumentException("Mapping for " + opcode + " not found."));
  }

  /**
   * Tests whether or not an opcode has a FrameMapping.
   *
   * @param opcode The opcode.
   * @return {@code true} iff the opcode has a FrameMapping.
   */
  public boolean hasMapping(int opcode) {
    return framesByOpcode.containsKey(opcode);
  }

  /**
   * Tests whether or not a Message class has a FrameMapping.
   *
   * @param clazz The Message type.
   * @return {@code true} iff the Message has a FrameMapping.
   */
  public <T extends Message> boolean hasMapping(Class<T> clazz) {
    return framesByType.containsKey(clazz);
  }

}
