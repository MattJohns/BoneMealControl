package mattjohns.common.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class StorageImage {
	public static BufferedImage copyFromPath(String filePath) throws StorageException {
		return copyFromFile(new File(filePath));
	}

	public static BufferedImage copyFromFile(File file) throws StorageException {
		try {
			return ImageIO.read(file);
		}
		catch (IOException exception) {
			throw new StorageException("Unable to copy image from file \"" + file.toString() + "\".", exception);
		}
	}

	public static BufferedImage copyFromFile(URL item) throws StorageException {
		try {
			return ImageIO.read(item);
		}
		catch (IOException exception) {
			throw new StorageException("Unable to copy image from URL \"" + item.toString() + "\".", exception);
		}
	}

	public static BufferedImage copyFromStream(InputStream item) throws StorageException {
		try {
			return ImageIO.read(item);
		}
		catch (IOException exception) {
			throw new StorageException("Unable to copy image from stream \"" + item.toString() + "\".", exception);
		}
	}
}
