package me.ryleykimmel.brandywine.network.isaac;

/**
 * A pair of two {@link IsaacRandom} random number generators used as a stream cipher. One takes the
 * role of an encoder for this endpoint, the other takes the role of a decoder for this endpoint.
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
  public IsaacRandomPair(IsaacRandom encodingRandom, IsaacRandom decodingRandom) {
    this.encodingRandom = encodingRandom;
    this.decodingRandom = decodingRandom;
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
