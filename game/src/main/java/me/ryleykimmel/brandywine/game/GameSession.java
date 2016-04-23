package me.ryleykimmel.brandywine.game;

import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.network.Session;

public final class GameSession extends Session {

  public GameSession(SocketChannel channel) {
    super(channel);
  }

}