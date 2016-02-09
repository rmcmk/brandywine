package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.moandjiezana.toml.Toml;

import io.netty.util.internal.StringUtil;
import me.ryleykimmel.brandywine.common.Suppliers;
import me.ryleykimmel.brandywine.common.util.ClassUtil;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Handles;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.MessageHandler;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * Parses MessageHandlers.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class MessageHandlerParser extends TomlParser {

  /**
   * A default implementation of a GameMessageHandler.
   *
   * @author Ryley Kimmel <ryley.kimmel@live.com>
   */
  private static final class DefaultMessageHandler implements MessageHandler<Message> {

    @Override
    public void handle(GameSession session, Message message) {}

  }

  /**
   * Represents a default MessageHandler.
   */
  private static final MessageHandler<Message> DEFAULT_MESSAGE_HANDLER =
      new DefaultMessageHandler();

  /**
   * The maximum amount of MessageHandlers.
   */
  private static final int MAXIMUM_HANDLERS = 256;

  /**
   * A {@link Map} of Message types to MessageHandlers.
   */
  private final Map<Class<? extends Message>, MessageHandler<Message>> handlers =
      new HashMap<>(MAXIMUM_HANDLERS);

  /**
   * Constructs a new {@link MessageCodecParser} with the specified path.
   *
   * @param path The path to the source.
   */
  public MessageHandlerParser(String path) {
    super(path);
  }

  /**
   * Appends the specified MessageHandler to its respective map.
   *
   * @param handler The MessageHandler to append.
   */
  protected final void append(MessageHandler<Message> handler) {
    Optional<Handles> optional = ClassUtil.getAnnotation(handler, Handles.class);
    Handles annotation = optional.orElseThrow(
        Suppliers.illegal(StringUtil.simpleClassName(handler) + " is not annotated with @Handles"));
    handlers.put(annotation.value(), handler);
  }

  /**
   * Gets some MessageHandler for the specified Message.
   *
   * @param message The Message.
   * @return The MessageHandler for the Message, never {@code null}.
   */
  public MessageHandler<Message> getHandler(Message message) {
    return handlers.getOrDefault(message.getClass(), DEFAULT_MESSAGE_HANDLER);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void parse(Reader source, Toml data) throws Exception {
    List<String> handlers = data.getList("codecs.handlers");
    for (String handler : handlers) {
      Class<?> clazz = Class.forName(handler);
      MessageHandler<Message> instance = (MessageHandler<Message>) clazz.newInstance();
      append(instance);
    }
  }

}
