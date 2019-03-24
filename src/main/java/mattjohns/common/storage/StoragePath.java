package mattjohns.common.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Path related utilities.  Windows specific in some cases.
 * <p>
 * See also StorageDirectory, StoragePath and StorageStream.
 */
public class StoragePath {
	/**
	 * Combine the given paths in the order given.
	 * 
	 * @param first
	 * First path to use when combining.  This is the root path.
	 * 
	 * @param others
	 * Other paths to combine.
	 * 
	 * @return
	 * The new, normalized path.
	 */
	public static String combine(String first, String... others) {
		Path path = Paths.get(first, others);
		return path.normalize().toString();
	}

	public static boolean isExist(Path path) {
		return Files.exists(path);
	}

	public static boolean isExist(String path) {
		return Files.exists(Paths.get(path));
	}
}
