package me.ryleykimmel.brandywine.network;

import io.netty.channel.socket.SocketChannel;

public final class GenericSession extends Session {

  public GenericSession(SocketChannel channel) {
    super(channel);
  }

}
