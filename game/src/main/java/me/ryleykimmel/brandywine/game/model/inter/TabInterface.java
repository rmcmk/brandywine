package me.ryleykimmel.brandywine.game.model.inter;

import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.OpenTabInterfaceMessage;

/**
 * Represents an Interface within the tab area.
 */
public final class TabInterface extends Interface {

  /**
   * The Tab to open this Interface on.
   */
  private final Tab tab;

  /**
   * Constructs a new TabInterface.
   *
   * @param id The id of this Interface.
   * @param tab The Tab to open an Interface on.
   */
  public TabInterface(int id, Tab tab) {
    super(id, InterfaceType.SIDEBAR);
    this.tab = tab;
  }

  @Override
  public void open(Player player, InterfaceSet interfaceSet) {
    player.write(new OpenTabInterfaceMessage(this));
  }

  /**
   * Gets the Tab to open this Interface on.
   *
   * @return The Tab to open this Interface on.
   */
  public Tab getTab() {
    return tab;
  }

  /**
   * Represents a Tab in the sidebar.
   */
  public enum Tab {

    /**
     * The attack style Tab.
     */
    ATTACK_STYLE(0),

    /**
     * The skills and statistics Tab.
     */
    SKILL(1),

    /**
     * The quests Tab.
     */
    QUEST(2),

    /**
     * The inventory Tab.
     */
    INVENTORY(3),

    /**
     * The worn equipment Tab.
     */
    EQUIPMENT(4),

    /**
     * The Prayer book Tab.
     */
    PRAYER(5),

    /**
     * The spell book Tab.
     */
    SPELL_BOOK(6),

    /**
     * An empty tab.
     */
    EMPTY(7),

    /**
     * The friends list Tab.
     */
    FRIENDS(8),

    /**
     * The ignores list Tab.
     */
    IGNORES(9),

    /**
     * The logout Tab.
     */
    LOGOUT(10),

    /**
     * The settings Tab.
     */
    SETTINGS(11),

    /**
     * The controls and emotes Tab.
     */
    CONTROLS(12),

    /**
     * The music Tab.
     */
    MUSIC(13);

    /**
     * The id of this Tab.
     */
    private final int id;

    /**
     * Constructs a new Tab.
     *
     * @param id The id of this Tab.
     */
    private Tab(int id) {
      this.id = id;
    }

    /**
     * Gets the id of this Tab.
     *
     * @return The id of this Tab.
     */
    public int getId() {
      return id;
    }

  }

}
