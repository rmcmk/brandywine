package me.ryleykimmel.brandywine.fs;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;

import com.google.common.base.Preconditions;

import me.ryleykimmel.brandywine.common.Buffer;
import me.ryleykimmel.brandywine.fs.archive.Archive;

/**
 * A file system which consists of data and index files, index files point to blocks in the data file, which contains the actual data.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class FileSystem {

	/**
	 * The configuration Cache index.
	 */
	public static final int CONFIG_INDEX = 0;

	/**
	 * The model Cache index.
	 */
	public static final int MODEL_INDEX = 1;

	/**
	 * The animation Cache index.
	 */
	public static final int ANIMATION_INDEX = 2;

	/**
	 * The sound and music Cache index.
	 */
	public static final int MIDI_INDEX = 3;

	/**
	 * The map Cache index.
	 */
	public static final int MAP_INDEX = 4;

	/**
	 * The title Archive id.
	 */
	public static final int TITLE_ARCHIVE = 1;

	/**
	 * The configuration Archive id.
	 */
	public static final int CONFIG_ARCHIVE = 2;

	/**
	 * The interface Archive id.
	 */
	public static final int INTERFACE_ARCHIVE = 3;

	/**
	 * The media Archive id.
	 */
	public static final int MEDIA_ARCHIVE = 4;

	/**
	 * The manifest Archive id.
	 */
	public static final int MANIFEST_ARCHIVE = 5;

	/**
	 * The textures Archive id.
	 */
	public static final int TEXTURES_ARCHIVE = 6;

	/**
	 * The word-filter Archive id.
	 */
	public static final int WORD_ARCHIVE = 7;

	/**
	 * The sound and music Archive id.
	 */
	public static final int MIDI_ARCHIVE = 8;

	/**
	 * The maximum amount of Archives this FileSystem is allowed to have.
	 */
	public static final int MAXIMUM_ARCHIVES = 9;

	/**
	 * The maximum amount of Cache indices this FileSystem is allowed to have.
	 */
	private static final int MAXIMUM_INDICES = 256;

	/**
	 * Represents the prefix of this FileSystem's main cache files.
	 */
	private static final String DATA_PREFIX = "main_file_cache.dat";

	/**
	 * Represents the prefix of this FileSystem's index files.
	 */
	private static final String INDEX_PREFIX = "main_file_cache.idx";

	/**
	 * All of the {@link Archive archives} within this FileSystem.
	 */
	private final Archive[] archives;

	/**
	 * All of the {@link Cache caches} within this FileSystem.
	 */
	private final Cache[] caches;

	/**
	 * The checksums of all of the Archives within this FileSystem.
	 */
	private final int[] archiveChecksums;

	/**
	 * The table of the Archive checksums.
	 */
	private final Buffer archiveChecksumTable;

	/**
	 * Constructs a new {@link FileSystem} with the specified Caches, Archives and Archive hashes.
	 *
	 * @param caches All of the Caches within this FileSystem.
	 * @param archives All of the Archives within this FileSystem.
	 * @param archiveChecksums All of the Archive hashes.
	 * @param archiveChecksumTable A table of the Archive checksums.
	 */
	private FileSystem(Cache[] caches, Archive[] archives, int[] archiveChecksums, Buffer archiveChecksumTable) {
		this.caches = caches;
		this.archives = archives;
		this.archiveChecksums = archiveChecksums;
		this.archiveChecksumTable = archiveChecksumTable;
	}

	/**
	 * Constructs and initializes a {@link FileSystem} from the specified {@code directory}.
	 *
	 * @param directory The directory of the FileSystem.
	 * @return The created FileSystem, never {@code null}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static FileSystem create(String directory) throws IOException {
		Path root = Paths.get(directory);
		Path data = root.resolve(DATA_PREFIX);

		Preconditions.checkArgument(Files.isDirectory(root), "Supplied path must be a directory!");
		Preconditions.checkArgument(Files.exists(data), "No data file found in the specified path!");

		SeekableByteChannel dataChannel = Files.newByteChannel(data);

		Cache[] caches = new Cache[MAXIMUM_INDICES];
		Archive[] archives = new Archive[MAXIMUM_ARCHIVES];
		int[] archiveChecksums = new int[archives.length];

		for (int index = 0; index < caches.length; index++) {
			Path path = root.resolve(INDEX_PREFIX + index);

			// indices are in order, if one does not exist there are no more indices.
			if (Files.notExists(path)) {
				break;
			}

			SeekableByteChannel indexChannel = Files.newByteChannel(path);
			caches[index] = new Cache(index, dataChannel, indexChannel);
		}

		Cache cache = Preconditions.checkNotNull(caches[CONFIG_INDEX], "Configuration cache is null - unable to decode archives");

		Buffer archiveChecksumTable = buildArchiveHashTable(cache);
		for (int index = 0; index < archiveChecksums.length; index++) {
			archiveChecksums[index] = archiveChecksumTable.getInt(index * Integer.BYTES);
		}

		// We don't use index 0
		for (int index = 1; index < archives.length; index++) {
			Buffer buffer = cache.getFile(index);
			archives[index] = Archive.decode(buffer);
		}

		FileSystem fileSystem = new FileSystem(caches, archives, archiveChecksums, archiveChecksumTable);

		return fileSystem;
	}

	/**
	 * Builds the Archive hash table from the specified Cache.
	 *
	 * @param cache The Cache to build the Archive hash table from.
	 * @return The Archive hash table.
	 * @throws IOException If some I/O exception occurs.
	 */
	private static Buffer buildArchiveHashTable(Cache cache) throws IOException {
		int[] crcs = new int[MAXIMUM_ARCHIVES];

		CRC32 crc32 = new CRC32();
		for (int index = 1; index < crcs.length; index++) {
			crc32.reset();
			crc32.update(cache.getFile(index).getByteBuffer());
			crcs[index] = (int) crc32.getValue();
		}

		Buffer buffer = Buffer.allocate((crcs.length + 1) * Integer.BYTES);

		int hash = 1234;
		for (int crc : crcs) {
			hash = (hash << 1) + crc;
			buffer.putInt(crc);
		}

		buffer.putInt(hash);
		buffer.flip();

		return buffer.asReadOnlyBuffer();
	}

	/**
	 * Gets a {@link Archive} for the specified {@code id}.
	 * <p>
	 * This method fails fast if the id is not valid or if the returned Archive is {@code null}
	 * </p>
	 *
	 * @param id The id of the Archive to fetch.
	 * @return The Archive for the specified id, never {@code null}.
	 */
	public Archive getArchive(int id) {
		Preconditions.checkElementIndex(id, archives.length);
		Preconditions.checkNotNull(archives[id]);

		return archives[id];
	}

	/**
	 * Gets a {@link Cache} for the specified {@code id}.
	 * <p>
	 * This method fails fast if the id is not valid or if the returned Cache is {@code null}
	 * </p>
	 *
	 * @param id The id of the Cache to fetch.
	 * @return The Cache for the specified id, never {@code null}.
	 */
	public Cache getCache(int id) {
		Preconditions.checkElementIndex(id, caches.length);
		Preconditions.checkNotNull(caches[id]);

		return caches[id];
	}

	/**
	 * Gets the specified Index as a {@link ByteBuffer} from some {@link Cache} as specified by the {@code index}
	 *
	 * @param index The index of the Cache.
	 * @param id The id of the File.
	 * @return A ByteBuffer representation of the Index.
	 * @throws IOException If some I/O exception occurs.
	 */
	public Buffer getFile(int index, int id) throws IOException {
		return getCache(index).getFile(id);
	}

	/**
	 * Gets the Archive hashes.
	 *
	 * @return The Archive hashes.
	 */
	public synchronized int[] getArchiveChecksums() {
		return archiveChecksums.clone();
	}

	/**
	 * Gets the Archive hash table.
	 *
	 * @return The Archive hash table.
	 */
	public synchronized Buffer getArchiveChecksumTable() {
		return archiveChecksumTable.duplicate();
	}

}