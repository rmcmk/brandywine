package me.ryleykimmel.brandywine.network.message;

import me.ryleykimmel.brandywine.network.frame.Frame;
import me.ryleykimmel.brandywine.network.frame.FrameMetadataSet;

/**
 * A registrar which manages the registration of {@link Message}s to the game.
 */
public interface MessageRegistrar {

    /**
     * Returns a collection of mapped {@link Frame}s to {@link Message}s
     *
     * @return A collection of FrameMetadata for upstream and downstream Frames.
     */
    FrameMetadataSet build();
}