package me.ryleykimmel.brandywine.game.model.inter;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.CloseInterfaceMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a set of open Interfaces.
 */
public final class InterfaceSet {

  /**
   * A mapping of InterfaceTypes to currently open Interfaces.
   */
  private final Map<InterfaceType, Interface> interfaces = new HashMap<>();

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
   * Adds the specified Interface to this InterfaceSet.
   * <p>
   * This method should not be used for opening Interfaces, use {@link InterfaceSet#open(Interface)}
   * </p>
   *
   * @param inter The Interface that was added.
   */
  public void add(Interface inter) {
    interfaces.put(inter.getType(), inter);
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
   * - Tabs cannot be closed, only hidden from view or changed.
   * - Static overlays must be closed by notifying the InterfacesClosedEvent, use {@link InterfaceSet#closeAndNotify()}.
   * </p>
   *
   * @param notify {@code true} if the EventListeners should be notified of this event.
   */
  private void close(boolean notify) {
    if (interfaces.isEmpty()) {
      return;
    }

    player.write(new CloseInterfaceMessage());

    if (notify) {
      player.getWorld().notify(new InterfacesClosedEvent(player));
    }

    interfaces.remove(InterfaceType.DIALOGUE);
    interfaces.remove(InterfaceType.DYNAMIC_OVERLAY);
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
