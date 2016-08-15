package me.ryleykimmel.brandywine.network.rsa;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Represents a public and private RSA key pair.
 */
public final class RsaKeypair {

  /**
   * The private RSA key.
   */
  private final RSAPrivateKey privateKey;

  /**
   * The public RSA key.
   */
  private final RSAPublicKey publicKey;

  /**
   * Constructs a new RsaKeypair.
   *
   * @param privateKey The private RSA key.
   * @param publicKey The public RSA key.
   */
  public RsaKeypair(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  /**
   * Gets the RSA private key.
   *
   * @return The RSA private key.
   */
  public RSAPrivateKey getPrivateKey() {
    return privateKey;
  }

  /**
   * Gets the RSA public key.
   *
   * @return The RSA public key.
   */
  public RSAPublicKey getPublicKey() {
    return publicKey;
  }

}
