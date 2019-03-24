package mattjohns.minecraft.common.storage;

import mattjohns.common.storage.StorageException;

public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
	}
	
	public ConfigurationException(String text) {
		super(text);
	}

	public ConfigurationException(StorageException exception) {
		this(exception.getMessage());
	}
}
