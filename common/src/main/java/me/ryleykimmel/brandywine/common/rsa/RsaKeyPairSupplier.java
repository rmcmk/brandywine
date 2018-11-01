package me.ryleykimmel.brandywine.common.rsa;

import static me.ryleykimmel.brandywine.common.rsa.RsaKeyGenerator.RSA;
import static me.ryleykimmel.brandywine.common.rsa.RsaKeyGenerator.RSA_PRIVATE_KEY_PATH;
import static me.ryleykimmel.brandywine.common.rsa.RsaKeyGenerator.RSA_PUBLIC_KEY_PATH;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * A basic Supplier for a local RSA key paid.
 */
public final class RsaKeyPairSupplier {

  /**
   * The private RSA key.
   */
  private final RSAPrivateKey privateKey;
  /**
   * The public RSA key.
   */
  private final RSAPublicKey publicKey;

  /**
   * Constructs a new {@link RsaKeyPairSupplier}.
   *
   * @param privateKey The private RSA key.
   * @param publicKey The public RSA key.
   */
  private RsaKeyPairSupplier(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
    this.privateKey = privateKey;
    this.publicKey = publicKey;
  }

  /**
   * Attempts to create a PrivateKey from the locally stored PEM file.
   *
   * @return The private RSA key.
   * @see RsaKeyGenerator#RSA_PRIVATE_KEY_PATH
   */
  private static RSAPrivateKey createPrivateKey() throws Exception {
    PemObject object = new PemReader(Files.newBufferedReader(RSA_PRIVATE_KEY_PATH)).readPemObject();
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(object.getContent());
    KeyFactory factory = KeyFactory.getInstance(RSA, PROVIDER_NAME);
    return (RSAPrivateKey) factory.generatePrivate(spec);
  }

  /**
   * Attempts to create a PublicKey from the locally stored PEM file.
   *
   * @return The private RSA key.
   * @see RsaKeyGenerator#RSA_PUBLIC_KEY_PATH
   */
  private static RSAPublicKey createPublicKey() throws Exception {
    PemObject object = new PemReader(Files.newBufferedReader(RSA_PUBLIC_KEY_PATH)).readPemObject();
    X509EncodedKeySpec spec = new X509EncodedKeySpec(object.getContent());
    KeyFactory factory = KeyFactory.getInstance(RSA, PROVIDER_NAME);
    return (RSAPublicKey) factory.generatePublic(spec);
  }

  /**
   * Creates a new {@link RsaKeyPairSupplier}.
   *
   * @return A new RsaKeyPairSupplier, never {@code nulll}.
   */
  public static RsaKeyPairSupplier create() {
    try {
      RSAPrivateKey privateKey = createPrivateKey();
      RSAPublicKey publicKey = createPublicKey();
      return new RsaKeyPairSupplier(privateKey, publicKey);
    } catch (Exception cause) {
      throw new RuntimeException("Unable to read public or private RSA key pair.", cause);
    }
  }

  /**
   * Gets the private RSA key.
   *
   * @return The private RSA key.
   */
  public RSAPrivateKey getPrivateKey() {
    return privateKey;
  }

  /**
   * Gets the public RSA key.
   *
   * @return The public RSA key.
   */
  public RSAPublicKey getPublicKey() {
    return publicKey;
  }

}
