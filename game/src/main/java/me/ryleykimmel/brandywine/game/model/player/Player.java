package me.ryleykimmel.brandywine.game.model.player;

import com.google.common.base.MoreObjects;
import io.netty.util.AttributeKey;
import me.ryleykimmel.brandywine.game.model.EntityType;
import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.model.Position;
import me.ryleykimmel.brandywine.game.model.World;
import me.ryleykimmel.brandywine.game.msg.LoginResponseMessage;
import me.ryleykimmel.brandywine.game.update.blocks.AppearancePlayerBlock;
import me.ryleykimmel.brandywine.network.ResponseCode;
import me.ryleykimmel.brandywine.network.Session;
import me.ryleykimmel.brandywine.network.msg.Message;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a Player character.
 */
public final class Player extends Mob {

  /**
   * Represents this Player as an AttributeKey.
   */
  public static final AttributeKey<Player> ATTRIBUTE_KEY = AttributeKey.valueOf("player");

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
   * The Session this Player is attached to.
   */
  private Optional<Session> session = Optional.empty();

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
   * Constructs a new {@link Player}.
   *
   * @param credentials The credentials of this Player.
   * @param world The World this Player is in.
   */
  public Player(PlayerCredentials credentials, World world) {
    super(world, EntityType.PLAYER);
    this.credentials = credentials;

    appearance.init();
    skills.init();

    flagUpdate(AppearancePlayerBlock.create(this));
  }

  @Override
  public void write(Message message) {
    getSession().voidWrite(message);
  }

  /**
   * Logs this Player into the World.
   */
  public void login() {
    write(new LoginResponseMessage(ResponseCode.STATUS_OK, privileges.getPrimaryId(), false));
    getSession().attr(ATTRIBUTE_KEY).setIfAbsent(this);
    world.notify(new InitializePlayerEvent(this));
  }

  /**
   * Disconnects this Player from the World.
   */
  public void disconnect() {
    getSession().close();
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
   * Gets the Session this Player is attached to.
   *
   * @return The Session this Player is attached to.
   */
  public Session getSession() {
    return session.orElseThrow(() -> new IllegalStateException("Session has not been configured!"));
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
   * Sets the Session for this Player.
   *
   * @param session The Session for this Player.
   */
  public void setSession(Session session) {
    this.session = Optional.of(session);
  }

  /**
   * Tests whether or not this Player is a member.
   *
   * @return {@code true} iff this Player is a member, otherwise {@code false}.
   */
  public boolean isMember() {
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
