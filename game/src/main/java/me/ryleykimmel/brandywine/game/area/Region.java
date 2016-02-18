package me.ryleykimmel.brandywine.game.area;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import me.ryleykimmel.brandywine.game.model.Entity;
import me.ryleykimmel.brandywine.game.model.EntityType;
import me.ryleykimmel.brandywine.game.model.Position;

/**
 * An 8x8 area of the map.
 */
public final class Region {

  /**
   * A {@link RegionListener} for {@link UpdateOperation}s.
   */
  private static final class UpdateRegionListener implements RegionListener {

    @Override
    public void execute(Region region, Entity entity, EntityUpdateType update) {}

  }

  /**
   * The width and length of a Region, in tiles.
   */
  public static final int SIZE = 8;

  /**
   * The radius of viewable regions.
   */
  public static final int VIEWABLE_REGION_RADIUS = 3;

  /**
   * The width of the viewport of every Player, in tiles.
   */
  public static final int VIEWPORT_WIDTH = SIZE * 13;

  /**
   * The default size of newly-created Lists, to reduce memory usage.
   */
  private static final int DEFAULT_LIST_SIZE = 2;

  /**
   * The RegionCoordinates of this Region.
   */
  private final RegionCoordinates coordinates;

  /**
   * The Map of Positions to Entities in that Position.
   */
  private final Map<Position, Set<Entity>> entities = new ConcurrentHashMap<>();

  /**
   * A List of RegionListeners registered to this Region.
   */
  private final List<RegionListener> listeners = new ArrayList<>();

  /**
   * Creates a new Region.
   *
   * @param x The x coordinate of the Region.
   * @param y The y coordinate of the Region.
   */
  public Region(int x, int y) {
    this(new RegionCoordinates(x, y));
  }

  /**
   * Creates a new Region with the specified {@link RegionCoordinates}.
   *
   * @param coordinates The RegionCoordinates.
   */
  public Region(RegionCoordinates coordinates) {
    this.coordinates = coordinates;
    listeners.add(new UpdateRegionListener());
  }

