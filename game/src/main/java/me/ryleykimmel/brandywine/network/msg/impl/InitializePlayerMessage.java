package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} which sends initialization flags.
 */
public final class InitializePlayerMessage implements Message {

  /**
   * The membership status of the Player.
   */
  private final boolean member;

  /**
   * The index of the Player.
   */
  private final int index;

  /**
   * Constructs a new {@link InitializePlayerMessage} with the specified membership status and
   * internal index.
   * 
   * @param member The membership status.
   * @param index The internal index.
   */
  public InitializePlayerMessage(boolean member, int index) {
    this.member = member;
    this.index = index;
  }

  /**
   * Gets the membership status of the Player.
   * 
   * @return {@code true} if the Player is a member otherwise {@code false}.
   */
  public boolean isMember() {
    return member;
  }

  /**
   * Gets the internal index of the Player.
   * 
   * @return The internal index of the Player.
   */
  public int getIndex() {
    return index;
  }

}
