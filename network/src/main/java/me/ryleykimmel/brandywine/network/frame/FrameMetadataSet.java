package me.ryleykimmel.brandywine.network.frame;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBufAllocator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageCodec;
import me.ryleykimmel.brandywine.network.message.MessageListener;

/**
 * A collection of FrameMetadata for upstream and downstream Frames.
 */
public final class FrameMetadataSet {

  /**
   * A mapping of Message types to Frames.
   */
  private final Map<Class<? extends Message>, FrameMapping<? extends Message>> framesByType = new HashMap<>();

  /**
   * A mapping of opcodes to Frames.
   */
  private final Map<Integer, FrameMapping<? extends Message>> framesByOpcode = new HashMap<>();

  /**
   * Delegate for {@link FrameMetadataSet#register(FrameMapping)}.
   *
   * @param message The MessageCodec this FrameMapping is mapped to.
   * @param codec The Message class this FrameMapping is mapped to.
   * @param listener The MessageListener this FrameMapping is mapped to.
   * @param opcode The opcode of the Frame.
   * @param length The length, in bytes, of the Frame.
   * @param <T> The Message type.
   */
  public <T extends Message> void register(Class<T> message,
      MessageCodec<T> codec,
      MessageListener<T> listener,
      int opcode,
      int length) {
    register(FrameMapping.create(message, codec, listener, opcode, length));
  }

  /**
   * Delegate for {@link FrameMetadataSet#register(FrameMapping)}.
   *
   * @param message The MessageCodec this FrameMapping is mapped to.
   * @param codec The Message class this FrameMapping is mapped to.
   * @param opcode The opcode of the Frame.
   * @param length The length, in bytes, of the Frame.
   * @param <T> The Message type.
   */
  public <T extends Message> void register(Class<T> message,
      MessageCodec<T> codec,
      int opcode,
      int length) {
    register(FrameMapping.create(message, codec, opcode, length));
  }

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
  @SuppressWarnings("unchecked")
  public <T extends Message> T decode(Frame frame) {
    MessageCodec<T> codec = (MessageCodec<T>) getMapping(frame.getOpcode()).map(FrameMapping::getCodec).orElseThrow(
        () -> new NullPointerException("No FrameMapping found for opcode: " + frame.getOpcode()));
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
    FrameMapping<T> mapping = (FrameMapping<T>) getMapping(message.getClass()).orElseThrow(
        () -> new NullPointerException("No FrameMapping found for message: " + message.getClass().getSimpleName()));
    FrameMetadata metadata = mapping.getMetadata();
    FrameBuilder builder = new FrameBuilder(metadata, allocator);
    mapping.getCodec().encode(message, builder);
    return builder.build();
  }

  /**
   * Handles a decoded Message.
   *
   * @param message The decoded Message.
   * @param session The Session that received the Message.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> void handle(T message, Session session) {
    FrameMapping<T> mapping = (FrameMapping<T>) getMapping(message.getClass()).orElse(null);
    if (mapping == null) {
      return;
    }
    mapping.getListener().ifPresent(listener -> listener.handle(session, message));
  }

  /**
   * Gets the FrameMetadata for a Message class.
   *
   * @param clazz The Message type.
   * @return The FrameMetadata for the specified Message.
   */
  public <T extends Message> FrameMetadata getMetadata(Class<T> clazz) {
    return getMapping(clazz).orElseThrow(
        () -> new IllegalArgumentException("Mapping for " + clazz.getSimpleName() + " not found.")).getMetadata();
  }

  /**
   * Gets the FrameMetadata for an opcode.
   *
   * @param opcode The opcode.
   * @return The FrameMetadata for the specified opcode.
   */
  public FrameMetadata getMetadata(int opcode) {
    return getMapping(opcode).orElseThrow(
        () -> new IllegalArgumentException("Mapping for " + opcode + " not found.")).getMetadata();
  }

  /**
   * Gets a FrameMapping for a Message class.
   *
   * @param clazz The Message type.
   * @return The FrameMapping for the specified Message.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> Optional<FrameMapping<T>> getMapping(Class<T> clazz) {
    return Optional.ofNullable((FrameMapping<T>) framesByType.get(clazz));
  }

  /**
   * Gets a FrameMapping for an opcode.
   *
   * @param opcode The opcode.
   * @return The FrameMapping for the specified opcode.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> Optional<FrameMapping<T>> getMapping(int opcode) {
    return Optional.ofNullable((FrameMapping<T>) framesByOpcode.get(opcode));
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
