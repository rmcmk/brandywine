package me.ryleykimmel.brandywine.game.update.task;

import me.ryleykimmel.brandywine.game.area.Region;
import me.ryleykimmel.brandywine.game.area.RegionCoordinates;
import me.ryleykimmel.brandywine.game.area.RegionRepository;
import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.EntityType;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.msg.PlayerUpdateMessage;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.game.update.descriptor.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * An UpdateTask which updates your Player and surrounding Players.
 */
public final class PlayerUpdateTask implements UpdateTask {

  /**
   * The maximum amount of players within the viewport.
   */
  private static final int MAXIMUM_LOCAL_PLAYERS = 255;

  /**
   * The maximum amount of added players per pulse.
   */
  private static final int MAXIMUM_ADDITIONS_PER_PULSE = 20;

  /**
   * The Player we are updating.
   */
  private final Player player;

  /**
   * Constructs a new {@link PlayerUpdateTask} with the specified Player and surrounding players.
   *
   * @param player The Player we are updating.
   */
  public PlayerUpdateTask(Player player) {
    this.player = player;
  }

  /**
   * Creates a PlayerDescriptor based upon the specified Player's current state (walking, running,
   * teleporting, etc).
   *
   * @param player The Player to create the descriptor for.
   * @param position the Position.
   * @param lastKnownRegion The last known region.
   * @return The new PlayerDescriptor.
   * @throws IllegalStateException If the PlayerDescriptor was unable to be created.
   */
  private PlayerDescriptor createStateDescriptor(Player player, Position position,
                                                  Position lastKnownRegion) {
    if (player.isTeleporting()) {
      return new TeleportPlayerDescriptor(player, position, lastKnownRegion);
    }

    if (player.getFirstDirection() == Direction.NONE
          && player.getSecondDirection() == Direction.NONE) {
      return new IdlePlayerDescriptor(player);
    }

    if (player.getFirstDirection() != Direction.NONE
          && player.getSecondDirection() == Direction.NONE) {
      return new WalkPlayerDescriptor(player);
    }

    if (player.getFirstDirection() != Direction.NONE
          && player.getSecondDirection() != Direction.NONE) {
      return new RunPlayerDescriptor(player);
    }

    throw new IllegalStateException("Unable to create state descriptor for player: " + player);
  }

  @Override
  public void run() {
    Position lastKnownRegion = player.getLastKnownRegion();
    Position position = player.getPosition();
    int viewingDistance = player.getViewingDistance();
    int[] tickets = player.getAppearanceTickets();

    PlayerDescriptor descriptor = createStateDescriptor(player, position, lastKnownRegion);

    // Remove chat player block from our self descriptor -- we don't want to update chat for
    // ourselves twice!
    descriptor.removeBlock(ChatPlayerBlock.class);

    List<PlayerDescriptor> descriptors = new ArrayList<>();
    Set<Player> localPlayers = player.getLocalPlayers();
    int localPlayerCount = localPlayers.size();

    for (Iterator<Player> it = localPlayers.iterator(); it.hasNext(); ) {
      Player other = it.next();
      if (removeable(position, viewingDistance, other)) {
        it.remove();
        descriptors.add(new RemovePlayerDescriptor(other));
      } else {
        PlayerDescriptor otherDescriptor = createStateDescriptor(other, position, lastKnownRegion);
        if (!hasCachedAppearance(tickets, other)) {
          otherDescriptor.addBlock(AppearancePlayerBlock.create(other));
        }
        descriptors.add(otherDescriptor);
      }
    }

    RegionRepository repository = player.getWorld().getRegionRepository();
    Region current = repository.fromPosition(position);

    Set<RegionCoordinates> regions = current.getSurrounding();
    regions.add(current.getCoordinates());

    Stream<Player> players = regions.stream().map(repository::get)
                               .flatMap(region -> region.getEntities(EntityType.PLAYER));

    Iterator<Player> iterator = players.iterator();
    int added = 0;

    while (iterator.hasNext()) {
      if (localPlayers.size() >= MAXIMUM_LOCAL_PLAYERS) {
        player.flagExcessivePlayers();
        break;
      } else if (added >= MAXIMUM_ADDITIONS_PER_PULSE) {
        break;
      }

      Player other = iterator.next();

      if (!player.equals(other) && position.isWithinDistance(other.getPosition(), viewingDistance)
            && !localPlayers.contains(other)) {
        localPlayers.add(other);
        descriptors.add(new AddPlayerDescriptor(other, position));
        added++;
      }
    }

    player.write(new PlayerUpdateMessage(lastKnownRegion, position, localPlayerCount, descriptor,
                                          descriptors));
  }

  /**
   * Tests whether or not the specified Player has a cached appearance within the specified
   * appearance ticket array.
   *
   * @param tickets The appearance tickets.
   * @param player The Player.
   * @return {@code true} if the specified Player has a cached appearance.
   */
  private boolean hasCachedAppearance(int[] tickets, Player player) {
    int index = player.getIndex() - 1;
    int ticket = player.getAppearanceTicket();

    if (tickets[index] != ticket) {
      tickets[index] = ticket;
      return false;
    }

    return true;
  }

  /**
   * Returns whether or not the specified {@link Player} should be removed.
   *
   * @param position The {@link Position} of the Player being updated.
   * @param distance The viewing distance.
   * @param other The Player being tested.
   * @return {@code true} iff the specified Player should be removed.
   */
  private boolean removeable(Position position, int distance, Player other) {
    if (other.isTeleporting() || !other.isActive()) {
      return true;
    }

    Position otherPosition = other.getPosition();
    return otherPosition.getLongestDelta(position) > distance || !otherPosition
                                                                    .isWithinDistance(position,
                                                                      distance);
  }

}
