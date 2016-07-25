package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.MouseClickedMessage;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link MouseClickedMessage}.
 */
public final class MouseClickedMessageCodec extends MessageCodec<MouseClickedMessage> {

  @Override
  public MouseClickedMessage decode(FrameReader frame) {
    int value = (int) frame.getUnsigned(DataType.INT);

    long delay = (value >> 20) * 50;
    boolean right = (value >> 19 & 0x1) == 1;

    int coordinates = value & 0x3FFFF;
    int x = coordinates % 765;
    int y = coordinates / 765;

    return new MouseClickedMessage(delay, right, x, y);
  }

}
