package me.ryleykimmel.brandywine.game.login;

import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.network.Session;

public final class LoginSession extends Session {

  public LoginSession(SocketChannel channel) {
    super(channel);
  }

}
