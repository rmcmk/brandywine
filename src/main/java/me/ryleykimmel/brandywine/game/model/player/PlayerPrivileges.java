package me.ryleykimmel.brandywine.game.model.player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;

import com.google.common.base.Preconditions;

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

  // gets the id of the primary rank, the primary rank is the _only_ one displayed within the client
  public int getPrimaryId() {
    PlayerPrivilege head = privileges.peek();
    return head.getId();
  }

  public void assignPrimary(PlayerPrivilege privilege) {
    Preconditions.checkArgument(privileges.contains(privilege),
        "You cannot assign a primary privilege that does not exist!");

    // the head is already the primary privilege, do nothing
    if (isPrimary(privilege)) {
      return;
    }

    privileges.remove(privilege);
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
    return privileges.contains(privilege);
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

}
