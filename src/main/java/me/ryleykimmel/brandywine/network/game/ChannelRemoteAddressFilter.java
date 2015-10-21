package me.ryleykimmel.brandywine.network.game;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;

@Sharable
public final class ChannelRemoteAddressFilter extends AbstractRemoteAddressFilter<InetSocketAddress> {

	private final Multiset<InetAddress> connected = ConcurrentHashMultiset.create();

	private final ServerContext context;

	public ChannelRemoteAddressFilter(ServerContext context) {
		this.context = context;
	}

	@Override
	protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress address) {
		InetAddress remoteAddress = address.getAddress();

		if (connected.count(remoteAddress) < context.getConnectionLimit()) {
			connected.add(remoteAddress);
			ctx.channel().closeFuture().addListener(future -> connected.remove(remoteAddress));
			return true;
		}

		return false;
	}

	@Override
	public ChannelFuture channelRejected(ChannelHandlerContext ctx, InetSocketAddress address) {
		// TODO: Log rejected address
		return ctx.write(new LoginResponseMessage(LoginResponseMessage.STATUS_TOO_MANY_CONNECTIONS));
	}

}