package me.ryleykimmel.brandywine.network;

import io.netty.channel.ChannelHandlerContext;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.server.Server;

public final class GenericSessionHandler extends SessionHandler<GenericSession> {

  private final Server server;

  public GenericSessionHandler(Server server, GenericSession session) {
    super(session);
    this.server = server;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message message) {
    server.notify(message);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {

  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {

  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

  }

}
