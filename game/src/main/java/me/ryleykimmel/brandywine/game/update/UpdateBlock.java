package me.ryleykimmel.brandywine.game.update;

/**
 * Represents an update block.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public abstract class UpdateBlock {

  /**
   * The mask of this UpdateBlock.
   */
  private final int mask;

  /**
   * Constructs a new {@link UpdateBlock} with the specified mask.
   * 
   * @param mask The Mask of this UpdateBlock.
   */
  public UpdateBlock(int mask) {
    this.mask = mask;
  }

  /**
   * Gets the Mask of this UpdateBlock.
   * 
   * @return The Mask of this UpdateBlock.
   */
  public final int getMask() {
    return mask;
  }

}
