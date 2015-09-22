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

public final class RsaKeygen {

	private static final Logger logger = LoggerFactory.getLogger(RsaKeygen.class);

	private static final String ALGORITHM = "RSA";
	private static final int BITS = 1024;

	public void generate() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyGen.initialize(BITS);

		KeyPair keyPair = keyGen.generateKeyPair();
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);

		RSAPublicKeySpec publicSpec = factory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
		RSAPrivateKeySpec privateSpec = factory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);

		write("rsa_public", publicSpec.getModulus(), publicSpec.getPublicExponent());
		write("rsa_private", privateSpec.getModulus(), privateSpec.getPrivateExponent());
	}

	private void write(String prefix, BigInteger modulus, BigInteger exponent) throws IOException {
		Path path = Paths.get("data", prefix);
		
		// Create the directories if they don't exist.
		if (Files.notExists(path)) {
			Files.createDirectories(path);
		}

		try (BufferedWriter writer = Files.newBufferedWriter(path.resolve("rsa.key"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			writer.write("private static final BigInteger RSA_MODULUS = new BigInteger(\"");
			writer.write(modulus.toString());
			writer.write("\");");
			writer.newLine();

			writer.write("private static final BigInteger RSA_EXPONENT = new BigInteger(\"");
			writer.write(exponent.toString());
			writer.write("\");");
			writer.newLine();
		}
	}

	public static void main(String[] args) {
		RsaKeygen keygen = new RsaKeygen();

		Stopwatch stopwatch = Stopwatch.createStarted();
		try {
			keygen.generate();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException cause) {
			logger.error("Unable to generate RSA keypair!", cause);
			System.exit(0);
		}

		logger.info("Took {}ms to generate public and private RSA keypairs.", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
	}

}