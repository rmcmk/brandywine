package me.ryleykimmel.brandywine.game.message;

import me.ryleykimmel.brandywine.game.message.codec.LoginHandshakeMessageCodec;
import me.ryleykimmel.brandywine.game.message.codec.LoginHandshakeResponseMessageCodec;
import me.ryleykimmel.brandywine.game.message.codec.LoginMessageCodec;
import me.ryleykimmel.brandywine.game.message.codec.LoginResponseMessageCodec;
import me.ryleykimmel.brandywine.game.message.listener.LoginHandshakeMessageListener;
import me.ryleykimmel.brandywine.game.message.listener.LoginMessageListener;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.network.frame.FrameMetadata;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;
import me.ryleykimmel.brandywine.network.message.Message;
import me.ryleykimmel.brandywine.network.message.MessageRegistrar;

/**
 * A registrar which manages the registration of pre-game {@link Message}s to the game.
 */
public final class LoginMessageRegistrar implements MessageRegistrar {

  /**
   * The World of this MessageRegistrar.
   */
  private final World world;

  /**
   * Constructs a new {@link LoginMessageRegistrar}.
   *
   * @param world The World of this MessageRegistrar.
   */
  public LoginMessageRegistrar(World world) {
    this.world = world;
  }

  @Override
  public FrameMetadataSet build() {
    FrameMetadataSet metadata = new FrameMetadataSet();
    metadata
        .register(LoginHandshakeResponseMessage.class, new LoginHandshakeResponseMessageCodec(), 1,
            17);
    metadata.register(LoginResponseMessage.class, new LoginResponseMessageCodec(), 2, 3);

    metadata.register(LoginHandshakeMessage.class, new LoginHandshakeMessageCodec(),
        new LoginHandshakeMessageListener(), 14, 1);

    LoginMessageCodec loginMessageCodec = new LoginMessageCodec();
    LoginMessageListener loginMessageListener = new LoginMessageListener(world);
    metadata.register(LoginMessage.class, loginMessageCodec, loginMessageListener, 16,
        FrameMetadata.VARIABLE_SHORT_LENGTH);
    metadata.register(LoginMessage.class, loginMessageCodec, loginMessageListener, 18,
        FrameMetadata.VARIABLE_SHORT_LENGTH);
    return metadata;
  }

}
