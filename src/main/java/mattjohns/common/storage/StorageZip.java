package mattjohns.common.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Zip file utilities.
 */
public class StorageZip {
	/**
	 * Extracts the zip file stream into the destination directory.
	 * 
	 * @param sourceStream
	 * Normally a zip file embedded in the program, loaded as a stream.
	 * 
	 * @param destinationDirectory
	 * The root directory to extract into. Creates destination folder if
	 * needed but does not automatically create parent directories. Child
	 * directories are made according to the zip file structure.
	 * 
	 * @param isOverwrite
	 * Ovewrites files if they already exist. Folders are always created if they
	 * don't exist.
	 * 
	 * @throws StorageException
	 */
	public static void extract(InputStream sourceStream, String destinationDirectory, boolean isOverwrite)
			throws StorageException {
		// root folder
		StorageDirectory.createIfNotExist(destinationDirectory);

		ZipInputStream zipStream = null;
		try {
			zipStream = new ZipInputStream(sourceStream);
			ZipEntry entry = null;

			// get each file from the zip and copy it to the corresponding destination directory
			while ((entry = zipStream.getNextEntry()) != null) {
				String sourcePath = entry.getName();

				String destinationPath = StoragePath.combine(destinationDirectory, sourcePath);

				// Might get a source file name before getting the directory
				// that it's in, so need to check that each file has its
				// destination directory already created.
				String currentDestinationDirectory = StorageDirectory.getFromPath(destinationPath);
				if (!StorageDirectory.isExist(currentDestinationDirectory)) {
					// destination directory didn't exist, create it and any
					// parent directories
					StorageDirectory.createWithParents(currentDestinationDirectory);
				}

				// copy the file
				if (!entry.isDirectory()) {
					// ignore source directories, they are handled above

					// only overwrite if required
					if (isOverwrite || !StorageFile.isExist(destinationPath)) {
						StorageStream.copyToFile(zipStream, destinationPath, true);
					}
				}
			}
		}
		catch (IOException exception) {
			throw new StorageException(exception.getMessage());
		}
		finally {
			StorageStream.forceClose(zipStream);
		}
	}
}
