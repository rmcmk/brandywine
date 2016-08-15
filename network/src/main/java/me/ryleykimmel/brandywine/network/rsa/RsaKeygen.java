package me.ryleykimmel.brandywine.network.rsa;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * A utility class which creates and loads RSA public and private keypairs.
 */
public final class RsaKeygen {

  /**
   * Represents the RsaKeypair.
   */
  public static final RsaKeypair RSA_KEYPAIR;

  /**
   * Where the RSA private key file is stored.
   */
  private static final Path PRIVATE_KEY_FILE = Paths.get("data", "rsa", "priv.pem");

  /**
   * Where the RSA public key file is stored.
   */
  private static final Path PUBLIC_KEY_FILE = Paths.get("data", "rsa", "pub.pem");

  /**
   * The RSA algorithm name.
   */
  private static final String ALGORITHM = "RSA";

  /**
   * The modulus' length, specified in the number of bits.
   * <p>Please note: 1024 bits is the maximum the RS client will natively support.</p>
   */
  private static final int BITS = 1024;

  /**
   * Attempts to read an existing RSA private key pair from the {@link #PRIVATE_KEY_FILE} otherwise creates a new RSA key pair.
   *
   * @return The read or created RSAPrivateKey.
   */
  public static RSAPrivateKey readPrivateKey() throws RsaKeygenException {
    try (PemReader pemReader = new PemReader(Files.newBufferedReader(PRIVATE_KEY_FILE))) {
      PemObject pem = pemReader.readPemObject();
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pem.getContent());
      KeyFactory factory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
      return (RSAPrivateKey) factory.generatePrivate(keySpec);
    } catch (IOException cause) {
      throw new UncheckedIOException("RSA private key store not found.", cause);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException cause) {
      throw new RsaKeygenException("Unable to read existing RSA pem file.", cause);
    }
  }

  /**
   * Attempts to read an existing RSA public key pair from the {@link #PUBLIC_KEY_FILE}.
   *
   * @return The read RSAPublicKey.
   */
  public static RSAPublicKey readPublicKey() throws RsaKeygenException {
    try (PemReader pemReader = new PemReader(Files.newBufferedReader(PUBLIC_KEY_FILE))) {
      PemObject pem = pemReader.readPemObject();
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pem.getContent());
      KeyFactory factory = KeyFactory.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
      return (RSAPublicKey) factory.generatePublic(keySpec);
    } catch (IOException cause) {
      throw new UncheckedIOException("RSA public key store not found, if lost you must create a new public and private RSA key pair.",
                                      cause);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException cause) {
      throw new RsaKeygenException("Unable to read existing RSA pem file.", cause);
    }
  }

  /**
   * Creates both the RSA public and private key pairs.
   */
  private static void create() throws RsaKeygenException {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM,
        BouncyCastleProvider.PROVIDER_NAME);
      keyPairGenerator.initialize(BITS);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

      try (PemWriter writer = new PemWriter(new FileWriter(PRIVATE_KEY_FILE.toFile()))) {
        writer.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
      } catch (IOException cause) {
        throw new RsaKeygenException("Unable to write private RSA pem file.", cause);
      }

      try (PemWriter writer = new PemWriter(new FileWriter(PUBLIC_KEY_FILE.toFile()))) {
        writer.writeObject(new PemObject("RSA PUBLIC KEY", publicKey.getEncoded()));
      } catch (IOException cause) {
        throw new RsaKeygenException("Unable to write public RSA pem file.", cause);
      }
    } catch (NoSuchAlgorithmException | NoSuchProviderException cause) {
      throw new RsaKeygenException("Unable to create RSA key pair.", cause);
    }
  }

  static {
    Security.addProvider(new BouncyCastleProvider());

    try {
      RSA_KEYPAIR = new RsaKeypair(readPrivateKey(), readPublicKey());
    } catch (RsaKeygenException cause) {
      throw new AssertionError("Unable to construct RSA key pair. See RsaKeygen for information on generating RSA keys.",
                                cause);
    }
  }

}
