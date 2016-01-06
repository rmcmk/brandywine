package me.ryleykimmel.brandywine.game.collect;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.stream.IntStream;

import me.ryleykimmel.brandywine.common.Suppliers;
import me.ryleykimmel.brandywine.game.model.Mob;

/**
 * A {@link MobRepository} is a repository of {@link Mob}s that are currently active in the game
 * World.
 *
 * @author Graham
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 *
 * @param <T> The type of Mob.
 */
public final class MobRepository<T extends Mob> implements Iterable<T> {

  /**
   * An Iterator for some MobRepository.
   * 
   * @author Ryley Kimmel <ryley.kimmel@live.com>
   *
   * @param <T> The Type of Mob.
   */
  private static final class MobRepositoryIterator<T extends Mob> implements Iterator<T> {

    /**
     * The current index.
     */
    private int current;

    /**
     * The last index found.
     */
    private int last = -1;

    /**
     * The MobRepository we're iterating over.
     */
    private final MobRepository<T> repository;

    /**
     * Constructs a new {@link MobRepositoryIterator} with the specified MobRepository.
     * 
     * @param repository The MobRepository we're iterating over.
     */
    private MobRepositoryIterator(MobRepository<T> repository) {
      this.repository = repository;
    }

    @Override
    public boolean hasNext() {
      int index = current;

      // return true iff there is a non-null element within the repository
      while (index <= repository.size()) {
        T mob = repository.mobs[index++];
        if (mob != null) {
          return true;
        }
      }

      return false;
    }

    @Override
    public T next() {
      while (current <= repository.size()) {
        T mob = repository.mobs[current++];
        if (mob != null) {
          last = current;
          return mob;
        }
      }

      throw new NoSuchElementException("There are no more elements!");
    }

    @Override
    public void remove() {
      if (last == -1) {
        throw new IllegalStateException("remove() may only be called once per call to next()");
      }

      repository.remove(last);
      last = -1;
    }

  }

  /**
   * A {@link Queue} of available indices.
   */
  private final Queue<Integer> indices;

  /**
   * The array of Mobs in this repository.
   */
  private final T[] mobs;

  /**
   * The current size of this repository.
   */
  private int size = 0;

  /**
   * The capacity of this repository.
   */
  private final int capacity;

  /**
   * Constructs a new {@link MobRepository} with the specified capacity.
   *
   * @param capacity The maximum number of Mobs that can be present in the repository.
   */
  @SuppressWarnings("unchecked")
  public MobRepository(int capacity) {
    this.capacity = capacity;

    mobs = (T[]) Array.newInstance(Mob.class, capacity);
    indices =
        IntStream.range(0, capacity).boxed().collect(Suppliers.collection(new ArrayDeque<>()));
  }

  /**
   * Adds a Mob to the repository.
   *
   * @param mob The Mob to add.
   * @return {@code true} if the Mob was added, {@code false} if the size has reached the capacity
   * of this repository.
   */
  public boolean add(T mob) {
    if (size == capacity()) {
      return false;
    }

    Integer index = indices.poll();
    if (index == null) {
      return false;
    }

    if (mobs[index] != null) {
      return false;
    }

    mobs[index] = mob;
    mob.setIndex(index + 1);
    size++;

    return true;
  }

  /**
   * Gets the capacity of this repository.
   *
   * @return The maximum size of this repository.
   */
  public int capacity() {
    return capacity;
  }

  /**
   * Gets the Mob at the given index.
   *
   * @param index The index of the Mob.
   * @return The Mob instance.
   */
  public T get(int index) {
    int actual = index - 1;
    if (actual < 0 || actual >= capacity) {
      throw new ArrayIndexOutOfBoundsException(
          "index: " + index + ", actual: " + actual + " is out of bounds, capacity: " + capacity);
    }

    return mobs[actual];
  }

  @Override
  public Iterator<T> iterator() {
    return new MobRepositoryIterator<>(this);
  }

  /**
   * Removes a Mob from the repository.
   *
   * @param mob The Mob to remove.
   * @return {@code true} if the Mob was removed, {@code false} if not.
   */
  public boolean remove(T mob) {
    return mob != null && remove(mob.getIndex());
  }

  /**
   * Removes a Mob from the repository by the specified index.
   *
   * @param index The index of the Mob to remove.
   * @return {@code true} if the Mob at the specified index was removed otherwise {@code false}.
   */
  public boolean remove(int index) {
    Mob mob = get(index);

    if (mob == null) {
      return false;
    }

    if (mob.getIndex() != index) {
      return false;
    }

    int actual = index - 1;

    indices.offer(actual);
    mobs[actual] = null;
    mob.setIndex(-1);
    size--;

    return true;
  }

  /**
   * Gets the size of this repository.
   *
   * @return The number of Mobs in this repository.
   */
  public int size() {
    return size;
  }

}
