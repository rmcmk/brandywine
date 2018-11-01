package me.ryleykimmel.brandywine.common.rsa;

import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

/**
 * A static-utility class containing extension or helper methods for Rsa {@link PublicKey} and {@link PrivateKey}
 */
public final class RsaKeyGenerator {

  /**
   * The RSA algorithm name.
   */
  static final String RSA = "RSA";
  /**
   * The path to our locally stored PEM encoded RSA public key.
   */
  static final Path RSA_PUBLIC_KEY_PATH = Paths.get("data", "rsa", "id_rsa.pub");
  /**
   * The path to our locally stored PEM encoded RSA private key.
   */
  static final Path RSA_PRIVATE_KEY_PATH = Paths.get("data", "rsa", "id_rsa");
  /**
   * The Logger for this class.
   */
  private static final Logger logger = LogManager.getLogger(RsaKeyGenerator.class);
  /**
   * The default modulus length, specified in the number of bits.
   */
  private static final int RSA_BITS = 1024;

  // Add the BouncyCastleProvider to our security config
  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * Creates a PEM encoded RSA key pair.
   */
  private static void createKeyPair() throws IOException {
    KeyPairGenerator generator;
    try {
      generator = KeyPairGenerator.getInstance(RSA, PROVIDER_NAME);
      generator.initialize(RSA_BITS);
    } catch (NoSuchAlgorithmException | NoSuchProviderException ignored) {
      throw new AssertionError(); // Should not happen
    }

    logger.info("Generating {} key pair using {} provider and {} bits.", RSA, PROVIDER_NAME,
        RSA_BITS);

    KeyPair pair = generator.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();

    writeToPem(publicKey, RSA_PUBLIC_KEY_PATH);
    writeToPem(privateKey, RSA_PRIVATE_KEY_PATH);

    logger.info("====================================================================");
    logger.info("public static final BigInteger RSA_EXPONENT = new BigInteger(\"" + publicKey
        .getPublicExponent().toString() + "\");");
    logger.info(
        "public static final BigInteger RSA_MODULUS = new BigInteger(\"" + publicKey.getModulus()
            .toString() + "\");");
    logger.info("====================================================================");
  }

  /**
   * PEM encodes and writes the specified {@code object} to the specified {@link Path}.
   *
   * @param object The object to encode and write.
   * @param path The Path where the object will be locally written.
   */
  private static void writeToPem(Object object, Path path) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer)) {
      logger
          .info("Writing PEM representation of {} to {}", object.getClass().getSimpleName(), path);
      pemWriter.writeObject(object);
    }
  }

  public static void main(String[] args) {
    try {
      createKeyPair();
    } catch (IOException cause) {
      logger.error("Unable to create RSA key pair.", cause);
    }
  }

}
