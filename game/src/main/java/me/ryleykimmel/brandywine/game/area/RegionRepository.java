package me.ryleykimmel.brandywine.game.area;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ImmutableList;

import me.ryleykimmel.brandywine.game.model.Position;

/**
 * A repository of {@link Region}s, backed by a {@link HashMap} of {@link RegionCoordinates} that
 * correspond to their appropriate regions.
 */
public final class RegionRepository {

  /**
   * The map of RegionCoordinates that correspond to the appropriate Regions.
   */
  private final Map<RegionCoordinates, Region> regions = new ConcurrentHashMap<>();

  /**
   * Indicates whether the supplied value (i.e. the {@link Region}) has a mapping.
   *
   * @param region The Region.
   * @return {@code true} if this repository contains an entry with {@link RegionCoordinates} equal
   * to the specified Region, otherwise {@code false}.
   */
  public boolean contains(Region region) {
    return contains(region.getCoordinates());
  }

  /**
   * Indicates whether the supplied key (i.e. the {@link RegionCoordinates}) has a mapping.
   *
   * @param coordinates The coordinates.
   * @return {@code true} if the key is already mapped to a value (i.e. a {@link Region}), otherwise
   * {@code false}.
   */
  public boolean contains(RegionCoordinates coordinates) {
    return regions.containsKey(coordinates);
  }

  /**
   * Gets the {@link Region} that contains the specified {@link Position}. If the Region does not
   * exist in this repository then a new Region is created, submitted to the repository, and
   * returned.
   *
   * @param position The position.
   * @return The Region.
   */
  public Region fromPosition(Position position) {
    return get(RegionCoordinates.fromPosition(position));
  }

  /**
   * Gets a {@link Region} with the specified {@link RegionCoordinates}. If the Region does not
   * exist in this repository then a new Region is created, submitted to the repository, and
   * returned.
   *
   * @param coordinates The RegionCoordinates.
   * @return The Region. Will never be null.
   */
  public Region get(RegionCoordinates coordinates) {
    return regions.computeIfAbsent(coordinates, Region::new);
  }

  /**
   * Gets a shallow copy of the {@link List} of {@link Region}s. This will be an
   * {@link ImmutableList}.
   *
   * @return The List.
   */
  public List<Region> getRegions() {
    return ImmutableList.copyOf(regions.values());
  }

}
