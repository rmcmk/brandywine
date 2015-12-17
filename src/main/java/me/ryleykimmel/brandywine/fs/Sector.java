package me.ryleykimmel.brandywine.fs;

import com.google.common.base.Preconditions;

import me.ryleykimmel.brandywine.common.Buffer;

public final class Sector {

	public static final int BYTES = Byte.BYTES * 8;

	private final int id;
	private final int block;
	private final int nextBlock;
	private final int index;

	public Sector(int id, int block, int nextBlock, int index) {
		this.id = id;
		this.block = block;
		this.nextBlock = nextBlock;
		this.index = index;
	}

	public static Sector decode(Buffer buffer) {
		Preconditions.checkArgument(buffer.remaining() >= BYTES, "Incorrect buffer length: " + buffer.remaining() + ", expected: " + BYTES);

		int id = buffer.getUnsignedShort();
		int block = buffer.getUnsignedShort();
		int nextBlock = buffer.getUnsignedTriByte();
		int index = buffer.getUnsignedByte();

		return new Sector(id, block, nextBlock, index);
	}

	public int getId() {
		return id;
	}

	public int getBlock() {
		return block;
	}

	public int getNextBlock() {
		return nextBlock;
	}

	public int getIndex() {
		return index;
	}

}