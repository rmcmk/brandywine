package me.ryleykimmel.brandywine.network.frame;

import java.util.Optional;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageCodec;
import me.ryleykimmel.brandywine.network.message.MessageListener;

/**
 * Represents a mapping of {@link Message}s to {@link FrameMetadata}.
 *
 * @param <T> The Message type.
 */
public final class FrameMapping<T extends Message> {

  /**
   * The MessageCodec this FrameMapping is mapped to.
   */
  private final MessageCodec<T> codec;

  /**
   * The MessageListener this FrameMapping is mapped to.
   */
  private final MessageListener<T> listener;

  /**
   * The Message class this FrameMapping is mapped to.
   */
  private final Class<T> messageClass;

  /**
   * The FrameMetadata this FrameMapping is mapped to.
   */
  private final FrameMetadata metadata;

  /**
   * Constructs a new {@link FrameMapping}.
   *
   * @param messageClass The MessageCodec this FrameMapping is mapped to.
   * @param codec The Message class this FrameMapping is mapped to.
   * @param listener The MessageListener this FrameMapping is mapped to.
   * @param metadata The FrameMetadata this FrameMapping is mapped to.
   */
  private FrameMapping(Class<T> messageClass,
      MessageCodec<T> codec,
      MessageListener<T> listener,
      FrameMetadata metadata) {
    this.messageClass = messageClass;
    this.codec = codec;
    this.listener = listener;
    this.metadata = metadata;
  }

  /**
   * Create a new {@link FrameMapping} with no {@link MessageListener}.
   *
   * @param messageClass The MessageCodec this FrameMapping is mapped to.
   * @param codec The Message class this FrameMapping is mapped to.
   * @param opcode The opcode of the Frame.
   * @param length The length, in bytes, of the Frame.
   * @param <T> The Message type.
   * @return A newly constructed {@link FrameMapping}, never {@code null}.
   */
  public static <T extends Message> FrameMapping<T> create(Class<T> messageClass,
      MessageCodec<T> codec,
      int opcode,
      int length) {
    return new FrameMapping<>(messageClass, codec, null, new FrameMetadata(opcode, length));
  }

  /**
   * Create a new {@link FrameMapping}.
   *
   * @param messageClass The MessageCodec this FrameMapping is mapped to.
   * @param codec The Message class this FrameMapping is mapped to.
   * @param listener The MessageListener this FrameMapping is mapped to.
   * @param opcode The opcode of the Frame.
   * @param length The length, in bytes, of the Frame.
   * @param <T> The Message type.
   * @return A newly constructed {@link FrameMapping}, never {@code null}.
   */
  public static <T extends Message> FrameMapping<T> create(Class<T> messageClass,
      MessageCodec<T> codec,
      MessageListener<T> listener,
      int opcode,
      int length) {
    return new FrameMapping<>(messageClass, codec, listener, new FrameMetadata(opcode, length));
  }

  /**
   * Gets the MessageCodec this FrameMapping is mapped to.
   *
   * @return The MessageCodec this FrameMapping is mapped to.
   */
  public Class<T> getMessageClass() {
    return messageClass;
  }

  /**
   * Gets the MessageCodec this FrameMapping is mapped to.
   *
   * @return The MessageCodec this FrameMapping is mapped to.
   */
  public MessageCodec<T> getCodec() {
    return codec;
  }

  /**
   * Gets the MessageListener this FrameMapping is mapped to.
   *
   * @return The MessageListener this FrameMapping is mapped to.
   */
  public Optional<MessageListener<T>> getListener() {
    return Optional.ofNullable(listener);
  }

  /**
   * Gets the FrameMetadata this FrameMapping is mapped to.
   *
   * @return The FrameMetadata this FrameMapping is mapped to.
   */
  public FrameMetadata getMetadata() {
    return metadata;
  }

}
