package me.ryleykimmel.brandywine.game.msg;

import me.ryleykimmel.brandywine.game.model.inter.TabInterface;
import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} sent to the client to open a TabInterface.
 */
public final class OpenTabInterfaceMessage extends Message {

  /**
   * The TabInterface to open.
   */
  private final TabInterface tabInterface;

  /**
   * Constructs a new OpenTabInterfaceMessage.
   *
   * @param tabInterface The TabInterface to open.
   */
  public OpenTabInterfaceMessage(TabInterface tabInterface) {
    this.tabInterface = tabInterface;
  }

  /**
   * Gets the TabInterface to open.
   *
   * @return The TabInterface to open.
   */
  public TabInterface getTabInterface() {
    return tabInterface;
  }

}
