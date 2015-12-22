package me.ryleykimmel.brandywine.network.msg.codec;

import java.math.BigInteger;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import me.ryleykimmel.brandywine.common.util.ByteBufUtil;
import me.ryleykimmel.brandywine.fs.FileSystem;
import me.ryleykimmel.brandywine.network.game.frame.Frame;
import me.ryleykimmel.brandywine.network.msg.Decodes;
import me.ryleykimmel.brandywine.network.msg.MessageDecoder;
import me.ryleykimmel.brandywine.network.msg.impl.LoginMessage;

/**
 * Decodes the {@link LoginMessage}
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
@Decodes({16, 18})
public final class LoginMessageDecoder implements MessageDecoder<LoginMessage> {

  /**
   * A flag denoting whether or not RSA encryption is enabled.
   */
  private static final boolean ENABLE_RSA = true;

  /**
   * The RSA modulus.
   */
  private static final BigInteger RSA_MODULUS = new BigInteger(
      "139336679038918681940011842953974272585051410904649770658577435855867183633665812687741993719831868431475682068631441220906898769317525269995093022281777805174835737910462193402453919341932203893011766236167064635635832380674426197116080563784883957567152084645057564939648636182331623005811277254720035536241");

  /**
   * The RSA exponent.
   */
      private static final BigInteger RSA_EXPONENT = new BigInteger(
          "122066559200764105847137341522490789724859876187185283619811173874738826634470436084258034811334465937176630152256062472452945449770303734554500065326243031415229867217558700157658911934536431275351026725267163220047422105624981686172662220393958694949845524268416752886307282943151281612837317011104001141977");

  @Override
  public LoginMessage decode(Frame frame) {
    ByteBuf buffer = frame.content();

    int magic = buffer.readUnsignedByte();
    int clientVersion = buffer.readShort();
    int detail = buffer.readUnsignedByte();

    int[] archiveChecksums = new int[FileSystem.MAXIMUM_ARCHIVES];
    Arrays.setAll(archiveChecksums, index -> buffer.readInt());

    int blockLength = buffer.readUnsignedByte();

    ByteBuf cipheredBuffer = buffer.alloc().buffer(blockLength);

    if (ENABLE_RSA) {
      byte[] bytes = new byte[blockLength];
      buffer.readBytes(bytes);

      BigInteger encodedBigInteger = new BigInteger(bytes);
      BigInteger decodedBigInteger = encodedBigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
      cipheredBuffer.writeBytes(decodedBigInteger.toByteArray());
    } else {
      cipheredBuffer.writeBytes(buffer);
    }

    int blockOperationCode = cipheredBuffer.readUnsignedByte();

    int[] sessionKeys = new int[4]; // TODO: Rid of magic number?
    Arrays.setAll(sessionKeys, index -> cipheredBuffer.readInt());

    int userId = cipheredBuffer.readInt();

    String username = ByteBufUtil.readJString(cipheredBuffer);
    String password = ByteBufUtil.readJString(cipheredBuffer);

    return new LoginMessage(magic, clientVersion, detail, archiveChecksums, blockLength,
        blockOperationCode, sessionKeys, userId, username, password);
  }

}
