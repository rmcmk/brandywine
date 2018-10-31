package me.ryleykimmel.brandywine.game.message.codec;

import me.ryleykimmel.brandywine.game.message.ArrowKeyMessage;
import me.ryleykimmel.brandywine.network.frame.DataOrder;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link ArrowKeyMessage}.
 */
public final class ArrowKeyMessageCodec extends MessageCodec<ArrowKeyMessage> {

  @Override
  public ArrowKeyMessage decode(FrameReader frame) {
    int roll = (int) frame.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
    int yaw = (int) frame.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
    return new ArrowKeyMessage(roll, yaw);
  }

}
