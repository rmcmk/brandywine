package me.ryleykimmel.brandywine.game.update.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.ryleykimmel.brandywine.game.collect.MobRepository;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.player.Player;
import me.ryleykimmel.brandywine.game.update.PlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.blocks.ChatPlayerBlock;
import me.ryleykimmel.brandywine.game.update.descriptor.AddPlayerDescriptor;
import me.ryleykimmel.brandywine.game.update.descriptor.RemovePlayerDescriptor;
import me.ryleykimmel.brandywine.network.msg.impl.PlayerUpdateMessage;

/**
 * An UpdateTask which updates your Player and surrounding Players.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class PlayerUpdateTask implements UpdateTask {

	/**
	 * The Player we are updating.
	 */
	private final Player player;

	/**
	 * The surrounding Players we are updating.
	 */
	private final MobRepository<Player> players;

	/**
	 * Constructs a new {@link PlayerUpdateTask} with the specified Player and surrounding players.
	 * 
	 * @param player The Player we are updating.
	 * @param players The surronding Players we are updating.
	 */
	public PlayerUpdateTask(Player player, MobRepository<Player> players) {
		this.player = player;
		this.players = players;
	}

	@Override
	public void run() {
		Position lastKnownRegion = player.getLastKnownRegion();
		Position position = player.getPosition();
		int[] tickets = player.getAppearanceTickets();
		int viewingDistance = player.getViewingDistance();

		PlayerDescriptor descriptor = PlayerDescriptor.create(player, tickets);

		// Remove chat player block from our self descriptor -- we don't want to update chat for ourselves twice!
		descriptor.removeBlock(ChatPlayerBlock.class);

		List<PlayerDescriptor> descriptors = new ArrayList<>();
		Set<Player> localPlayers = player.getLocalPlayers();
		int localPlayerCount = localPlayers.size();

		for (Iterator<Player> it = localPlayers.iterator(); it.hasNext();) {
			Player other = it.next();
			if (removeable(position, viewingDistance, other)) {
				it.remove();
				descriptors.add(new RemovePlayerDescriptor(other, tickets));
			} else {
				descriptors.add(PlayerDescriptor.create(other, tickets));
			}
		}

		for (Player other : players) {
			if (localPlayers.size() >= 255) {
				player.flagExcessivePlayers();
				break;
			}

			if (!player.equals(other) && position.isWithinDistance(other.getPosition(), viewingDistance) && !localPlayers.contains(other)) {
				localPlayers.add(other);
				descriptors.add(new AddPlayerDescriptor(other, tickets));
			}
		}

		player.write(new PlayerUpdateMessage(lastKnownRegion, position, localPlayerCount, descriptor, descriptors));
	}

	/**
	 * Returns whether or not the specified {@link Player} should be removed.
	 *
	 * @param position The {@link Position} of the Player being updated.
	 * @param other The Player being tested.
	 * @return {@code true} iff the specified Player should be removed.
	 */
	private boolean removeable(Position position, int distance, Player other) {
		if (other.isTeleporting() || !other.isActive()) {
			return true;
		}

		Position otherPosition = other.getPosition();
		return otherPosition.getLongestDelta(position) > distance || !otherPosition.isWithinDistance(position, distance);
	}

}