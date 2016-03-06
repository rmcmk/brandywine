package me.ryleykimmel.brandywine.common.util;

import java.nio.file.Path;

import com.moandjiezana.toml.Toml;

/**
 * A static utility class containing {@link Toml} related extension or helper methods.
 */
public final class TomlUtil {

  /**
   * Reads the contents of the specified {@link Path} into a {@link Toml} object.
   * 
   * @param path The Path.
   * @return The popukated Toml instance.
   */
  public static Toml read(Path path) {
    return new Toml().read(path.toFile());
  }

}
