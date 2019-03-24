package mattjohns.common.storage;

import java.io.InputStream;

/**
 * Java resource utilities.
 */
public class StorageResource {
	/**
	 * Copy a program resource to the given path.
	 * 
	 * @param resourcePath
	 * The resource location in the form of 'assets/images/picture.png' .
	 * 
	 * @param destinationFilePath
	 * The file system path to copy the resource to.
	 * 
	 * @throws StorageException
	 */
	public static void copyToFile(String resourcePath, String destinationFile, boolean isOverwrite) throws StorageException {
		InputStream sourceStream = inputStreamGet(resourcePath);
		
		StorageStream.copyToFile(sourceStream, destinationFile, isOverwrite);
	}

	/**
	 * Gets an input stream for the given resource path.
	 * 
	 * @param resourcePath
	 * The resource location in the form of 'assets/images/picture.png' .

	 * @return
	 * @throws StorageException
	 */
	public static InputStream inputStreamGet(String resourcePath) throws StorageException {
		InputStream returnValue = StorageResource.class.getClassLoader().getResourceAsStream(resourcePath);
		if (returnValue == null)
			throw new StorageException("Unable to get input stream from path: \"" + resourcePath.toString() + "\".");

		return returnValue;
	}
	
	/**
	 * Gets an input stream for the given resource directory and file name.
	 * 
	 * @param resourceDirectory
	 * The resource directory in the form of 'assets/images'  .  Trailing or
	 * non-trailing slash is ok.

	 * @return
	 * The stream to the path.
	 * 
	 * @throws StorageException
	 * Throws if unable to open.
	 */
	public static InputStream inputStreamGet(String resourceDirectory, String filename) throws StorageException {
		String resourcePath = StoragePath.combine(resourceDirectory, filename);
	
		return inputStreamGet(resourcePath);
	}
}
