package me.ryleykimmel.brandywine.network.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import me.ryleykimmel.brandywine.game.GameService;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A specialized {@link SimpleChannelInboundHandler} which only receives {@link Message messages}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Sharable
public final class GameSessionHandler extends SimpleChannelInboundHandler<Message> {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(GameSessionHandler.class);

	/**
	 * The GameSession for this ChannelHandler.
	 */
	private final GameSession session;

	/**
	 * Constructs a new {@link GameSessionHandler} with the specified GameSession.
	 *
	 * @param session The GameSession for this ChannelHandler.
	 */
	public GameSessionHandler(GameSession session) {
		this.session = session;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		GameService service = session.getContext().getService(GameService.class);
		Player player = session.attr().get();
		if (player != null) {
			World world = service.getWorld();
			world.removePlayer(player);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("An uncaught error has occured, closing session.", cause);
		session.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) {
		session.dispatch(message);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		if (evt instanceof IdleStateEvent) {
			session.close();
		}
	}

}