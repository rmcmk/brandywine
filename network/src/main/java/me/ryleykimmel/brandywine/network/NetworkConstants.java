package me.ryleykimmel.brandywine.network;

import io.netty.channel.Channel;

/**
 * A class representing common network related constants.
 */
public final class NetworkConstants {

  /**
   * The amount of time, in seconds, until a {@link Channel} is considered idle.
   */
  public static final int IDLE_TIME = 15;

  /**
   * The maximum length, in bytes, a request may be.
   */
  public static final int MAXIMUM_REQUEST_LENGTH = 8192;

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private NetworkConstants() {
  }

}
