package me.ryleykimmel.brandywine.parser;

import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents a Parser with the source of a {@link Reader}.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @param <T> The parsed data.
 */
public abstract class ReaderParser<T> implements Parser<T, Reader> {

	/**
	 * The Path to the source.
	 */
	protected final Path path;

	/**
	 * Constructs a new {@link ReaderParser} with the specified path.
	 *
	 * @param path The path to the source.
	 */
	public ReaderParser(Path path) {
		this.path = path;
	}

	/**
	 * Constructs a new {@link ReaderParser} with the specified path.
	 *
	 * @param path The path to the source.
	 */
	public ReaderParser(String path) {
		this(Paths.get(path));
	}

}