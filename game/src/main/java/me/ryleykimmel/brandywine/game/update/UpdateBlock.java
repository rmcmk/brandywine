package me.ryleykimmel.brandywine.game.update;

import me.ryleykimmel.brandywine.network.frame.FrameBuilder;

/**
 * Represents an update block.
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

  /**
   * Encodes this UpdateBlock.
   * 
   * @param builder The FrameBuilder used to encode this UpdateBlock.
   */
  public abstract void encode(FrameBuilder builder);

}
