package me.ryleykimmel.brandywine.fs.archive;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import me.ryleykimmel.brandywine.common.Buffer;
import me.ryleykimmel.brandywine.common.util.CompressionUtil;

/**
 * An archive in the RuneScape cache. An archive is a set of files which can be completely
 * compressed, or each individual file can be compressed.
 */
public final class Archive {

  /**
   * The empty Archive.
   */
  private static final Archive EMPTY_ARCHIVE = new Archive(Collections.emptyMap());

  /**
   * The entries in this archive.
   */
  private final Map<Integer, ArchiveEntry> entries;

  /**
   * Creates a new archive.
   * 
   * @param entries The entries in this archive.
   */
  private Archive(Map<Integer, ArchiveEntry> entries) {
    this.entries = ImmutableMap.copyOf(entries);
  }

  /**
   * Gets the {@link ArchiveEntry} with the specified name, wrapped in an Optional.
   * 
   * @param name The name of the entry.
   * @return The entry, wrapped in an Optional.
   */
  private Optional<ArchiveEntry> getOptionalEntry(String name) {
    int hash = ArchiveUtil.hash(name);
    return Optional.ofNullable(entries.get(hash));
  }

  /**
   * Gets the {@link ArchiveEntry} with the specified name.
   * 
   * @param name The name of the entry.
   * @return The entry.
   */
  public ArchiveEntry getEntry(String name) {
    return getOptionalEntry(name).orElseThrow(
        () -> new IllegalArgumentException("ArchiveEntry not found for " + name + "."));
  }

  /**
   * Decodes the {@link Archive} in the specified {@link Buffer}.
   * 
   * @param buffer The buffer.
   * @return The archive.
   * @throws IOException If there is an error decompressing the data.
   */
  public static Archive decode(Buffer buffer) throws IOException {
    if (buffer.isEmpty()) {
      return Archive.EMPTY_ARCHIVE;
    }

    int extractedSize = buffer.getUnsignedTriByte();
    int size = buffer.getUnsignedTriByte();
    boolean extracted = size != extractedSize;

    if (extracted) {
      buffer = CompressionUtil.bunzip2(buffer);
    }

    int count = buffer.getUnsignedShort();
    int[] identifiers = new int[count];
    int[] extractedSizes = new int[count];
    int[] sizes = new int[count];

    for (int entry = 0; entry < count; entry++) {
      identifiers[entry] = buffer.getInt();
      extractedSizes[entry] = buffer.getUnsignedTriByte();
      sizes[entry] = buffer.getUnsignedTriByte();
    }

    Map<Integer, ArchiveEntry> entries = new HashMap<>(count);
    for (int entry = 0; entry < count; entry++) {
      Buffer data = Buffer.allocate(extracted ? extractedSizes[entry] : sizes[entry]);
      Buffer filled = extracted ? data : data.fill(buffer);

      entries.put(identifiers[entry], new ArchiveEntry(identifiers[entry], filled));
    }

    return new Archive(entries);
  }

  @Override
  public int hashCode() {
    return entries.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof Archive) {
      Archive other = (Archive) object;
      return entries.equals(other.entries);
    }

    return false;
  }

}
