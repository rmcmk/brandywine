package me.ryleykimmel.brandywine.game.model.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ryleykimmel.brandywine.game.model.player.PlayerPrivileges.PlayerPrivilege;

public final class PlayerPrivilegeTest {

  private PlayerPrivileges privileges;

  @Before
  public void setup() {
    privileges = new PlayerPrivileges();
  }

  @Test
  public void testDefaults() {
    Assert.assertTrue(privileges.has(PlayerPrivilege.NONE));
    Assert.assertTrue(privileges.isPrimary(PlayerPrivilege.NONE));
  }

  @Test
  public void testAssignPrimary() {
    // Assign the privilege
    privileges.assign(PlayerPrivilege.MODERATOR);

    // Make it the primary privilege
    privileges.assignPrimary(PlayerPrivilege.MODERATOR);

    Assert.assertTrue(privileges.has(PlayerPrivilege.NONE));
    Assert.assertTrue(privileges.has(PlayerPrivilege.MODERATOR));
    Assert.assertTrue(privileges.isPrimary(PlayerPrivilege.MODERATOR));
  }

  @Test
  public void testHasAll() {
    // see if we have our only privilege...
    Assert.assertTrue(privileges.hasAll(PlayerPrivilege.NONE));

    // assign some more...
    privileges.assign(PlayerPrivilege.ADMINISTRATOR);
    privileges.assign(PlayerPrivilege.MODERATOR);

    Assert.assertTrue(privileges.hasAll(PlayerPrivilege.ADMINISTRATOR));
    Assert.assertTrue(privileges.hasAll(PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.MODERATOR));
    Assert.assertTrue(privileges.hasAll(PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.MODERATOR,
        PlayerPrivilege.NONE));
  }

  @Test
  public void testHasAny() {
    Assert.assertTrue(privileges.hasAny(PlayerPrivilege.NONE));
    Assert.assertTrue(privileges.hasAny(PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.MODERATOR,
        PlayerPrivilege.NONE));

    privileges.assign(PlayerPrivilege.ADMINISTRATOR);

    Assert.assertTrue(privileges.hasAny(PlayerPrivilege.ADMINISTRATOR));
    Assert.assertTrue(privileges.hasAny(PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.MODERATOR));
  }

}
