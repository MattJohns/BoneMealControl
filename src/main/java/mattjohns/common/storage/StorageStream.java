package mattjohns.common.storage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Stream utilities.
 */
public class StorageStream {
	/**
	 * Copy the content of one stream to another.
	 * 
	 * @param source
	 * The stream to copy from.
	 * 
	 * @param destination
	 * The stream to copy to.
	 * 
	 * @throws StorageException
	 */
	public static void copy(InputStream source, OutputStream destination) throws StorageException {
		byte[] buffer = new byte[1024 * 4];
		int length;

		try {
			while ((length = source.read(buffer)) >= 0) {
				destination.write(buffer, 0, length);
			}
		}
		catch (IOException exception) {
			throw new StorageException("Unable to copy stream.", exception);
		}
	}

	/**
	 * Creates the destination file.
	 * 
	 * @param source
	 * The stream to copy from.
	 * 
	 * @param destinationFile
	 * The path of the file to copy to. If it doesn't exist it will be created.
	 * 
	 * @param isOverwrite
	 * Overwrite file if it already exists.
	 */
	public static void copyToFile(InputStream source, String destinationFile, boolean isOverwrite)
			throws StorageException {
		try {
			Path destinationFilePath = Paths.get(destinationFile);

			if (isOverwrite) {
				Files.copy(source, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				if (!StorageFile.isExist(destinationFile)) {
					Files.copy(source, destinationFilePath);
				}
			}
		}
		catch (IOException exception) {
			throw new StorageException("Unable to copy stream to file \"" + destinationFile + "\" .", exception);
		}
	}

	/**
	 * Creates a file on the file system if it doesn't already exist.  Opens it
	 * in output mode.
	 * 
	 * @param file
	 * The path of the file to open.
	 * 
	 * @return
	 * An output stream for the file.
	 * 
	 * @throws StorageException
	 */ 
	public static OutputStream createOutput(String file) throws StorageException {
		StorageFile.createIfNotExist(file);

		try {
			return new FileOutputStream(file);
		}
		catch (FileNotFoundException exception) {
			throw new StorageException(
					"Unable to create stream.  File could not be created: " + exception.getMessage());
		}
	}

	/**
	 * Force a stream to close and ignore any error.
	 */
	public static void forceClose(InputStream stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		}
		catch (IOException exception) {
			// ignore
		}
	}
}
