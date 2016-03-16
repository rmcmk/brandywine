package me.ryleykimmel.brandywine.network.game.frame;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.moandjiezana.toml.Toml;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.common.util.TomlUtil;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;

/**
 * A collection of FrameMetadata for upstream and downstream Frames.
 */
public final class FrameMetadataSet {

  /**
   * The type of Frame.
   */
  public enum Type {

    /**
     * Represents an upstream Frame.
     */
    UPSTREAM,

    /**
     * Represents a downstream frame.
     */
    DOWNSTREAM;

    /**
     * The name of this Type.
     */
    private final String name = name().toLowerCase();

    @Override
    public String toString() {
      return name;
    }

  }

  /**
   * The Logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(FrameMetadataSet.class);

  /**
   * A {@link Map} of upstream or downstream Frame types to another mapping of Messages to
   * FrameMetadata.
   */
  private final Map<Type, Map<Class<? extends Message>, FrameMetadata>> frames =
      new HashMap<>(FrameMetadata.MAXIMUM_OPCODE);

  /**
   * A {@link Map} of Integer opcodes to Messages.
   */
  private final Map<Integer, Class<? extends Message>> messages =
      new HashMap<>(FrameMetadata.MAXIMUM_OPCODE);

  /**
   * A {@link Map} of Integer opcodes to MessageDecoders.
   */
  private final Map<Integer, MessageDecoder<? extends Message>> decoders =
      new HashMap<>(FrameMetadata.MAXIMUM_OPCODE);

  /**
   * A {@link Map} of Messages to MessageEncoders.
   */
  private final Map<Class<? extends Message>, MessageEncoder<? extends Message>> encoders =
      new HashMap<>(FrameMetadata.MAXIMUM_OPCODE);

  /**
   * A {@link Map} of Messages to MessageHandlers.
   */
  private final Map<Class<? extends Message>, MessageHandler<? extends Message>> handlers =
      new HashMap<>(FrameMetadata.MAXIMUM_OPCODE);

  /**
   * Constructs a new {@link FrameMetadataSet}.
   */
  public FrameMetadataSet() {
    init();
  }

  /**
   * Initializes the {@link #frames} array.
   */
  private void init() {
    Preconditions.checkState(frames.isEmpty(),
        "This FrameMetadataSet has already been initialized.");

    frames.put(Type.DOWNSTREAM, new HashMap<>(FrameMetadata.MAXIMUM_OPCODE));
    frames.put(Type.UPSTREAM, new HashMap<>(FrameMetadata.MAXIMUM_OPCODE));

    Path data = Paths.get("data");
    Toml toml = TomlUtil.read(data.resolve("frames.toml"));

    try {
      populate(Type.DOWNSTREAM, toml);
      populate(Type.UPSTREAM, toml);
    } catch (Exception cause) {
      logger.error("An error occured while initializing Frame metadata!", cause);
    }
  }

  /**
   * Populates the appropriate {@link Map} with FrameMetadata from the Toml representation.
   * 
   * @param type The FrameMetadata type.
   * @param data The Toml representation of the FrameMetdata.
   * @throws Exception If some exception occurs.
   */
  @SuppressWarnings("unchecked")
  private void populate(Type type, Toml data) throws Exception {
    List<Toml> tables = Preconditions.checkNotNull(data.getTables(type.toString()),
        "Tables for type: " + type + " do not exist.");

    for (Toml toml : tables) {
      String name = toml.getString("name");
      int opcode = toml.getLong("opcode", -1L).intValue();
      int length = toml.getLong("length", 0L).intValue();
      boolean headless = opcode == -1 && length == 0;
      boolean ciphered = toml.getBoolean("ciphered", true);

      if (toml.contains("message")) {
        Class<? extends Message> message =
            (Class<? extends Message>) Class.forName(toml.getString("message"));
        String messageName = StringUtil.simpleClassName(message);

        Map<Class<? extends Message>, FrameMetadata> map = frames.get(type);
        map.put(message, new FrameMetadata(name, opcode, length, ciphered));

        // All headless frames have -1 as their opcode, no need to put them in the map.
        if (!headless) {
          messages.put(opcode, message);
        }

        switch (type) {
          case DOWNSTREAM:
            if (toml.contains("decoder")) {
              Class<?> decoder = Class.forName(toml.getString("decoder"));
              decoders.put(opcode, (MessageDecoder<? extends Message>) decoder.newInstance());
              if (toml.contains("handler")) {
                Class<?> handler = Class.forName(toml.getString("handler"));
                handlers.put(message, (MessageHandler<? extends Message>) handler.newInstance());
              }
            } else {
              if (toml.contains("encoder")) {
                logger.warn(
                    "Downstream frame {{}, {}} expects a decoder however an encoder was found.",
                    opcode, messageName);
              } else {
                logger.warn("Downstream frame {{}, {}} has a message representation but no decoder",
                    opcode, messageName);
              }
            }
            break;

          case UPSTREAM:
            if (toml.contains("encoder")) {
              Class<?> encoder = Class.forName(toml.getString("encoder"));
              encoders.put(message, (MessageEncoder<? extends Message>) encoder.newInstance());
              if (toml.contains("handler")) {
                logger.warn(
                    "Upstream frame {{}, {}} has a handler, however handling upstream frames is currently not supported.",
                    opcode, messageName);
              }
            } else {
              if (toml.contains("decoder")) {
                logger.warn(
                    "Upstream frame {{}, {}} expects an encoder however a decoder was found.",
                    opcode, messageName);
              } else {
                logger.warn("Upsream frame {{}, {}} has a message representation but no encoder",
                    opcode, messageName);
              }
            }
            break;

          default:
            throw new UnsupportedOperationException(type + " is not a supported stream type.");
        }
      } else {
        logger.warn("Frame {} has no message representation and cannot be parsed.", opcode);
      }
    }

  }

