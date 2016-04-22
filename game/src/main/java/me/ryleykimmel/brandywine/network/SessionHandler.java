package me.ryleykimmel.brandywine.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.ryleykimmel.brandywine.network.msg.Message;

public abstract class SessionHandler<T extends Session>
    extends SimpleChannelInboundHandler<Message> {

  protected final T session;

  public SessionHandler(T session) {
    this.session = session;
  }

  @Override
  protected abstract void channelRead0(ChannelHandlerContext ctx, Message message);

  @Override
  public abstract void channelActive(ChannelHandlerContext ctx);

  @Override
  public abstract void channelInactive(ChannelHandlerContext ctx);

  @Override
  public abstract void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);

}
