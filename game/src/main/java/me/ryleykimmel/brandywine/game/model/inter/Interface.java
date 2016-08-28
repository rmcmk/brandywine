package me.ryleykimmel.brandywine.game.model.inter;

import me.ryleykimmel.brandywine.game.model.player.Player;

import java.util.Objects;

/**
 * Represents an in-game Interface.
 */
public abstract class Interface {

  /**
   * The id of this Interface.
   */
  private final int id;

  /**
   * The type of this Interface.
   */
  private final InterfaceType type;

  /**
   * Constructs a new Interface.
   *
   * @param id The id of this Interface.
   * @param type The type of this Interface.
   */
  public Interface(int id, InterfaceType type) {
    this.id = id;
    this.type = type;
  }

  /**
   * Gets the id of this Interface.
   *
   * @return The id of this Interface.
   */
  public final int getId() {
    return id;
  }

  /**
   * Gets the type of this Interface.
   *
   * @return The type of this Interface.
   */
  public final InterfaceType getType() {
    return type;
  }

  /**
   * Opens this Interface.
   *
   * @param player The Player who opened this Interface.
   * @param interfaceSet The InterfaceSet this Interface belongs to.
   */
  public abstract void open(Player player, InterfaceSet interfaceSet);

  @Override
  public final int hashCode() {
    return Objects.hash(id, type);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj instanceof Interface) {
      Interface other = (Interface) obj;
      return other.getId() == id && other.getType() == type;
    }
    return false;
  }

}
