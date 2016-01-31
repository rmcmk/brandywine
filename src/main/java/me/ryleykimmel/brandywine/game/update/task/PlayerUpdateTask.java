package me.ryleykimmel.brandywine.game.update.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.Direction;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.Updater;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.game.update.descriptor.AddPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.IdlePlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.RemovePlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.RunPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.TeleportPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.WalkPlayerDescriptor;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * An UpdateTask which updates your Player and surrounding Players.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
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
   * The surrounding Players we are updating.
   */
  private final MobRepository<Player> players;

  /**
   * The Updater.
   */
  private final Updater updater;

  /**
   * Constructs a new {@link PlayerUpdateTask} with the specified Player and surrounding players.
   * 
   * @param updater
   * @param player The Player we are updating.
   * @param players The surronding Players we are updating.
   */
  public PlayerUpdateTask(Updater updater, Player player, MobRepository<Player> players) {
    this.player = player;
    this.players = players;
    this.updater = updater;
  }

  /**
   * Creates a PlayerDescriptor based upon the specified Player's current state (walking, running,
   * teleporting, etc).
   * 
   * @param player The Player to create the descriptor for.
   * @return The new PlayerDescriptor.
   * @throws IllegalStateException If the PlayerDescriptor was unable to be created.
   */
  private PlayerDescriptor createStateDescriptor(Player player, Position position,
      Position lastKnownRegion) {
    if (player.isTeleporting()) {
      return new TeleportPlayerDescriptor(player, position, lastKnownRegion, updater);
    }

    if (player.getFirstDirection() == Direction.NONE
        && player.getSecondDirection() == Direction.NONE) {
      return new IdlePlayerDescriptor(player, updater);
    }

    if (player.getFirstDirection() != Direction.NONE
        && player.getSecondDirection() == Direction.NONE) {
      return new WalkPlayerDescriptor(player, updater);
    }

    if (player.getFirstDirection() != Direction.NONE
        && player.getSecondDirection() != Direction.NONE) {
      return new RunPlayerDescriptor(player, updater);
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

    for (Iterator<Player> it = localPlayers.iterator(); it.hasNext();) {
      Player other = it.next();
      if (removeable(position, viewingDistance, other)) {
        it.remove();
        descriptors.add(new RemovePlayerDescriptor(other, updater));
      } else {
        PlayerDescriptor otherDescriptor = createStateDescriptor(other, position, lastKnownRegion);
        if (!hasCachedAppearance(tickets, other)) {
          otherDescriptor.addBlock(AppearancePlayerBlock.create(other));
        }
        descriptors.add(otherDescriptor);
      }
    }

    int added = 0;
    for (Player other : players) {
      if (localPlayers.size() >= MAXIMUM_LOCAL_PLAYERS) {
        player.flagExcessivePlayers();
        break;
      } else if (added >= MAXIMUM_ADDITIONS_PER_PULSE) {
        break;
      }

      if (!player.equals(other) && position.isWithinDistance(other.getPosition(), viewingDistance)
          && !localPlayers.contains(other)) {
        localPlayers.add(other);
        descriptors.add(new AddPlayerDescriptor(other, position, updater));
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
   * @return {@code true} if the specified Player has a cached appearance otherwise {@code false}.
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
    return otherPosition.getLongestDelta(position) > distance
        || !otherPosition.isWithinDistance(position, distance);
  }

}
