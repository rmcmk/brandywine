package me.ryleykimmel.brandywine.game;

import io.netty.channel.ChannelHandlerContext;
import me.ryleykimmel.brandywine.network.SessionHandler;
import me.ryleykimmel.brandywine.network.msg.Message;

public class GameSessionHandler extends SessionHandler<GameSession> {

  public GameSessionHandler(GameSession session) {
    super(session);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message message) {
    session.notify(message);
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
