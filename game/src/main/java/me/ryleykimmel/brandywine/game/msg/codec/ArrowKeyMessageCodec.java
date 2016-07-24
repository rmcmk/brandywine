package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.ArrowKeyMessage;
import me.ryleykimmel.brandywine.network.frame.DataOrder;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link ArrowKeyMessage}.
 */
public final class ArrowKeyMessageCodec extends MessageCodec<ArrowKeyMessage> {

  @Override public ArrowKeyMessage decode(FrameReader frame) {
    int roll = (int) frame.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
    int yaw = (int) frame.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
    return new ArrowKeyMessage(roll, yaw);
  }

}
