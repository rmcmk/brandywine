package me.ryleykimmel.brandywine.network.game;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.UniqueIpFilter;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

@Sharable
public final class ChannelRemoteAddressFilter extends UniqueIpFilter {

	@Override
	public ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress address) {
		// TODO: Log rejected address
		return ctx.write(new LoginResponseMessage(LoginResponseMessage.STATUS_TOO_MANY_CONNECTIONS));
	}

}