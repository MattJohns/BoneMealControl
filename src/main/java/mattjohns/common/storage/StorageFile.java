package mattjohns.common.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import mattjohns.common.text.TextUtility;

/**
 * File utilities. Windows specific in some cases.
 * <p>
 * See also StorageDirectory, StoragePath and StorageStream.
 * 
 */
public class StorageFile {
	/**
	 * Creates a file at the given path.
	 * 
	 * @return The path to the new file.
	 * 
	 * @throws StorageException
	 * Thrown if the file already exists.
	 */
	public static String create(String file) throws StorageException {
		Path filePath = Paths.get(file);

		try {
			Path destinationPath = Files.createFile(filePath);
			return destinationPath.normalize()
					.toString();
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
	}

	/**
	 * Checks if the given file exists on the file system.
	 */
	public static boolean isExist(String file) {
		Path filePath = Paths.get(file);

		return Files.exists(filePath);
	}

	/**
	 * Creates the given file only if it doesn't exist.
	 * 
	 * @return The name of the new or the existing file.
	 * 
	 * @throws StorageException
	 */
	public static String createIfNotExist(String file) throws StorageException {
		Path filePath = Paths.get(file);

		if (StoragePath.isExist(filePath))
			return filePath.normalize()
					.toString();

		return create(filePath.toString());
	}

	/**
	 * Creates a file in the given directory, only if it doesn't exist.
	 * 
	 * @param directory
	 * The directory to create the file in.
	 * 
	 * @param fileName
	 * Must be just a file name without any directories.
	 * 
	 * @return The path of the new or existing file.
	 * 
	 * @throws StorageException
	 */
	public static String createIfNotExist(String directory, String fileName) throws StorageException {
		Path filePath = Paths.get(directory, fileName);

		return createIfNotExist(filePath.toString());
	}

	/**
	 * Copies a file to a directory.
	 * 
	 * @param sourceFile
	 * The path to the file that will be copied.
	 * 
	 * @param destinationDirectory
	 * The directory to copy into.
	 * 
	 * @param isOverwrite
	 * Overwrites if the file already exists.
	 * 
	 * @throws StorageException
	 */
	public static void copyToDirectory(String sourceFile, String destinationDirectory, boolean isOverwrite)
			throws StorageException {
		Path sourceFilePath = Paths.get(sourceFile);
		Path destinationDirectoryPath = Paths.get(destinationDirectory);

		Path sourceFileNamePath = sourceFilePath.getFileName();
		Path destinationFilePath = destinationDirectoryPath.resolve(sourceFileNamePath);
		String destinationFile = destinationFilePath.normalize()
				.toString();

		try {
			if (isOverwrite) {
				Files.copy(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				if (!isExist(destinationFile)) {
					Files.copy(sourceFilePath, destinationFilePath);
				}
			}
		}
		catch (IOException exception) {
			throw new StorageException("Unable to copy file \"" + sourceFile + "\" to directory \""
					+ destinationFilePath.toString() + "\" .");
		}
	}

	/**
	 * Get the extension of the given file.
	 * 
	 * @param file
	 * The path to the file.
	 * 
	 * @return null if there is no extension. Empty string if there is a dot on
	 * the end. Otherwise the extension which is treated as everything to the
	 * right of the last dot.
	 */
	public static String extensionGet(String file) {
		String extension = "";

		int i = file.lastIndexOf('.');
		if (i >= 0) {
			extension = file.substring(i + 1);
		}

		return extension;
	}

	/**
	 * Get just the file name for the given file path.
	 * 
	 * @param file
	 * The path to the file.
	 * 
	 * @return The file name with extension.
	 */
	public static String nameGet(String file) {
		Path filePath = Paths.get(file);

		return filePath.getFileName()
				.toString();
	}

	/**
	 * Get just the file name without the extension.
	 * 
	 * @param file
	 * The file path to get the file name of. It is ok if there is no extension.
	 * 
	 * @return The file name without extension and without any trailing '.'
	 * character.
	 */
	public static String nameGetWithoutExtension(String path) {
		String fileName = nameGet(path);

		int extensionIndex = fileName.lastIndexOf(".");
		if (extensionIndex > 0) {
			fileName = fileName.substring(0, extensionIndex);
		}

		return fileName;
	}

	/**
	 * Returns false if it was already deleted.
	 */
	public static boolean delete(String path) throws StorageException {
		if (!isExist(path)) {
			return false;
		}

		try {
			Files.delete(Paths.get(path));
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}

		return true;
	}

	public static void createText(String path, String content) throws StorageException {
		if (isExist(path)) {
			throw new StorageException("File \"" + path + "\" already exists.");
		}

		OpenOption[] options = { StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE };

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8, options)) {
			writer.write(content, 0, content.length());
		}
		catch (IOException x) {
			throw new StorageException(x);
		}
	}

	public static String getText(String path) throws StorageException {
		if (!isExist(path)) {
			throw new StorageException("File \"" + path + "\" doesn't exists.");
		}

		StringBuilder builder = new StringBuilder();
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
			String line;

			while ((line = reader.readLine()) != null) {
				builder.append(line + TextUtility.CARRIAGE_RETURN);
			}
		}
		catch (IOException exception) {
			throw new StorageException(exception);
		}
		
		return builder.toString();
	}
}
