package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.FocusUpdateMessage;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link FocusUpdateMessage}.
 */
public final class FocusUpdateMessageCodec extends MessageCodec<FocusUpdateMessage> {

  @Override
  public FocusUpdateMessage decode(FrameReader frame) {
    boolean focused = (byte) frame.getUnsigned(DataType.BYTE) == 1;
    return new FocusUpdateMessage(focused);
  }

}
