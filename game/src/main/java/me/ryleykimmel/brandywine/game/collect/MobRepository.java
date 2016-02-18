package me.ryleykimmel.brandywine.game.collect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.IntStream;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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
   * The Comparator used to sort available indices.
   */
  private static final Comparator<Integer> INDEX_COMPARATOR = Integer::compare;

  /**
   * A {@link Queue} of available indices.
   */
  private final Queue<Integer> indices = new PriorityQueue<>(INDEX_COMPARATOR);

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
    IntStream.rangeClosed(0, capacity).boxed().forEachOrdered(indices::offer);
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
   */
  public void remove(T mob) {
    Preconditions.checkNotNull(mob, "Mob may not be null");
    remove(mob.getIndex());
  }

  /**
   * Removes a Mob from the repository by the specified index.
   *
   * @param index The index of the Mob to remove.
   */
  private void remove(int index) {
    Mob mob = get(index);

    Preconditions.checkArgument(mob.getIndex() == index,
        "Mob index mismatch, unable to remove Mob. [index=" + mob.getIndex() + ", expected=" + index
            + "]");

    int actual = index - 1;

    indices.offer(actual);
    mobs[actual] = null;
    mob.setIndex(-1);
    size--;
  }

  /**
   * Gets the size of this repository.
   *
   * @return The number of Mobs in this repository.
   */
  public int size() {
    return size;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("mobs", Arrays.toString(mobs)).add("size", size)
        .add("capacity", capacity).toString();
  }

}
