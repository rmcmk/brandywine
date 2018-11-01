package me.ryleykimmel.brandywine.network.isaac;

import java.util.Arrays;

/**
 * A pair of two {@link IsaacRandom} random number generators used as a stream cipher. One takes the role of an encoder for this endpoint, the other takes the role of a decoder for
 * this endpoint.
 */
public final class IsaacRandomPair {

  /**
   * The IsaacRandom number generator used to decode data.
   */
  private final IsaacRandom decodingRandom;

  /**
   * The IsaacRandom number generator used to encode data.
   */
  private final IsaacRandom encodingRandom;

  /**
   * Constructs a new {@link IsaacRandomPair} with the specified encoding and decoding random.
   *
   * @param encodingRandom The IsaacRandom number generator used for encoding.
   * @param decodingRandom The IsaacRandom number generator used for decoding.
   */
  private IsaacRandomPair(IsaacRandom encodingRandom, IsaacRandom decodingRandom) {
    this.encodingRandom = encodingRandom;
    this.decodingRandom = decodingRandom;
  }

  /**
   * Constructs a new IsaacRandomPair from the specified seed.
   *
   * @param seed The seed to construct the IsaacRandomPair from.
   * @return A new IsaacRandomPair, never {@code null}.
   */
  public static IsaacRandomPair fromSeed(int[] seed) {
    int[] copy = seed.clone();

    IsaacRandom decodingRandom = new IsaacRandom(copy);
    Arrays.setAll(copy, index -> copy[index] += 50);
    IsaacRandom encodingRandom = new IsaacRandom(copy);

    return new IsaacRandomPair(encodingRandom, decodingRandom);
  }

  /**
   * Gets the IsaacRandom number generator used for decoding.
   *
   * @return The IsaacRandom number generator used for decoding.
   */
  public IsaacRandom getDecodingRandom() {
    return decodingRandom;
  }

  /**
   * Gets the IsaacRandom number generator used for encoding.
   *
   * @return The IsaacRandom number generator used for encoding.
   */
  public IsaacRandom getEncodingRandom() {
    return encodingRandom;
  }

}
