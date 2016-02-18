package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.moandjiezana.toml.Toml;

import me.ryleykimmel.brandywine.network.game.frame.FrameType;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * Parses Frame definitions.
 */
public final class FrameParser extends TomlParser {

  /**
   * The maximum amount of Frames.
   */
  private static final int MAXIMUM_FRAMES = 256;

  /**
   * The length of Frames which contain an empty payload.
   */
  private static final int EMPTY_LENGTH = 0;

  /**
   * The length of a variable byte Frame.
   */
  private static final int VARIABLE_BYTE_LENGTH = -1;

  /**
   * The length of a variable short Frame.
   */
  private static final int VARIABLE_SHORT_LENGTH = -2;

  /**
   * The default length for Frames that are unmapped.
   */
  private static final int UNMAPPED_LENGTH = -3;

  /**
   * A Map of Frame opcodes to lengths.
   */
  private final Map<Integer, Integer> cipheredFrames = new HashMap<>(MAXIMUM_FRAMES);

  /**
   * A Map of Frame opcodes to lengths.
   */
  private final Map<Integer, Integer> uncipheredFrames = new HashMap<>(MAXIMUM_FRAMES);

  /**
   * Constructs a new {@link FrameParser} with the specific path.
   *
   * @param path The path to the source.
   */
  public FrameParser(String path) {
    super(path);
  }

  @Override
  public void parse(Reader source, Toml data) {
    populate(data, uncipheredFrames, "unciphered");
    populate(data, cipheredFrames, "ciphered");
  }

  /**
   * Populates the specified Frame map for the specified key.
   * 
   * @param data The data to parse from.
   * @param map The Map to populate.
   * @param key The key of the map.
   */
  private void populate(Toml data, Map<Integer, Integer> map, String key) {
    for (Toml toml : data.getTables(key)) {
      int opcode = getInteger(toml, "opcode");
      int length = getInteger(toml, "length");
      map.put(opcode, length);
    }
  }

  /**
   * Gets the length of the specified Frame for its opcode.
   * 
   * @param ciphered Whether or not the Frames are ciphered.
   * @param opcode The opcode of the Frame.
   * @return The length of the Frame or <tt>-3</tt> if it does not exist.
   */
  public int getLength(boolean ciphered, int opcode) {
    Map<Integer, Integer> map = ciphered ? cipheredFrames : uncipheredFrames;
    return map.getOrDefault(opcode, UNMAPPED_LENGTH);
  }

  /**
   * Gets the type of a Frame for the specified opcode.
   *
   * @param ciphered Whether or not the Frames are ciphered.
   * @param opcode The opcode of the Frame.
   * @return The type of the Frame.
   */
  public FrameType getType(boolean ciphered, int opcode) {
    int length = getLength(ciphered, opcode);

    switch (length) {
      case EMPTY_LENGTH:
        return FrameType.EMPTY;

      case VARIABLE_BYTE_LENGTH:
        return FrameType.VARIABLE_BYTE;

      case VARIABLE_SHORT_LENGTH:
        return FrameType.VARIABLE_SHORT;

      case UNMAPPED_LENGTH:
        return FrameType.INVALID;
    }

    return FrameType.FIXED;
  }

}
