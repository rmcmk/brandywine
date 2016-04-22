package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.CommandMessage;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link CommandMessage}.
 */
public final class CommandMessageCodec implements MessageCodec<CommandMessage> {

  @Override
  public CommandMessage decode(FrameReader reader) {
    return new CommandMessage(reader.getString());
  }

  @Override
  public void encode(CommandMessage message, FrameBuilder builder) {

  }

}
