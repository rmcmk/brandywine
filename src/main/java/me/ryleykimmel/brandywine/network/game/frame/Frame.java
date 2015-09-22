package me.ryleykimmel.brandywine.network.game.frame;

import java.util.Objects;

import com.google.common.base.MoreObjects;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Represents a Frame.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Frame {

	/**
	 * The maximum opcode a Frame may have.
	 */
	private static final int MAXIMUM_OPCODE = 255;

	/**
	 * The opcode or identifier of this Frame.
	 */
	private final int opcode;

	/**
	 * The type of this Frame.
	 */
	private final FrameType type;

	/**
	 * The payload of this Frame.
	 */
	private final ByteBuf payload;

	/**
	 * The length of this Frame.
	 */
	private final int length;

	/**
	 * Constructs a new {@link Frame} with the specified opcode, FrameType and payload.
	 *
	 * @param opcode The opcode or identifier of this Frame.
	 * @param type The type of this Frame.
	 * @param payload The payload of this Frame.
	 */
	public Frame(int opcode, FrameType type, ByteBuf payload) {
		this.opcode = opcode;
		this.type = type;
		this.payload = payload;
		this.length = payload.readableBytes();
	}

	/**
	 * Constructs a new fixed, empty Frame for the specified opcode.
	 * 
	 * @param opcode The opcode or identifier of this Frame.
	 */
	public Frame(int opcode) {
		this(opcode, FrameType.EMPTY, Unpooled.EMPTY_BUFFER);
	}

	/**
	 * Gets the opcode of this Frame.
	 *
	 * @return The opcode or identifier of this Frame.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Gets the length (readable bytes) of this Frame.
	 *
	 * @return The length of this Frame.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the payload of this Frame.
	 *
	 * @return The payload of this Frame.
	 */
	public ByteBuf getPayload() {
		return payload;
	}

	/**
	 * Gets the type of this Frame.
	 *
	 * @return The type of this Frame.
	 */
	public FrameType getType() {
		return type;
	}

	/**
	 * Returns whether or not this Frame has a valid opcode.
	 *
	 * @return {@code true} iff this Frame has a valid opcode, otherwise {@code false}.
	 */
	public boolean hasValidOpcode() {
		return opcode >= 0 && opcode <= MAXIMUM_OPCODE;
	}

	@Override
	public int hashCode() {
		return Objects.hash(opcode, type, payload);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Frame) {
			Frame other = (Frame) obj;
			return opcode == other.opcode && type == other.type && Objects.equals(payload, other.payload);
		}

		return false;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("opcode", opcode).add("type", type).add("payload", payload).toString();
	}

}