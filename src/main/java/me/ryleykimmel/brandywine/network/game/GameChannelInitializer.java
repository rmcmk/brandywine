package me.ryleykimmel.brandywine.network.game;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.network.NetworkConstants;
import me.ryleykimmel.brandywine.network.game.frame.FrameDecoder;
import me.ryleykimmel.brandywine.network.game.frame.FrameEncoder;

/**
 * Initializes a {@link SocketChannel} for the GameService.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class GameChannelInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * The context of the Server.
	 */
	private final ServerContext context;

	/**
	 * Constructs a new {@link GameChannelInitializer} with the specified ServerContext.
	 *
	 * @param context The context of the Server.
	 */
	public GameChannelInitializer(ServerContext context) {
		this.context = context;
	}

	@Override
	protected void initChannel(SocketChannel channel) {
		GameSession session = new GameSession(context, channel);

		channel.pipeline().addLast(new FrameEncoder(session), new MessageEncoder(session), new FrameDecoder(session), new MessageDecoder(session), new GameSessionHandler(session),
				new IdleStateHandler(NetworkConstants.IDLE_TIME, 0, 0));
	}

}