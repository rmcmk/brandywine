package me.ryleykimmel.brandywine.fs.archive;

import me.ryleykimmel.brandywine.common.Buffer;

/**
 * A single entry in an {@link Archive}. This class is immutable.
 * 
 * @author Graham
 * @author Major
 */
public final class ArchiveEntry {

	/**
	 * The buffer of this entry.
	 */
	private final Buffer buffer;

	/**
	 * The identifier of this entry.
	 */
	private final int identifier;

	/**
	 * Creates the archive entry.
	 * 
	 * @param identifier The identifier.
	 * @param buffer The buffer containing this entry's data.
	 */
	public ArchiveEntry(int identifier, Buffer buffer) {
		this.identifier = identifier;
		this.buffer = buffer.asReadOnlyBuffer();
	}

	/**
	 * Creates the archive entry.
	 *
	 * @param name The name of the archive.
	 * @param buffer The buffer containing this entry's data.
	 */
	public ArchiveEntry(String name, Buffer buffer) {
		this(ArchiveUtils.hash(name), buffer);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ArchiveEntry) {
			ArchiveEntry other = (ArchiveEntry) object;
			return identifier == other.identifier && buffer.equals(other.buffer);
		}

		return false;
	}

	/**
	 * Gets a deep copy of the buffer of this entry.
	 * 
	 * @return The buffer of this entry.
	 */
	public Buffer getBuffer() {
		return buffer.copy();
	}

	/**
	 * Gets the identifier of this entry.
	 * 
	 * @return The identifier of this entry.
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the size of this entry (i.e. the capacity of the {@link Buffer} backing it), in bytes.
	 * 
	 * @return The size of this entry.
	 */
	public int getSize() {
		return buffer.limit();
	}

	@Override
	public int hashCode() {
		return 31 * buffer.hashCode() + identifier;
	}

}