package me.ryleykimmel.brandywine.game.model.player;

import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.skill.LevelUpSkillListener;
import me.ryleykimmel.brandywine.game.model.skill.SynchronizationSkillListener;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.impl.ChatMessage;
import me.ryleykimmel.brandywine.network.msg.impl.InitializePlayerMessage;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;
import me.ryleykimmel.brandywine.network.msg.impl.RebuildRegionMessage;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

/**
 * Represents a Player character.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Player extends Mob {

	/**
	 * The current amount of appearance tickets.
	 */
	private static int appearanceTicketCounter = 0;

	/**
	 * This appearance tickets for this Player.
	 */
	private final int[] appearanceTickets = new int[World.MAXIMUM_PLAYERS];

	/**
	 * The Appearance of this Player.
	 */
	private final Appearance appearance = new Appearance();

	/**
	 * The GameSession this Player is attached to.
	 */
	private final GameSession session;

	/**
	 * The credentials of this Player.
	 */
	private final PlayerCredentials credentials;

	/**
	 * The current maximum viewing distance of this player.
	 */
	private int viewingDistance = 1;

	/**
	 * A flag which indicates there are players that couldn't be added.
	 */
	private boolean excessivePlayers;

	/**
	 * The last known region this Player was in.
	 */
	private Position lastKnownRegion;

	/**
	 * A flag denoting whether or not the map region has changed and needs to be rebuilt.
	 */
	private boolean mapRegionChanged;

	/**
	 * This Players id within the Servers database.
	 */
	private int databaseId;

	/**
	 * This Players appearance ticket.
	 */
	private int appearanceTicket = nextAppearanceTicket();

	/**
	 * This Players cached ChatMessage; used to ensure only one ChatMessage is sent per pulse.
	 */
	private ChatMessage chatMessage;

	/**
	 * Constructs a new {@link Player} with the specified GameSession and PlayerCredentials.
	 * 
	 * @param session The GameSession this Player is attached to.
	 * @param credentials The credentials of this Player.
	 */
	public Player(GameSession session, PlayerCredentials credentials) {
		this.session = session;
		this.credentials = credentials;

		appearance.init();
		skills.init();
	}

	@Override
	public void write(Message message) {
		session.voidWrite(message);
	}

	/**
	 * Logs this Player into the World.
	 */
	public void login() {
		// TODO: Attribute system?
		write(new LoginResponseMessage(LoginResponseMessage.STATUS_OK, 0, false));
		write(new InitializePlayerMessage(false, getIndex()));

		session.seedCiphers(credentials.getSessionKeys());
		session.attr().set(this);

		Position position = DEFAULT_POSITION;
		setLastKnownRegion(position);
		teleport(position);

		skills.addListener(new SynchronizationSkillListener(this));
		skills.addListener(new LevelUpSkillListener(this));

		write(new RebuildRegionMessage(position));
		write(new ServerChatMessage("Welcome to %s.", session.getContext().getName()));

		skills.refresh();
	}

	/**
	 * Disconnects this Player from the World.
	 */
	public void disconnect() {
		session.close();
	}

	/**
	 * Generates the next appearance ticket.
	 * 
	 * @return The next available appearance ticket.
	 */
	private static int nextAppearanceTicket() {
		if (++appearanceTicketCounter == 0) {
			appearanceTicketCounter = 1;
		}
		return appearanceTicketCounter;
	}

	/**
	 * Gets the GameSession this Player is attached to.
	 * 
	 * @return The GameSession this Player is attached to.
	 */
	public GameSession getSession() {
		return session;
	}

	/**
	 * Gets the Appearance for this Player.
	 * 
	 * @return The Appearance for this Player.
	 */
	public Appearance getAppearance() {
		return appearance;
	}

	/**
	 * Gets all of this Players appearance tickets.
	 * 
	 * @return All of this Players appearance tickets.
	 */
	public int[] getAppearanceTickets() {
		return appearanceTickets;
	}

	/**
	 * Gets this Players appearance ticket.
	 * 
	 * @return This Players appearance ticket.
	 */
	public int getAppearanceTicket() {
		return appearanceTicket;
	}

	/**
	 * Gets the credentials of this Player.
	 * 
	 * @return This Players credentials.
	 */
	public PlayerCredentials getCredentials() {
		return credentials;
	}

	/**
	 * Gets whether or not the map region has changed.
	 * 
	 * @return {@code true} if the map region has changed and an update is required, otherwise {@code false}.
	 */
	public boolean hasMapRegionChanged() {
		return mapRegionChanged;
	}

	/**
	 * Gets this Players id within the Servers database.
	 * 
	 * @return This Players id within the Servers database.
	 */
	public int getDatabaseId() {
		return databaseId;
	}

	/**
	 * Gets the last known region for this Player.
	 * 
	 * @return The last known region for this Player.
	 */
	public Position getLastKnownRegion() {
		return lastKnownRegion;
	}

	/**
	 * Gets this Players username.
	 * 
	 * @return This Players username.
	 */
	public String getUsername() {
		return credentials.getUsername();
	}

	/**
	 * Gets this Players encoded username.
	 * 
	 * @return This Players encoded username.
	 */
	public long getEncodedUsername() {
		return credentials.getEncodedUsername();
	}

	/**
	 * Gets this Players cached ChatMessage, wrapped in an Optional.
	 * 
	 * @return This Players cached ChatMessage as an Optional.
	 */
	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	/**
	 * Gets this player's viewing distance.
	 *
	 * @return The viewing distance.
	 */
	public int getViewingDistance() {
		return viewingDistance;
	}

	/**
	 * Resets this player's viewing distance.
	 */
	public void resetViewingDistance() {
		viewingDistance = 1;
	}

	/**
	 * Sets the excessive players flag.
	 */
	public void flagExcessivePlayers() {
		excessivePlayers = true;
	}

	/**
	 * Checks if there are excessive players.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isExcessivePlayersSet() {
		return excessivePlayers;
	}

	/**
	 * Resets the excessive players flag.
	 */
	public void resetExcessivePlayers() {
		excessivePlayers = false;
	}

	/**
	 * Increments this player's viewing distance if it is less than the maximum viewing distance.
	 */
	public void incrementViewingDistance() {
		if (viewingDistance < Position.MAX_DISTANCE) {
			viewingDistance++;
		}
	}

	/**
	 * Decrements this player's viewing distance if it is greater than 1.
	 */
	public void decrementViewingDistance() {
		if (viewingDistance > 1) {
			viewingDistance--;
		}
	}

	/**
	 * Sets this Players cached ChatMessage.
	 * 
	 * @param chatMessage The ChatMessage to set.
	 */
	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	/**
	 * Sets the last known region for this Player.
	 * 
	 * @param lastKnownRegion The last known region to set.
	 */
	public void setLastKnownRegion(Position lastKnownRegion) {
		this.lastKnownRegion = lastKnownRegion;
		mapRegionChanged = true;
	}

	/**
	 * Sets this Players id within the Servers database.
	 * 
	 * @param databaseId The database id to set.
	 */
	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	/**
	 * Resets this Players cached ChatMessage.
	 */
	public void resetChatMessage() {
		chatMessage = null;
	}

	@Override
	public void reset() {
		super.reset();
		mapRegionChanged = false;
		resetChatMessage();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			Player other = (Player) obj;
			return other.getEncodedUsername() == getEncodedUsername();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getEncodedUsername());
	}

}