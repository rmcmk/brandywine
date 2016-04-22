package me.ryleykimmel.brandywine.network.msg.codec;

import me.ryleykimmel.brandywine.network.frame.FrameBuilder;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.msg.impl.DummyMessage;

/**
 * MessageCodec for the {@link DummyMessage}.
 */
public final class DummyMessageCodec implements MessageCodec<DummyMessage> {

  /**
   * An instance of a {@link DummyMessage}.
   */
  private static final DummyMessage INSTANCE = new DummyMessage();

  @Override
  public DummyMessage decode(FrameReader reader) {
    return INSTANCE;
  }

  @Override
  public void encode(DummyMessage message, FrameBuilder builder) {

  }

}
