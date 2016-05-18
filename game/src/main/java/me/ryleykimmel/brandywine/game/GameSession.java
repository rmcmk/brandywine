package me.ryleykimmel.brandywine.game;

import io.netty.channel.socket.SocketChannel;
import me.ryleykimmel.brandywine.game.event.Event;
import me.ryleykimmel.brandywine.game.event.EventConsumer;
import me.ryleykimmel.brandywine.game.event.EventConsumerChain;
import me.ryleykimmel.brandywine.game.event.EventConsumerChainSet;
import me.ryleykimmel.brandywine.network.Session;

public final class GameSession extends Session {

    /**
     * The {@link EventConsumerChainSet} for this GameSession.
     */
    private final EventConsumerChainSet events;

    /**
     * Constructs a new {@link GameSession}.
     *
     * @param channel The SocketChannel this GameSession is listening on.
     * @param events  The {@link EventConsumerChainSet} for this GameSession.
     */
    public GameSession(SocketChannel channel, EventConsumerChainSet events) {
        super(channel);
        this.events = events;
    }

    /**
     * Notifies the appropriate {@link EventConsumerChain} that an {@link Event} has occurred.
     *
     * @param event The Event.
     * @return {@code true} if the Event should continue on with its outcome.
     */
    public <E extends Event> boolean notify(E event) {
        return events.notify(event);
    }

}