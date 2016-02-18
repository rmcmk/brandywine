package me.ryleykimmel.brandywine.game.model.player;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.MoreObjects;

import me.ryleykimmel.brandywine.ServerContext;
import me.ryleykimmel.brandywine.game.model.EntityType;
import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.model.skill.LevelUpSkillListener;
import me.ryleykimmel.brandywine.game.model.skill.SynchronizationSkillListener;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.network.game.GameSession;
import me.ryleykimmel.brandywine.network.msg.Message;
import me.ryleykimmel.brandywine.network.msg.impl.InitializePlayerMessage;
import me.ryleykimmel.brandywine.network.msg.impl.LoginResponseMessage;
import me.ryleykimmel.brandywine.network.msg.impl.RebuildRegionMessage;
import me.ryleykimmel.brandywine.network.msg.impl.ServerChatMessage;

/**
 * Represents a Player character.
 */
public final class Player extends Mob {

  /**
   * The current amount of appearance tickets.
   */
  private static final AtomicInteger appearanceTicketCounter = new AtomicInteger(0);

  /**
   * This appearance tickets for this Player.
   */
  private final int[] appearanceTickets = new int[World.MAXIMUM_PLAYERS];

  /**
   * The Appearance of this Player.
   */
  private final Appearance appearance = new Appearance();

  /**
   * The privileges for this Player.
   */
  private final PlayerPrivileges privileges = new PlayerPrivileges();

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
   * Constructs a new {@link Player} with the specified GameSession and PlayerCredentials.
   * 
   * @param session The GameSession this Player is attached to.
   * @param credentials The credentials of this Player.
   * @param world The World this Player is in.
   * @param context The context of the Server.
   */
  public Player(GameSession session, PlayerCredentials credentials, World world,
      ServerContext context) {
    super(world, EntityType.PLAYER, context);
    this.session = session;
    this.credentials = credentials;

    appearance.init();
    skills.init();

    flagUpdate(AppearancePlayerBlock.create(this));
  }

  @Override
  public void write(Message message) {
    session.voidWrite(message);
  }

  /**
   * Logs this Player into the World.
   */
  public void login() {
    write(
        new LoginResponseMessage(LoginResponseMessage.STATUS_OK, privileges.getPrimaryId(), false));
    write(new InitializePlayerMessage(isMember(), getIndex()));

    session.seedCiphers(credentials.getSessionKeys());
    session.attr().set(this);

    setLastKnownRegion(position);
    teleport(position);

    write(new RebuildRegionMessage(position));
    write(new ServerChatMessage("Welcome to %s.", session.getContext().getName()));

    if (isMember()) {
      write(new ServerChatMessage("You are a member!"));
    }

    skills.addListener(new SynchronizationSkillListener(this));
    skills.addListener(new LevelUpSkillListener(this));

    skills.refresh();
  }

  /**
   * Disconnects this Player from the World.
   */
  public void disconnect() {
    session.close();
  }

  /**
   * Updates the Appearance for this Player.
   */
  public void updateAppearance() {
    appearanceTicket = nextAppearanceTicket();
    flagUpdate(AppearancePlayerBlock.create(this));
  }

  /**
   * Generates the next appearance ticket.
   * 
   * @return The next available appearance ticket.
   */
  private static int nextAppearanceTicket() {
    if (appearanceTicketCounter.incrementAndGet() == 0) {
      appearanceTicketCounter.set(1);
    }
    return appearanceTicketCounter.get();
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
   * Gets this Players privileges.
   * 
   * @return This Players privileges.
   */
  public PlayerPrivileges getPrivileges() {
    return privileges;
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
   * @return {@code true} if the map region has changed and an update is required, otherwise {@code
   * false}.
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
   * Tests whether or not this Player is a member.
   * 
   * @return {@code true} iff this Player is a member, otherwise {@code false}.
   */
  private boolean isMember() {
    return false;
  }

  @Override
  public void reset() {
    super.reset();
    mapRegionChanged = false;
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("username", getUsername()).toString();
  }

}
