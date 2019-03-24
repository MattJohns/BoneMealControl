package mattjohns.common.storage;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class StorageJson {
	public static <TConfiguration> TConfiguration copyFromFile(String path, Class<TConfiguration> classType)
			throws StorageException {

		if (!StoragePath.isExist(path)) {
			throw new StorageException("JSON configuration file \"" + path + "\" not found.");
		}

		String fileText = StorageFile.getText(path);

		try {
			return copyFromText(fileText, classType);
		} catch (StorageException e) {
			throw new StorageException(e.getMessage() + "  File path: \"" + path + "\".");
		}
	}

	public static <TConfiguration> TConfiguration copyFromText(String text, Class<TConfiguration> classType)
			throws StorageException {

		Gson gson = new Gson();

		try {
			return gson.fromJson(text, classType);
		} catch (JsonSyntaxException e) {
			throw new StorageException("Error in JSON file: " + e.getMessage());
		}
	}
}
