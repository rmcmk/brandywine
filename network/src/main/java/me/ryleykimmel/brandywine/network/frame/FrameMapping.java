package me.ryleykimmel.brandywine.network.frame;

import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

public final class FrameMapping<T extends Message> {

  public static <T extends Message> FrameMapping<T> create(Class<T> messageClass,
      MessageCodec<T> codec, int opcode, int length) {
    return new FrameMapping<>(messageClass, codec, new FrameMetadata(opcode, length));
  }

  private final MessageCodec<T> codec;
  private final Class<T> messageClass;
  private final FrameMetadata metadata;

  private FrameMapping(Class<T> messageClass, MessageCodec<T> codec, FrameMetadata metadata) {
    this.messageClass = messageClass;
    this.codec = codec;
    this.metadata = metadata;
  }

  public Class<T> getMessageClass() {
    return messageClass;
  }

  public MessageCodec<T> getCodec() {
    return codec;
  }

  public FrameMetadata getMetadata() {
    return metadata;
  }

}
