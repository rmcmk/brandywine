package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which initiates login to the server.
 */
public final class LoginHandshakeMessage implements Message {

  /**
   * The hash of the connecting clients username.
   */
  private final int nameHash;

  /**
   * Constructs a new {@link LoginHandshakeMessage} with the specified name hash.
   *
   * @param nameHash The hash of the connecting clients username.
   */
  public LoginHandshakeMessage(int nameHash) {
    this.nameHash = nameHash;
  }

  /**
   * Gets the hash of the connecting clients username.
   *
   * @return The hash of the connecting clients username.
   */
  public int getNameHash() {
    return nameHash;
  }

}
