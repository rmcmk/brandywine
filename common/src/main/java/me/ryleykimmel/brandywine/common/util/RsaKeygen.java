package me.ryleykimmel.brandywine.common.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

/**
 * A utility class which generates RSA public and private keypairs.
 */
public final class RsaKeygen {

  /**
   * The logger for this class.
   */
  private static final Logger logger = LoggerFactory.getLogger(RsaKeygen.class);

  /**
   * The RSA algorithm name.
   */
  private static final String ALGORITHM = "RSA";

  /**
   * The modulus' length, specified in the number of bits.
   */
  private static final int BITS = 1024;

  /**
   * Generates a public and private RSA keypair.
   * 
   * @throws NoSuchAlgorithmException If the specified algorithm is not available in this
   * environment.
   * @throws InvalidKeySpecException If any key cannot be processed.
   * @throws IOException If some I/O exception occurs.
   */
  private void write() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
    generator.initialize(BITS);

    KeyPair pair = generator.generateKeyPair();
    KeyFactory factory = KeyFactory.getInstance(ALGORITHM);

    RSAPublicKeySpec publicSpec = factory.getKeySpec(pair.getPublic(), RSAPublicKeySpec.class);
    RSAPrivateKeySpec privateSpec = factory.getKeySpec(pair.getPrivate(), RSAPrivateKeySpec.class);

    write("rsa_public", publicSpec.getModulus(), publicSpec.getPublicExponent());
    write("rsa_private", privateSpec.getModulus(), privateSpec.getPrivateExponent());
  }

  /**
   * Writes a RSA keypair to disk.
   * 
   * @param root The root directory to write the RSA keypair.
   * @param modulus The RSA modulus.
   * @param exponent The RSA exponent.
   * @throws IOException If some I/O exception occurs.
   */
  private void write(String root, BigInteger modulus, BigInteger exponent) throws IOException {
    Path path = Paths.get("data", root);

    // Create the directories if they don't exist.
    if (Files.notExists(path)) {
      Files.createDirectories(path);
    }

    try (BufferedWriter writer = Files.newBufferedWriter(path.resolve("rsa.key"),
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      writer.write("modulus = \"");
      writer.write(modulus.toString());
      writer.write("\"");
      writer.newLine();

      writer.write("exponent = \"");
      writer.write(exponent.toString());
      writer.write("\"");
      writer.newLine();
    }
  }

  /**
   * Generates both the rsa public and private key pairs.
   */
  public void generate() {
    Stopwatch stopwatch = Stopwatch.createStarted();

    try {
      write();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException cause) {
      logger.error("Unable to generate RSA keypair!", cause);
      return;
    }

    logger.info("Took {}ms to generate public and private RSA keypairs.",
        stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }

}
