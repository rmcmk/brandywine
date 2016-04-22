package me.ryleykimmel.brandywine.game.login;

import io.netty.channel.ChannelHandlerContext;
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet;
import me.ryleykimmel.brandywine.network.SessionHandler;
import me.ryleykimmel.brandywine.network.msg.Message;

public final class LoginSessionHandler extends SessionHandler<LoginSession> {

  private final EventConsumerChainSet events;

  public LoginSessionHandler(EventConsumerChainSet events, LoginSession session) {
    super(session);
    this.events = events;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message message) {
    events.notify(message);
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
