package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moandjiezana.toml.Toml;

import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.common.Suppliers;
import me.ryleykimmel.brandywine.common.util.ClassUtil;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.msg.Decodes;
import me.ryleykimmel.brandywine.network.msg.Encodes;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.MessageEncoder;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * Parses MessageEncoders MessageDecoders.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class MessageCodecParser extends TomlParser {

	/**
	 * A default implementation of a Message.
	 *
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 */
	private static final class DefaultMessage implements Message {
	}

	/**
	 * A default implementation of a MessageDecoder.
	 *
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 */
	private static final class DefaultMessageDecoder implements MessageDecoder<Message> {

		/**
		 * Represents a default Message.
		 */
		private static final Message DEFAULT_MESSAGE = new DefaultMessage();

		@Override
		public Message decode(Frame frame) {
			logger.warn("The {} has no attached MessageDecoder.", frame);
			return DEFAULT_MESSAGE;
		}

	}

	/**
	 * A default implementation of a MessageEncoder.
	 *
	 * @author Ryley Kimmel <ryley.kimmel@live.com>
	 */
	private static final class DefaultMessageEncoder implements MessageEncoder<Message> {

		/**
		 * Represents a default Frame.
		 */
		private static final Frame DEFAULT_FRAME = new Frame(-1);

		@Override
		public Frame encode(Message message) {
			logger.error("The {} has no attached MessageEncoder or is not intended to be encoded.", message);
			return DEFAULT_FRAME;
		}

	}

	/**
	 * The maximum amount of Messages.
	 */
	private static final int MAXIMUM_MESSAGES = 256;

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageCodecParser.class);

	/**
	 * Represents the default MessageDecoder, used when a MessageDecoder mapping is not found.
	 */
	private static final MessageDecoder<Message> DEFAULT_MESSAGE_DECODER = new DefaultMessageDecoder();

	/**
	 * Represents the default MessageEncoder, used when a MessageEncoder mapping is not found.
	 */
	private static final MessageEncoder<Message> DEFAULT_MESSAGE_ENCODER = new DefaultMessageEncoder();

	/**
	 * A {@link Map} of Message types to MessageEncoders.
	 */
	private final Map<Class<? extends Message>, MessageEncoder<? extends Message>> encoders = new HashMap<>(MAXIMUM_MESSAGES);

	/**
	 * A {@link Map} of Frame opcodes to MessageDecoders.
	 */
	private final Map<Integer, MessageDecoder<? extends Message>> decoders = new HashMap<>(MAXIMUM_MESSAGES);

	/**
	 * Constructs a new {@link MessageCodecParser} with the specified path.
	 *
	 * @param path The path to the source.
	 */
	public MessageCodecParser(String path) {
		super(path);
	}

	/**
	 * Appends the specified MessageEncoder to its respective map.
	 *
	 * @param encoder The MessageEncoder to append.
	 */
	private <T extends Message> void append(MessageEncoder<T> encoder) {
		Optional<Encodes> optional = ClassUtil.getAnnotation(encoder, Encodes.class);
		Encodes annotation = optional.orElseThrow(Suppliers.illegal(StringUtil.simpleClassName(encoder) + " is not annotated with @Encodes"));
		encoders.put(annotation.value(), encoder);
	}

	/**
	 * Appends the specified MessageDecoder to its respective map.
	 *
	 * @param encoder The MessageDecoder to append.
	 */
	private <T extends Message> void append(MessageDecoder<T> decoder) {
		Optional<Decodes> optional = ClassUtil.getAnnotation(decoder, Decodes.class);
		Decodes annotation = optional.orElseThrow(Suppliers.illegal(StringUtil.simpleClassName(decoder) + " is not annotated with @Decodes"));
		IntStream.of(annotation.value()).forEach(id -> decoders.put(id, decoder));
	}

	/**
	 * Encodes the specified Message into a Frame.
	 *
	 * @param message The Message to encode.
	 * @return The encoded Frame, wrapped in an Optional.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Message> Frame encode(T message) {
		MessageEncoder<T> encoder = (MessageEncoder<T>) encoders.getOrDefault(message.getClass(), DEFAULT_MESSAGE_ENCODER);
		return encoder.encode(message);
	}

	/**
	 * Decodes the specified Frame into a Message.
	 *
	 * @param frame The Frame to decode.
	 * @return The decoded Message, wrapped in an Optional.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Message> T decode(Frame frame) {
		MessageDecoder<T> decoder = (MessageDecoder<T>) decoders.getOrDefault(frame.getOpcode(), DEFAULT_MESSAGE_DECODER);
		return decoder.decode(frame);
	}

	@Override
	public void parse(Reader source, Toml data) throws Exception {
		List<String> encoders = data.getList("codecs.encoders");
		List<String> decoders = data.getList("codecs.decoders");

		for (String encoder : encoders) {
			Class<?> clazz = Class.forName(encoder);
			MessageEncoder<?> instance = (MessageEncoder<?>) clazz.newInstance();
			append(instance);
		}

		for (String decoder : decoders) {
			Class<?> clazz = Class.forName(decoder);
			MessageDecoder<?> instance = (MessageDecoder<?>) clazz.newInstance();
			append(instance);
		}
	}

}