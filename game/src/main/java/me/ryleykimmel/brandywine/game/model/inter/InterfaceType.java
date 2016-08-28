package me.ryleykimmel.brandywine.game.model.inter;

/**
 * Represents the type of an Interface.
 */
public enum InterfaceType {

  /**
   * An Interface that occupies the game screen and will close when a Player performs actions.
   */
  DYNAMIC_OVERLAY,

  /**
   * An Interface that occupies the game screen and will _not_ close when a Player performs actions.
   */
  STATIC_OVERLAY,

  /**
   * An Interface that occupies the chat box.
   */
  DIALOGUE,

  /**
   * An Interface that occupies the sidebar or tab area.
   */
  SIDEBAR

}
