package me.ryleykimmel.brandywine.game.msg;

import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.network.msg.Message;

import java.util.List;

/**
 * A {@link Message} which updates Players.
 */
public final class PlayerUpdateMessage extends Message {

  /**
   * The last known region.
   */
  private final Position lastKnownRegion;

  /**
   * The current Position.
   */
  private final Position position;

  /**
   * The local player count.
   */
  private final int localPlayerCount;

  /**
   * This Players descriptor.
   */
  private final PlayerDescriptor descriptor;

  /**
   * The surrounding Players descriptors.
   */
  private final List<PlayerDescriptor> descriptors;

  /**
   * Constructs a new {@link PlayerUpdateMessage}.
   * 
   * @param lastKnownRegion The last known region.
   * @param position The current position.
   * @param localPlayerCount The local player count.
   * @param descriptor This Players descriptor.
   * @param descriptors The surrounding Players descriptors.
   */
  public PlayerUpdateMessage(Position lastKnownRegion, Position position, int localPlayerCount,
      PlayerDescriptor descriptor, List<PlayerDescriptor> descriptors) {
    this.lastKnownRegion = lastKnownRegion;
    this.position = position;
    this.localPlayerCount = localPlayerCount;
    this.descriptor = descriptor;
    this.descriptors = descriptors;
  }

  /**
   * Gets the last known region.
   * 
   * @return The last know region.
   */
  public Position getLastKnownRegion() {
    return lastKnownRegion;
  }

  /**
   * Gets the current position.
   * 
   * @return The currnet position.
   */
  public Position getPosition() {
    return position;
  }

  /**
   * Gets the local player count.
   * 
   * @return The local player count.
   */
  public int getLocalPlayerCount() {
    return localPlayerCount;
  }

  /**
   * Gets this Players descriptor.
   * 
   * @return This Players descriptor.
   */
  public PlayerDescriptor getDescriptor() {
    return descriptor;
  }

  /**
   * Gets the surrounding Players descriptors.
   * 
   * @return The surrounding Players descriptors.
   */
  public List<PlayerDescriptor> getDescriptors() {
    return descriptors;
  }

}
