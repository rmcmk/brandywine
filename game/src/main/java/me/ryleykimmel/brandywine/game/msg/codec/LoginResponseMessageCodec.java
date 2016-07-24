package me.ryleykimmel.brandywine.game.msg.codec;

import me.ryleykimmel.brandywine.game.msg.LoginResponseMessage;
import me.ryleykimmel.brandywine.network.ResponseCode;
import me.ryleykimmel.brandywine.network.frame.DataType;
import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;

/**
 * MessageCodec for the {@link LoginResponseMessage}.
 */
public final class LoginResponseMessageCodec extends MessageCodec<LoginResponseMessage> {

  @Override
  public void encode(LoginResponseMessage message, FrameBuilder builder) {
    ResponseCode response = message.getResponse();
    builder.put(DataType.BYTE, response.getCode());
    if (response == ResponseCode.STATUS_OK) {
      builder.put(DataType.BYTE, message.getPrivilege());
      builder.put(DataType.BYTE, message.isFlagged() ? 1 : 0);
    }
  }

}
