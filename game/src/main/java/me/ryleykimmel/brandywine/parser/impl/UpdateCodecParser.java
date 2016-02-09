package me.ryleykimmel.brandywine.parser.impl;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.moandjiezana.toml.Toml;

import me.ryleykimmel.brandywine.game.model.Mob;
import me.ryleykimmel.brandywine.game.update.Descriptor;
import me.ryleykimmel.brandywine.game.update.DescriptorEncoder;
import me.ryleykimmel.brandywine.game.update.UpdateBlock;
import me.ryleykimmel.brandywine.game.update.UpdateBlockEncoder;
import me.ryleykimmel.brandywine.game.update.Updater;
import me.ryleykimmel.brandywine.network.game.frame.FrameBuilder;
import me.ryleykimmel.brandywine.parser.TomlParser;

/**
 * A TomlParser which parses codecs for an {@link Updater}.
 * 
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class UpdateCodecParser extends TomlParser {

  /**
   * A {@link Map} of {@link UpdateBlock} types to {@link UpdateBlockEncoder}s.
   */
  private final Map<Class<? extends UpdateBlock>, UpdateBlockEncoder<? extends UpdateBlock>> blocks =
      new HashMap<>();

  /**
   * A {@link Map} of {@link Descriptor}s to {@link DescriptorEncoder}s.
   */
  private final Map<Class<? extends Descriptor<? extends Mob>>, DescriptorEncoder<? extends Descriptor<? extends Mob>>> descriptors =
      new HashMap<>();

  /**
   * Constructs a new {@link UpdateCodecParser} with the specified path.
   * 
   * @param path The path to the source.
   */
  public UpdateCodecParser(String path) {
    super(path);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void parse(Reader source, Toml data) throws Exception {
    for (Toml toml : data.getTables("block")) {
      Class<? extends UpdateBlock> clazz =
          (Class<? extends UpdateBlock>) Class.forName(toml.getString("type"));

      UpdateBlockEncoder<? extends UpdateBlock> encoder =
          (UpdateBlockEncoder<? extends UpdateBlock>) Class.forName(toml.getString("encoder"))
              .newInstance();

      blocks.put(clazz, encoder);
    }

    for (Toml toml : data.getTables("descriptor")) {
      Class<? extends Descriptor<? extends Mob>> clazz =
          (Class<? extends Descriptor<? extends Mob>>) Class.forName(toml.getString("type"));

      DescriptorEncoder<? extends Descriptor<? extends Mob>> encoder =
          (DescriptorEncoder<? extends Descriptor<? extends Mob>>) Class
              .forName(toml.getString("encoder")).newInstance();

      descriptors.put(clazz, encoder);
    }
  }

  /**
   * Encodes the specified UpdateBlock.
   * 
   * @param block The UpdateBlock to encode.
   * @param builder The FrameBuilder representation of the UpdateBlock.
   */
  @SuppressWarnings("unchecked")
  public <B extends UpdateBlock> void encode(B block, FrameBuilder builder) {
    UpdateBlockEncoder<B> encoder = (UpdateBlockEncoder<B>) blocks.get(block.getClass());
    encoder.encode(block, builder);
  }

  /**
   * Encodes the specified Descriptor.
   * 
   * @param descriptor The Descriptor to encode.
   * @param builder The FrameBuilder representation of the Descriptor.
   * @param blockBuilder The UpdateBlock FrameBuilder.
   */
  @SuppressWarnings("unchecked")
  public <D extends Descriptor<? extends Mob>> void encode(D descriptor, FrameBuilder builder,
      FrameBuilder blockBuilder) {
    DescriptorEncoder<D> encoder = (DescriptorEncoder<D>) descriptors.get(descriptor.getClass());
    encoder.encode(descriptor, builder, blockBuilder);
  }

}
