package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.game.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.Decodes;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.CommandMessage;

/**
 * Decodes the {@link CommandMessage}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Decodes(103)
public final class CommandMessageDecoder implements MessageDecoder<CommandMessage> {

  @Override
  public CommandMessage decode(Frame frame) {
    FrameReader reader = new FrameReader(frame);
    return new CommandMessage(reader.getString());
  }

}
