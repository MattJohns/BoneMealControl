package mattjohns.common.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Directory utilities. Windows specific in some cases.
 * 
 */
public class StorageDirectory {
	/**
	 * Creates a directory. All parent directories must be created or this will
	 * fail.
	 * 
	 * @param directory
	 * The path of the directory to create.
	 * 
	 * @return The path of the new directory.
	 * 
	 * @throws StorageException
	 */
	public static String create(String directory) throws StorageException {
		Path directoryPath = Paths.get(directory);

		try {
			Path destinationPath = Files.createDirectory(directoryPath);
			return destinationPath.normalize()
					.toString();
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
	}

	/**
	 * Creates a directory and also creates any parents that don't exist.
	 * 
	 * @param directory
	 * The path of the directory to create.
	 * 
	 * @return The path of the new directory.
	 * 
	 * @throws StorageException
	 */
	public static String createWithParents(String path) throws StorageException {
		Path directoryPath = Paths.get(path);

		try {
			Path destinationPath = Files.createDirectories(directoryPath);
			return destinationPath.normalize()
					.toString();
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
	}

	/**
	 * Checks if the directory exists on the file system.
	 * 
	 * @param pathText
	 * Path of the directory.
	 */
	public static boolean isExist(String pathText) {
		Path path = Paths.get(pathText);

		return StoragePath.isExist(path);
	}

	/**
	 * Creates a directory if it doesn't already exist.
	 * 
	 * @param pathText
	 * Path of directory to create.
	 * 
	 * @return The name of the new or existing directory.
	 */
	public static String createIfNotExist(String path) throws StorageException {
		Path sourcePath = Paths.get(path);

		if (StoragePath.isExist(sourcePath))
			return sourcePath.normalize()
					.toString();

		return create(sourcePath.toString());
	}

	/**
	 * Creates a folder within a directory.
	 * 
	 * @param baseDirectory
	 * The directory which the folder will be created in.
	 * 
	 * @param folder
	 * The name of the folder to create. The folder should only be 1 level deep
	 * otherwise this will fail.
	 * 
	 * @return The path of the new directory.
	 * 
	 * @throws StorageException
	 */
	public static String createIfNotExist(String baseDirectory, String folder) throws StorageException {
		Path path = Paths.get(baseDirectory, folder);

		return createIfNotExist(path.toString());
	}

	/**
	 * Checks if a directory is a child / subset of a directory.
	 * 
	 * @param parent
	 * The parent directory that may contain the child.
	 * 
	 * @param child
	 * The child which may be a subset of the given parent.
	 */
	public static boolean isChild(String parent, String child) {
		if (parent == null) {
			return false;
		}
		if (child == null) {
			return false;
		}

		Path parentPath = Paths.get(parent)
				.normalize();
		Path childPath = Paths.get(child)
				.normalize();

		return childPath.startsWith(parentPath);
	}

	/**
	 * Get the files in the given directory.
	 * 
	 * 'directory' can be relative, extension is mandatory, can't contain
	 * wildcards, should not have the dot prefix
	 * 
	 * @param directory
	 * The directory to check. Child directories are not searched.
	 * 
	 * @param extensionFilter
	 * The extension to search for. This cannot be blank and must not contain a
	 * '.' character .
	 * 
	 * @return List of files in the directory.
	 * 
	 * @throws StorageException
	 */
	public static ArrayList<String> fileGet(String directory, String extensionFilter) throws StorageException {
		ArrayList<String> returnValue = new ArrayList<>();

		try (Stream<Path> stream = Files.walk(Paths.get(directory), 1)) {
			stream.forEach(path -> {
				if (Files.isRegularFile(path)) {
					String pathText = path.toString();
					String extension = StorageFile.extensionGet(pathText);

					if (extension.compareToIgnoreCase(extensionFilter) == 0) {
						returnValue.add(pathText);
					}
				}
			});
		}
		catch (FileNotFoundException exception) {
			throw new StorageException("Unable to get files for directory \"" + directory + "\".", exception);
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}

		return returnValue;
	}

	protected static List<Path> subDirectoryPathGet(String baseDirectory) throws StorageException {
		Path basePath = Paths.get(baseDirectory);

		try {
			List<Path> walkResult = Files.walk(basePath, 1)
					.filter(Files::isDirectory)
					.collect(Collectors.toList());

			walkResult.remove(0);

			return walkResult;
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
	}

	public static ArrayList<String> subDirectoryGet(String baseDirectory) throws StorageException {
		List<Path> pathList = subDirectoryPathGet(baseDirectory);

		ArrayList<String> result = new ArrayList<>();

		for (Path path : pathList) {
			String pathText = path.toString();
			result.add(pathText);
		}

		return result;
	}

	public static ArrayList<String> subDirectoryFilenameGet(String baseDirectory) throws StorageException {
		List<Path> pathList = subDirectoryPathGet(baseDirectory);

		ArrayList<String> result = new ArrayList<>();

		for (Path path : pathList) {
			String pathText = path.toFile()
					.getName();
			result.add(pathText);
		}

		return result;
	}

	public static ArrayList<String> subDirectoryFolderGet(String baseDirectory) throws StorageException {
		List<Path> pathList = subDirectoryPathGet(baseDirectory);

		ArrayList<String> result = new ArrayList<>();

		for (Path path : pathList) {
			String folder = path.toFile()
					.getName();
			result.add(folder);
		}

		return result;
	}

	/**
	 * Gets the directory from the given path.
	 * 
	 * @param path
	 * Path to check. Can be absolute or relative.
	 * 
	 * @return The normalized directory.
	 * 
	 * @throws StorageException
	 * thrown if path is just a filename without a parent.
	 */
	public static String getFromPath(String path) throws StorageException {
		if (isExist(path)) {
			return path;
		}

		// not a directory
		Path filePath = Paths.get(path);

		Path fileParentPath = filePath.getParent();
		if (fileParentPath == null) {
			throw new StorageException("Invalid parent for path \"" + path + "\".");
		}

		return fileParentPath.normalize()
				.toString();
	}

	public static void folderRename(String sourceDirectory, String destinationFolderName) throws StorageException {
		if (!isExist(sourceDirectory)) {
			throw new StorageException("Directory \"" + sourceDirectory + "\" doesn't exist.");
		}

		Path sourcePath = Paths.get(sourceDirectory);

		try {
			Files.move(sourcePath, sourcePath.resolveSibling(destinationFolderName));
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
	}

	/**
	 * Assumes directory is empty.
	 */
	public static void delete(String directory) throws StorageException {
		if (!isExist(directory)) {
			throw new StorageException("Directory \"" + directory + "\" doesn't exist.");
		}
		
		Path path = Paths.get(directory);

		try {
			Files.delete(path);
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
	}
}
