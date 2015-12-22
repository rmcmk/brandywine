package me.ryleykimmel.brandywine.fs.archive;

/**
 * Contains archive-related utility methods.
 * 
 * @author Major
 */
public final class ArchiveUtil {

  /**
   * Hashes the specified string into an integer used to identify an {@link ArchiveEntry}.
   * 
   * @param name The name of the entry. @return The hash.
   */
  public static int hash(String name) {
    return name.toUpperCase().chars().reduce(0, (hash, character) -> hash * 61 + character - 32);
  }

  /**
   * Sole private constructor to discourage instantiation of this class.
   */
  private ArchiveUtil() {

  }

}
