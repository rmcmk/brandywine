package me.ryleykimmel.brandywine.game.msg.codec;

import io.netty.buffer.ByteBuf;
import me.ryleykimmel.brandywine.common.util.ByteBufUtil;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.game.msg.LoginMessage;
import me.ryleykimmel.brandywine.network.frame.FrameReader;
import me.ryleykimmel.brandywine.network.msg.MessageCodec;
import me.ryleykimmel.brandywine.network.rsa.RsaKeygen;
import me.ryleykimmel.brandywine.network.rsa.RsaKeypair;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;

/**
 * MessageCodec for the {@link LoginMessage}.
 */
public final class LoginMessageCodec extends MessageCodec<LoginMessage> {

  @Override
  public LoginMessage decode(FrameReader reader) {
    ByteBuf buffer = reader.getBuffer();

    int length = buffer.readUnsignedByte(); // TODO: Verify
    int magic = buffer.readUnsignedByte();
    int clientVersion = buffer.readShort();
    int detail = buffer.readUnsignedByte();

    int[] archiveChecksums = new int[FileSystem.MAXIMUM_ARCHIVES];
    Arrays.setAll(archiveChecksums, index -> buffer.readInt());

    int blockLength = buffer.readUnsignedByte();

    ByteBuf cipheredBuffer = buffer.alloc().buffer(blockLength);

    RsaKeypair keypair = RsaKeygen.RSA_KEYPAIR;
    RSAPrivateKey privateKey = keypair.getPrivateKey();
    byte[] bytes = new byte[blockLength];
    buffer.readBytes(bytes);

    BigInteger encodedBigInteger = new BigInteger(bytes);
    BigInteger decodedBigInteger = encodedBigInteger.modPow(privateKey.getPrivateExponent(),
      privateKey.getModulus());
    cipheredBuffer.writeBytes(decodedBigInteger.toByteArray());

    try {
      int blockOperationCode = cipheredBuffer.readUnsignedByte();

      int[] sessionKeys = new int[4]; // TODO: Rid of magic number?
      Arrays.setAll(sessionKeys, index -> cipheredBuffer.readInt());

      int userId = cipheredBuffer.readInt();

      String username = ByteBufUtil.readJString(cipheredBuffer);
      String password = ByteBufUtil.readJString(cipheredBuffer);

      return new LoginMessage(magic, clientVersion, detail, archiveChecksums, blockLength,
                               blockOperationCode, sessionKeys, userId, username, password);
    } finally {
      cipheredBuffer.release();
    }
  }

}
