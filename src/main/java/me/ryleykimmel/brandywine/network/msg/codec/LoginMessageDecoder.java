package me.ryleykimmel.brandywine.network.msg.codec;

import java.math.BigInteger;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
@Decodes({ 16, 18 })
public final class LoginMessageDecoder implements MessageDecoder<LoginMessage> {

	/**
	 * A flag denoting whether or not RSA encryption is enabled.
	 */
	private static final boolean ENABLE_RSA = false;

	/**
	 * The RSA modules.
	 */
	private static final BigInteger RSA_MODULUS = new BigInteger("14369095800122584910050349689375806694898492138048265956411"
			+ "35961528009343521194968733868752142512642584252089951673164973317865959427542909838498785496302267419616107804"
			+ "16197036711585670124061149988186026407785250364328460839202438651793652051153157765358767514800252431284681765" + "433239888090564804146588087023");

	/**
	 * The RSA exponent.
	 */
	private static final BigInteger RSA_EXPONENT = new BigInteger("1244253149605500242069910653328771579314722109395057895580"
			+ "12215720454903710618146200843877022273818555405810618059191162604008259757866640421952188957253368398733319663"
			+ "236323097864278319463888334484786055755767881706264786840339899269810859874287402892848784247637729987603089254" + "067178011764721326471352835473");

	@Override
	public LoginMessage decode(Frame frame) {
		ByteBuf buffer = frame.content();

		int magic = buffer.readUnsignedByte();
		int clientVersion = buffer.readShort();
		int detail = buffer.readUnsignedByte();

		int[] archiveChecksums = new int[FileSystem.MAXIMUM_ARCHIVES];
		Arrays.setAll(archiveChecksums, index -> buffer.readInt());

		int blockLength = buffer.readUnsignedByte();

		ByteBuf cipheredBuffer = buffer;

		if (ENABLE_RSA) {
			byte[] bytes = new byte[blockLength];
			buffer.readBytes(bytes);

			BigInteger encodedBigInteger = new BigInteger(bytes);
			BigInteger decodedBigInteger = encodedBigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
			cipheredBuffer = Unpooled.wrappedBuffer(decodedBigInteger.toByteArray());
		}

		int blockOperationCode = cipheredBuffer.readUnsignedByte();

		int[] sessionKeys = new int[4]; // TODO: Rid of magic number?
		Arrays.setAll(sessionKeys, index -> cipheredBuffer.readInt());

		int userId = cipheredBuffer.readInt();

		String username = ByteBufUtil.readJString(cipheredBuffer);
		String password = ByteBufUtil.readJString(cipheredBuffer);

		return new LoginMessage(magic, clientVersion, detail, archiveChecksums, blockLength, blockOperationCode, sessionKeys, userId, username, password);
	}

}