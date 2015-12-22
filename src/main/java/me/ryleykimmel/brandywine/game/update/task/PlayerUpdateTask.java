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

  private static final int MAXIMUM_LOCAL_PLAYERS = 255;
  private static final int MAXIMUM_ADDITIONS_PER_PULSE = 20;

  /**
   * The Player we are updating.
   */
  private final Player player;

  /**
   * The surrounding Players we are updating.
   */
  private final MobRepository<Player> players;

  private final Updater updater;

  /**
   * Constructs a new {@link PlayerUpdateTask} with the specified Player and surrounding players.
   * 
   * @param updater @param player The Player we are updating. @param players The surronding Players
   * we are updating.
   */
  public PlayerUpdateTask(Updater updater, Player player, MobRepository<Player> players) {
    this.player = player;
    this.players = players;
    this.updater = updater;
  }

  private PlayerDescriptor createStateDescriptor(Player player) {
    if (player.isTeleporting()) {
      return new TeleportPlayerDescriptor(player, updater);
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

    PlayerDescriptor descriptor = createStateDescriptor(player);

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
        descriptors.add(checkCachedAppearance(tickets, other, createStateDescriptor(other)));
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
        descriptors.add(new AddPlayerDescriptor(other, updater));
        added++;
      }
    }

    player.write(new PlayerUpdateMessage(lastKnownRegion, position, localPlayerCount, descriptor,
        descriptors));
  }

  private PlayerDescriptor checkCachedAppearance(int[] tickets, Player player,
      PlayerDescriptor descriptor) {
    if (player.isActive()) {
      int index = player.getIndex() - 1;
      int ticket = player.getAppearanceTicket();
      if (tickets[index] != ticket) {
        tickets[index] = ticket;
        descriptor.addBlock(AppearancePlayerBlock.create(player));
      }
    }
    return descriptor;
  }

  /**
   * Returns whether or not the specified {@link Player} should be removed.
   *
   * @param position The {@link Position} of the Player being updated. @param other The Player being
   * tested. @return {@code true} iff the specified Player should be removed.
   */
  private boolean removeable(Position position, int distance, Player other) {
    if (other.isTeleporting() || !other.isActive()) {
      return true;
    }

    Position otherPosition = other.getPosition();
    return otherPosition.getLongestDelta(position) > distance
        || !otherPosition.isWithinDistance(position, distance);
  }

  @Override
  public void exceptionCaught(Throwable cause) {
    cause.printStackTrace();
    player.disconnect();
  }

}
