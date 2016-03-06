package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.game.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.DummyMessage;

/**
 * A {@link MessageDecoder} which decodes dummy messages.
 */
public final class DummyMessageDecoder implements MessageDecoder<DummyMessage> {

  /**
   * An instance of a {@link DummyMessage}.
   */
  private static final DummyMessage INSTANCE = new DummyMessage();

  @Override
  public DummyMessage decode(GameSession session, FrameReader reader) {
    return INSTANCE;
  }

}
