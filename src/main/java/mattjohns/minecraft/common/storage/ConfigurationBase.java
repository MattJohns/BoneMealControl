package mattjohns.minecraft.common.storage;

import java.io.File;
import java.util.Optional;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import mattjohns.common.storage.StorageDirectory;
import mattjohns.common.storage.StorageException;
import mattjohns.common.storage.StoragePath;
import mattjohns.minecraft.common.log.Log;

public abstract class ConfigurationBase {
	public static final String CategoryGeneral = Configuration.CATEGORY_GENERAL;
	public static final String CategoryCommon = "common";
	public static final String CategoryServer = "server";
	public static final String CategoryClient = Configuration.CATEGORY_CLIENT;

	protected Configuration forgeConfiguration;
	protected String filename;
	protected Optional<String> subFolder;
	protected ConfigurationElementList elementList;
	
	protected Log log;

	protected ConfigurationBase(String filename, Optional<String> subFolder, Log log) {
		this.filename = filename;
		this.subFolder = subFolder;
		this.log = log;

		elementList = elementList();
	}

	protected abstract ConfigurationElementList elementList();

	public String directory() {
		String baseDirectory = Loader.instance().getConfigDir().getPath();

		if (subFolder.isPresent()) {
			return StoragePath.combine(baseDirectory, subFolder.get());
		} else {
			return baseDirectory;
		}
	}

	public void copyFromStorage() {
		forgeConfiguration = forgeCopyFromStorage();
		
		validateAndFix();
		
		copyFromStoragePost();

		// re-save it as it might have changed during validation etc..
		copyToStorage();
	}

	// anything that needs to be done after loading (before re-save)
	protected void copyFromStoragePost() {
	}

	protected Configuration forgeCopyFromStorage() {
		String directory = directory();

		if (!StorageDirectory.isExist(directory)) {
			try {
				// directory doesn't exist
				StorageDirectory.create(directory);
			} catch (StorageException exception) {
				// failed, log and continue
				log.error("Failed to load configuration.  " + exception.getMessage());
			}
		}

		File configurationFile = new File(directory, filename);
		if (configurationFile == null || !configurationFile.exists()) {
			// configuration file doesn't exist, it will be created below
		}

		Configuration newForgeConfiguration = new Configuration(configurationFile);

		newForgeConfiguration.load();

		for (ConfigurationElement<?> element : elementList) {
			element.propertyGetOrCreate(newForgeConfiguration);
		}

		return newForgeConfiguration;
	}

	// fix any bad values
	protected void validateAndFix() {
	}

	public void copyToStorage() {
		for (ConfigurationElement<?> element : elementList) {
			element.propertyGetOrCreate(forgeConfiguration);
		}

		// set order
		sortOrderSet();

		if (forgeConfiguration.hasChanged()) {
			forgeConfiguration.save();
		}
	}

	public <T> T elementGet(ConfigurationElement<T> element) {
		return element.valueGet(forgeConfiguration);
	}

	public <T> void elementSet(ConfigurationElement<T> element, T value) {
		element.valueSet(value, forgeConfiguration);
	}

	protected void sortOrderSet() {
	}
}
