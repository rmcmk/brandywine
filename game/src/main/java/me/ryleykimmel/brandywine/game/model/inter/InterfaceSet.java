package me.ryleykimmel.brandywine.game.model.inter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.ryleykimmel.brandywine.game.message.CloseInterfaceMessage;
import me.ryleykimmel.brandywine.game.model.player.Player;

/**
 * Maintains a set of open Interfaces.
 */
public final class InterfaceSet {

  /**
   * A mapping of Interface ids to currently open Interfaces.
   */
  private final Map<Integer, Interface> interfaces = new HashMap<>();

  /**
   * The Player who maintains this InterfaceSet.
   */
  private final Player player;

  /**
   * Constructs a new InterfaceSet.
   *
   * @param player The Player who maintains this InterfaceSet.
   */
  public InterfaceSet(Player player) {
    this.player = player;
  }

  /**
   * Opens the specified Interface.
   *
   * @param inter The Interface to open.
   */
  public void open(Interface inter) {
    close();
    add(inter);
    inter.open(player, this);
  }

  /**
   * Tests whether or not this InterfaceSet has the specified InterfaceType open.
   *
   * @param type The InterfaceType to test.
   * @return {@code true} iff the specified InterfaceType is open.
   */
  public boolean isOpen(InterfaceType type) {
    for (Interface inter : interfaces.values()) {
      if (inter.getType() == type) {
        return true;
      }
    }

    return false;
  }

  /**
   * Tests whether or not this InterfaceSet has the specified Interface open.
   *
   * @param id The Interface id.
   * @return {@code true} iff the specified Interface is open.
   */
  public boolean isOpen(int id) {
    return interfaces.containsKey(id);
  }

  /**
   * Adds the specified Interface to this InterfaceSet.
   * <p>
   * This method should not be used for opening Interfaces, use {@link InterfaceSet#open(Interface)}
   * </p>
   *
   * @param inter The Interface that was added.
   */
  public void add(Interface inter) {
    interfaces.put(inter.getId(), inter);
  }

  /**
   * Closes all Interfaces in this InterfaceSet and notifies the InterfacesClosedEvent.
   */
  public void closeAndNotify() {
    close(true);
  }

  /**
   * Closes all Interfaces in this InterfaceSet but does _not_ notify any InterfacesClosedEvent.
   */
  public void close() {
    close(false);
  }

  /**
   * Closes all Interfaces in this InterfaceSet.
   * <p>
   * - Tabs cannot be closed, only hidden from view or changed. - Static overlays must be closed by notifying the InterfacesClosedEvent, use {@link InterfaceSet#closeAndNotify()}.
   * </p>
   *
   * @param notify {@code true} if the EventListeners should be notified of this event.
   */
  private void close(boolean notify) {
    if (interfaces.isEmpty()) {
      return;
    }

    remove(InterfaceType.DIALOGUE);
    remove(InterfaceType.DYNAMIC_OVERLAY);

    player.write(new CloseInterfaceMessage());

    if (notify) {
      player.getWorld().notify(new InterfacesClosedEvent(player));
    }
  }

  /**
   * Removes all of the specified InterfaceTypes.
   *
   * @param type The InterfaceType to remove.
   */
  private void remove(InterfaceType type) {
    Set<Interface> remove = interfaces.values().stream().filter(inter -> inter.getType() == type)
        .collect(Collectors.toSet());
    remove.forEach(inter -> interfaces.remove(inter.getId()));
  }

  /**
   * Gets the size of this InterfaceSet.
   *
   * @return The size of this InterfaceSet.
   */
  public int size() {
    return interfaces.size();
  }

}
