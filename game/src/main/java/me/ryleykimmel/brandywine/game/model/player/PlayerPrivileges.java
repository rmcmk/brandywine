package me.ryleykimmel.brandywine.game.model.player;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents the Privilege's a Player may have.
 */
public final class PlayerPrivileges {

  /**
   * The Set of Privileges a Player has.
   */
  private final Set<Privilege> privileges = new HashSet<>(EnumSet.of(
    Privilege.NONE)); // Everyone has 'none'

  /**
   * Gets the id of the most senior (as specified by {@link Enum#compareTo(Enum)}) Crown.
   *
   * @return The id of the most senior Crown.
   */
  public int getCrownId() {
    return privileges.stream().max(Enum::compareTo).orElse(Privilege.NONE).getCrown().getId();
  }

  /**
   * Assigns the specified Privilege.
   *
   * @param privilege The Privilege to assign.
   */
  public void assign(Privilege privilege) {
    privileges.add(privilege);
  }

  /**
   * Removes the specified Privilege.
   *
   * @param privilege The Privilege to remove.
   */
  public void remove(Privilege privilege) {
    privileges.remove(privilege);
  }

  /**
   * Tests whether or not we have the specified Privilege.
   *
   * @param privilege The Privilege to test.
   * @return {@code true} iff we have the specified Privilege.
   */
  public boolean has(Privilege privilege) {
    return privileges.contains(privilege);
  }

  /**
   * Tests whether or not we have <strong>all of the</strong> specified Privileges.
   *
   * @param privileges The Privileges to test.
   * @return {@code true} iff we have <strong>all of the</strong> specified Privilege.
   */
  public boolean hasAll(Privilege... privileges) {
    for (Privilege privilege : privileges) {
      if (!has(privilege)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Tests whether or not we have <strong>any of the</strong> specified Privileges.
   *
   * @param privileges The Privileges to test.
   * @return {@code true} iff we have <strong>any of the</strong> specified Privilege.
   */
  public boolean hasAny(Privilege... privileges) {
    for (Privilege privilege : privileges) {
      if (has(privilege)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Represents a Privilege.
   */
  public enum Privilege {

    /**
     * No Privilege level, standard permissions.
     */
    NONE(),

    /**
     * Player moderator Privilege level.
     */
    MODERATOR(Crown.SILVER),

    /**
     * Player administrator Privilege level.
     */
    ADMINISTRATOR(Crown.GOLD);

    /**
     * The Crown displayed for this Privilege.
     */
    private final Crown crown;

    /**
     * Constructs a Privilege with no crown.
     */
    private Privilege() {
      this(Crown.NONE);
    }

    /**
     * Constructs a new Privilege with the specified Crown.
     *
     * @param crown The Crown for this Privilege.
     */
    private Privilege(Crown crown) {
      this.crown = crown;
    }

    /**
     * Gets the Crown for this Privilege.
     *
     * @return The Crown for this Privilege.
     */
    public Crown getCrown() {
      return crown;
    }

  }


  /**
   * Represents the Crown displayed for a Privilege.
   */
  public enum Crown {

    /**
     * Represents no Crown.
     */
    NONE(0),

    /**
     * Represents the silver, moderator Crown.
     */
    SILVER(1),

    /**
     * Represents the gold, administrator Crown.
     */
    GOLD(2);

    /**
     * The id of this Crown.
     */
    private final int id;

    /**
     * Constructs a new Crown.
     *
     * @param id The id of the Crown.
     */
    private Crown(int id) {
      this.id = id;
    }

    /**
     * Gets the id of this Crown.
     *
     * @return The id of this Crown.
     */
    public int getId() {
      return id;
    }

  }

}