  /**
   * Decodes the specified Frame into a Message.
   * 
   * @param frame The Frame to decode.
   * @return The decoded Message, never {@code null}.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> T decode(Frame frame) {
    MessageDecoder<T> decoder = (MessageDecoder<T>) decoders.get(frame.getOpcode());
    FrameReader reader = new FrameReader(frame);
    return decoder.decode(reader);
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
    FrameMetadata metadata = getMetadata(Type.UPSTREAM, message.getClass());
    MessageEncoder<T> encoder = (MessageEncoder<T>) encoders.get(message.getClass());
    FrameBuilder builder = new FrameBuilder(metadata, allocator);
    encoder.encode(message, builder);
    return builder.build();
  }

  /**
   * Gets the MessageHandler for the specified Message.
   * 
   * @param message The Message.
   * @return The MessageHandler, may be {@code null}.
   */
  @SuppressWarnings("unchecked")
  public <T extends Message> MessageHandler<T> getHandler(T message) {
    MessageHandler<T> handler = (MessageHandler<T>) handlers.get(message.getClass());
    return handler;
  }

  /**
   * Gets whether or not the specified opcode has FrameMetadata for the specified Type.
   * 
   * @param type The FrameMetadata type.
   * @param opcode The opcode of the Frame.
   * @return {@code true} if the opcode has metadata.
   */
  public boolean hasMetadata(Type type, int opcode) {
    Map<Class<? extends Message>, FrameMetadata> map = frames.get(type);
    return map.containsKey(messages.get(opcode));
  }

  /**
   * Gets the FrameMetadata for the specified Message and type.
   * 
   * @param type The FrameMetadata type.
   * @param message The Message representation of the Frame.
   * @return The FrameMetadata, if it exists.
   */
  public FrameMetadata getMetadata(Type type, Class<? extends Message> message) {
    Map<Class<? extends Message>, FrameMetadata> map = frames.get(type);
    return Preconditions.checkNotNull(map.get(message), type + " metadata for message: "
        + StringUtil.simpleClassName(message) + " does not exist.");
  }

  /**
   * Gets whether or not the specified opcode has FrameMetadata for the specified Type.
   * 
   * @param type The FrameMetadata type.
   * @param message The Message representation of the Frame.
   * @return {@code true} if the Message has metadata.
   */
  public boolean hasMetadata(Type type, Class<? extends Message> message) {
    Map<Class<? extends Message>, FrameMetadata> map = frames.get(type);
    return map.containsKey(message);
  }

  /**
   * Gets the FrameMetadata for the specified Message and type.
   * 
   * @param type The FrameMetadata type.
   * @param opcode The opcode of the Frame.
   * @return The FrameMetadata, if it exists.
   */
  public FrameMetadata getMetadata(Type type, int opcode) {
    Map<Class<? extends Message>, FrameMetadata> map = frames.get(type);
    Class<? extends Message> message = messages.get(opcode);
    return Preconditions.checkNotNull(map.get(message), "Metadata for type: " + type + ", message: "
        + StringUtil.simpleClassName(message) + " does not exist.");
  }

}
