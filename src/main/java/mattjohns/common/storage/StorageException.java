package mattjohns.common.storage;

import java.io.IOException;

public class StorageException extends Exception {
	private static final long serialVersionUID = 1L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public StorageException(String message, IOException ioException) {
    	super(message + "  IO error: \"" + ioException.getMessage() + "\".");
    }

    public StorageException(IOException ioException) {
    	super("Storage exception.  IO error: \"" + ioException.getMessage() + "\".");
    }
}