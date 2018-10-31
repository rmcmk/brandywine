package me.ryleykimmel.brandywine.game.message;

import me.ryleykimmel.brandywine.game.message.codec.*;
import me.ryleykimmel.brandywine.game.message.listener.ChatMessageListener;
import me.ryleykimmel.brandywine.game.message.listener.CommandMessageListener;
import me.ryleykimmel.brandywine.game.message.listener.MovementMessageListener;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageRegistrar;

import static me.ryleykimmel.brandywine.network.frame.FrameMetadata.VARIABLE_BYTE_LENGTH;
import static me.ryleykimmel.brandywine.network.frame.FrameMetadata.VARIABLE_SHORT_LENGTH;

/**
 * A registrar which manages the registration of in-game {@link Message}s to the game.
 */
public final class GameMessageRegistrar implements MessageRegistrar {

    /**
     * The World of this MessageRegistrar.
     */
    private final World world;

    /**
     * Constructs a new {@link GameMessageRegistrar}.
     *
     * @param world The World of this MessageRegistrar.
     */
    public GameMessageRegistrar(World world) {
        this.world = world;
    }

    @Override
    public FrameMetadataSet build() {
        FrameMetadataSet metadata = new FrameMetadataSet();
        metadata.register(PingMessage.class, new PingMessageCodec(), 0, 0);
        metadata.register(FocusUpdateMessage.class, new FocusUpdateMessageCodec(), 3, 1);
        metadata.register(OpenTabInterfaceMessage.class, new OpenTabInterfaceMessageCodec(), 71, 3);
        metadata.register(RebuildRegionMessage.class, new RebuildRegionMessageCodec(), 73, 4);
        metadata.register(ResetDestinationMessage.class, new ResetDestinationMessageCodec(), 78, 0);
        metadata.register(PlayerUpdateMessage.class, new PlayerUpdateMessageCodec(), 81, VARIABLE_SHORT_LENGTH);
        metadata.register(ArrowKeyMessage.class, new ArrowKeyMessageCodec(), 86, 4);
        metadata.register(CloseInterfaceMessage.class, new CloseInterfaceMessageCodec(), 130, 0);
        metadata.register(UpdateSkillMessage.class, new UpdateSkillMessageCodec(), 134, 6);
        metadata.register(MouseClickedMessage.class, new MouseClickedMessageCodec(), 241, 4);
        metadata.register(InitializePlayerMessage.class, new InitializePlayerMessageCodec(), 249, 3);
        metadata.register(ServerChatMessage.class, new ServerChatMessageCodec(), 253, VARIABLE_BYTE_LENGTH);

        MovementMessageCodec movementMessageCodec = new MovementMessageCodec();
        MovementMessageListener movementMessageListener = new MovementMessageListener();
        metadata.register(MovementMessage.class, movementMessageCodec, movementMessageListener, 164, VARIABLE_BYTE_LENGTH); // game movement
        metadata.register(MovementMessage.class, movementMessageCodec, movementMessageListener, 248, VARIABLE_BYTE_LENGTH); // minimap movement
        metadata.register(MovementMessage.class, movementMessageCodec, movementMessageListener, 98, VARIABLE_BYTE_LENGTH); // command movement

        metadata.register(ChatMessage.class, new ChatMessageCodec(), new ChatMessageListener(), 4, VARIABLE_BYTE_LENGTH);
        metadata.register(CommandMessage.class, new CommandMessageCodec(), new CommandMessageListener(), 103, VARIABLE_BYTE_LENGTH);

        SpamPacketMessageCodec spamMessageCodec = new SpamPacketMessageCodec();
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 77, VARIABLE_BYTE_LENGTH);
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 78, 0);
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 165, VARIABLE_BYTE_LENGTH);
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 189, 1);
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 210, 4);
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 226, VARIABLE_BYTE_LENGTH);
        metadata.register(SpamPacketMessage.class, spamMessageCodec, 121, 0);
        return metadata;
    }

}
