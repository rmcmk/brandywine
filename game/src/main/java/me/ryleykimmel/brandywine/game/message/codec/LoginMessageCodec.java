package me.ryleykimmel.brandywine.game.message.codec;

import io.netty.buffer.ByteBuf;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import me.ryleykimmel.brandywine.common.rsa.RsaKeyPairSupplier;
import me.ryleykimmel.brandywine.common.util.ByteBufUtil;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.message.LoginMessage;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.message.MessageCodec;

/**
 * MessageCodec for the {@link LoginMessage}.
 */
public final class LoginMessageCodec extends MessageCodec<LoginMessage> {

  /**
   * The RSA key pair for the LoginMessageCodec.
   */
  private static final RsaKeyPairSupplier KEY_PAIR = RsaKeyPairSupplier.create();

  /**
   * The amount of {@code byte}s that are not part of the RSA block.
   */
  private static final int NON_RSA_BYTE_LENGTH = Byte.BYTES * 40;

  @Override
  public LoginMessage decode(FrameReader reader) {
    ByteBuf buffer = reader.getBuffer();

    int length = buffer.readUnsignedByte() - NON_RSA_BYTE_LENGTH;
    if (length < Byte.BYTES) {
      throw new IllegalArgumentException("login block has no readable bytes");
    }

    int dummy = buffer.readUnsignedByte();
    int clientVersion = buffer.readShort();
    int detail = buffer.readUnsignedByte();

    int[] archiveChecksums = new int[FileSystem.MAXIMUM_ARCHIVES];
    Arrays.setAll(archiveChecksums, index -> buffer.readInt());

    int blockLength = buffer.readUnsignedByte();
    int available = length - Byte.BYTES;
    if (blockLength != available) {
      throw new IllegalArgumentException("Available: " + available + ", expected: " + blockLength);
    }

    ByteBuf cipheredBuffer = buffer.alloc().buffer(blockLength);

    RSAPrivateKey privateKey = KEY_PAIR.getPrivateKey();
    byte[] bytes = new byte[blockLength];
    buffer.readBytes(bytes);

    BigInteger encodedBigInteger = new BigInteger(bytes);
    BigInteger decodedBigInteger = encodedBigInteger
        .modPow(privateKey.getPrivateExponent(), privateKey.getModulus());
    cipheredBuffer.writeBytes(decodedBigInteger.toByteArray());

    try {
      int blockOperationCode = cipheredBuffer.readUnsignedByte();

      long clientSessionId = cipheredBuffer.readLong();
      long serverSessionId = cipheredBuffer.readLong();

      int userId = cipheredBuffer.readInt();

      String username = ByteBufUtil.readJString(cipheredBuffer);
      String password = ByteBufUtil.readJString(cipheredBuffer);

      return new LoginMessage(dummy, clientVersion, detail, archiveChecksums, blockLength,
          blockOperationCode, clientSessionId, serverSessionId, userId, username, password);
    } finally {
      cipheredBuffer.release();
    }
  }

}
