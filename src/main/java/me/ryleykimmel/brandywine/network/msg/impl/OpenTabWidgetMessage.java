package me.ryleykimmel.brandywine.network.msg.impl;

import me.ryleykimmel.brandywine.network.msg.Message;

/**
 * A {@link Message} sent to the client to open a tab widget.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class OpenTabWidgetMessage implements Message {

  /**
   * The Widget id.
   */
  private final int widgetId;

  /**
   * The tab id.
   */
  private final int tabId;

  /**
   * Constructs a new {@link OpenTabWidgetMessage}.
   *
   * @param widgetId The Widget id.
   * @param tabId The tab id.
   */
  public OpenTabWidgetMessage(int widgetId, int tabId) {
    this.widgetId = widgetId;
    this.tabId = tabId;
  }

  /**
   * Gets the Widget id.
   *
   * @return The Widget id.
   */
  public int getWidgetId() {
    return widgetId;
  }

  /**
   * Gets the tab id.
   *
   * @return The tab id.
   */
  public int getTabId() {
    return tabId;
  }

}
