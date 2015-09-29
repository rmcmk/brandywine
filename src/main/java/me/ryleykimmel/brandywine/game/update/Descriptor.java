package me.ryleykimmel.brandywine.game.update;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableSet;

import me.ryleykimmel.brandywine.game.model.Mob;

/**
 * Represents a Descriptor for some Mob which encodes UpdateBlocks and other Descriptors.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The Mob.
 */
public abstract class Descriptor<T extends Mob> {

	/**
	 * A Map of UpdateBlock types to UpdateBlocks.
	 */
	private final Map<Class<? extends UpdateBlock<T>>, UpdateBlock<T>> blocks = new HashMap<>();

	/**
	 * The Mob.
	 */
	protected final T mob;

	/**
	 * Constructs a new {@link Descriptor} with the specified Mob.
	 *
	 * @param mob The Mob.
	 */
	protected Descriptor(T mob) {
		this.mob = mob;
	}

	/**
	 * Adds the specified UpdateBlock to this Descriptor.
	 *
	 * @param block The UpdateBlock to add.
	 */
	@SuppressWarnings("unchecked")
	public void addBlock(UpdateBlock<T> block) {
		blocks.putIfAbsent((Class<? extends UpdateBlock<T>>) block.getClass(), block);
	}

	/**
	 * Removes the specified UpdateBlock.
	 * 
	 * @param type The UpdateBlocks type.
	 */
	public <V extends UpdateBlock<T>> void removeBlock(Class<V> type) {
		blocks.remove(type);
	}

	/**
	 * Tests whether or not an UpdateBlock update is required.
	 *
	 * @return {@code true} if and only if an UpdateBlock update is required.
	 */
	public boolean isBlockUpdatedRequired() {
		return !blocks.isEmpty();
	}

	/**
	 * Tests whether or not this Descriptor has the specified UpdateBlock.
	 *
	 * @param type The UpdateBlocks type.
	 * @return {@code true} if and only if this Descriptor contains the specified UpdateBlock.
	 */
	public <V extends UpdateBlock<T>> boolean hasBlock(Class<V> type) {
		return blocks.containsKey(type);
	}

	/**
	 * Gets the specified UpdateBlock, wrapped in an Optional.
	 *
	 * @param type The UpdateBlocks type.
	 * @return The UpdateBlock wrapped in an Optional.
	 */
	@SuppressWarnings("unchecked")
	public <V extends UpdateBlock<T>> Optional<V> getBlock(Class<V> type) {
		return Optional.ofNullable((V) blocks.get(type));
	}

	/**
	 * Gets a Collection of all the current UpdateBlocks.
	 *
	 * @return A Collection of this Descriptors UpdateBlocks.
	 */
	public ImmutableSet<UpdateBlock<T>> getBlocks() {
		return ImmutableSet.copyOf(blocks.values());
	}

}