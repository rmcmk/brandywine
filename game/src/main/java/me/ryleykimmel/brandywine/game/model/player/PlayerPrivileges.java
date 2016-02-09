package me.ryleykimmel.brandywine.game.model.player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;

public final class PlayerPrivileges {

  public enum PlayerPrivilege {

    NONE(0), MODERATOR(1), ADMINISTRATOR(2);

    private final int id;

    private PlayerPrivilege(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

  }

  private final Deque<PlayerPrivilege> privileges =
      new ArrayDeque<>(EnumSet.of(PlayerPrivilege.NONE)); // Everyone has 'none'

  /**
   * Whether or not these PlayerPrivileges are root.
   */
  private boolean root;

  public int getPrimaryId() {
    PlayerPrivilege primary = privileges.peek();
    return primary.getId();
  }

  public void assignPrimary(PlayerPrivilege privilege) {
    if (has(privilege)) {
      remove(privilege);
    }

    privileges.offerFirst(privilege);
  }

  public void assign(PlayerPrivilege privilege) {
    privileges.offer(privilege);
  }

  public void remove(PlayerPrivilege privilege) {
    privileges.remove(privilege);
  }

  public boolean isPrimary(PlayerPrivilege privilege) {
    return privileges.peek() == privilege;
  }

  public boolean has(PlayerPrivilege privilege) {
    return privileges.contains(privilege) || root;
  }

  public boolean hasAll(PlayerPrivilege... privileges) {
    for (PlayerPrivilege privilege : privileges) {
      if (!has(privilege)) {
        return false;
      }
    }

    return true;
  }

  public boolean hasAny(PlayerPrivilege... privileges) {
    for (PlayerPrivilege privilege : privileges) {
      if (has(privilege)) {
        return true;
      }
    }

    return false;
  }

  public boolean isRoot() {
    return root;
  }

  public void removeRoot() {
    root = false;
  }

  public boolean assignRoot() {
    if (!has(PlayerPrivilege.ADMINISTRATOR)) {
      return false;
    }

    return root = true;
  }

}