  /**
   * Adds a {@link Entity} to the Region. Note that this does not spawn the Entity, or do any other
   * action other than register it to this Region.
   *
   * @param entity The Entity.
   * @param notify Whether or not the {@link RegionListener}s for this Region should be notified.
   * @throws IllegalArgumentException If the Entity does not belong in this Region.
   */
  public void addEntity(Entity entity, boolean notify) {
    Position position = entity.getPosition();
    checkPosition(position);

    Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_LIST_SIZE));
    local.add(entity);

    if (notify) {
      notifyListeners(entity, EntityUpdateType.ADD);
    }
  }

  /**
   * Adds a {@link Entity} to the Region. Note that this does not spawn the Entity, or do any other
   * action other than register it to this Region.
   *
   * By default, this method notifies RegionListeners for this region of the addition.
   *
   * @param entity The Entity.
   * @throws IllegalArgumentException If the Entity does not belong in this Region.
   */
  public void addEntity(Entity entity) {
    addEntity(entity, true);
  }

  /**
   * Checks if this Region contains the specified Entity.
   *
   * This method operates in constant time.
   *
   * @param entity The Entity.
   * @return {@code true} if this Region contains the Entity, otherwise {@code false}.
   */
  public boolean contains(Entity entity) {
    Position position = entity.getPosition();
    Set<Entity> local = entities.get(position);

    return local != null && local.contains(entity);
  }

  /**
   * Returns whether or not the specified {@link Position} is inside this Region.
   *
   * @param position The Position.
   * @return {@code true} iff the specified Position is inside this Region.
   */
  public boolean contains(Position position) {
    return coordinates.equals(position.getRegionCoordinates());
  }

  /**
   * Gets this Region's {@link RegionCoordinates}.
   *
   * @return The RegionCoordinates.
   */
  public RegionCoordinates getCoordinates() {
    return coordinates;
  }

  /**
   * Gets an intermediate {@link Stream} from the {@link Set} of {@link Entity}s with the specified
   * {@link EntityType} (s). Type will be inferred from the call, so ensure that the Entity type and
   * the reference correspond, or this method will fail at runtime.
   *
   * @param types The {@link EntityType}s.
   * @return The Stream of Entity objects.
   */
  @SuppressWarnings("unchecked")
  public <T extends Entity> Stream<T> getEntities(EntityType... types) {
    Set<EntityType> set = ImmutableSet.copyOf(types);
    return (Stream<T>) entities.values().stream().flatMap(Collection::stream)
        .filter(entity -> set.contains(entity.getType()));
  }

  /**
   * Gets a shallow copy of the {@link Set} of {@link Entity} objects at the specified
   * {@link Position}. The returned type will be immutable.
   *
   * @param position The Position containing the entities.
   * @return The Set. Will be immutable.
   */
  public Set<Entity> getEntities(Position position) {
    Set<Entity> set = entities.get(position);
    return (set == null) ? ImmutableSet.of() : ImmutableSet.copyOf(set);
  }

  /**
   * Gets a shallow copy of the {@link Set} of {@link Entity}s with the specified {@link EntityType}
   * (s). The returned type will be immutable. Type will be inferred from the call, so ensure that
   * the Entity type and the reference correspond, or this method will fail at runtime.
   *
   * @param position The {@link Position} containing the entities.
   * @param types The {@link EntityType}s.
   * @return The Set of Entity objects.
   */
  public <T extends Entity> Set<T> getEntities(Position position, EntityType... types) {
    Set<Entity> local = entities.get(position);
    if (local == null) {
      return ImmutableSet.of();
    }

    Set<EntityType> set = ImmutableSet.copyOf(types);
    @SuppressWarnings("unchecked")
    Set<T> filtered = (Set<T>) local.stream().filter(entity -> set.contains(entity.getType()))
        .collect(Collectors.toSet());
    return ImmutableSet.copyOf(filtered);
  }

  /**
   * Gets the {@link Set} of {@link RegionCoordinates} of Regions that are viewable from the
   * specified {@link Position}.
   *
   * @return The Set of RegionCoordinates.
   */
  public Set<RegionCoordinates> getSurrounding() {
    int localX = coordinates.getX(), localY = coordinates.getY();
    int maxX = localX + VIEWABLE_REGION_RADIUS, maxY = localY + VIEWABLE_REGION_RADIUS;

    Set<RegionCoordinates> viewable = new HashSet<>();
    for (int x = localX - VIEWABLE_REGION_RADIUS; x < maxX; x++) {
      for (int y = localY - VIEWABLE_REGION_RADIUS; y < maxY; y++) {
        viewable.add(new RegionCoordinates(x, y));
      }
    }

    return viewable;
  }

  /**
   * Notifies the {@link RegionListener}s registered to this Region that an update has occurred.
   *
   * @param entity The {@link Entity} that was updated.
   * @param type The {@link EntityUpdateType} that occurred.
   */
  public void notifyListeners(Entity entity, EntityUpdateType type) {
    listeners.forEach(listener -> listener.execute(this, entity, type));
  }

  /**
   * Removes an {@link Entity} from this Region.
   *
   * @param entity The Entity.
   * @throws IllegalArgumentException If the Entity does not belong in this Region, or if it was
   * never added.
   */
  public void removeEntity(Entity entity) {
    Position position = entity.getPosition();
    checkPosition(position);

    Set<Entity> local = entities.get(position);

    if (local == null || !local.remove(entity)) {
      throw new IllegalArgumentException(
          "Entity (" + entity + ") belongs in (" + toString() + ") but does not exist.");
    }

    notifyListeners(entity, EntityUpdateType.REMOVE);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("coordinates", coordinates).toString();
  }

  /**
   * Checks that the specified {@link Position} is included in this Region.
   *
   * @param position The position.
   * @throws IllegalArgumentException If the specified position is not included in this Region.
   */
  private void checkPosition(Position position) {
    Preconditions.checkArgument(coordinates.equals(RegionCoordinates.fromPosition(position)),
        "Position is not included in this Region.");
  }

}
