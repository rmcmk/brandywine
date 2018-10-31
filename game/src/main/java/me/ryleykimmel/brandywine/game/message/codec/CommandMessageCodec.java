package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.CommandMessage;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link CommandMessage}.
 */
public final class CommandMessageCodec extends MessageCodec<CommandMessage> {

  @Override
  public CommandMessage decode(FrameReader reader) {
    return new CommandMessage(reader.getString());
  }

}
