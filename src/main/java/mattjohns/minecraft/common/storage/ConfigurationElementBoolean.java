package mattjohns.minecraft.common.storage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigurationElementBoolean extends ConfigurationElement<Boolean> {
	protected ConfigurationElementBoolean(String category, String key, Boolean defaultValue, String description) {
		super(category, key, defaultValue, description);
	}
	
	public static ConfigurationElementBoolean of(String category, String key, Boolean defaultValue, String description) {
		return new ConfigurationElementBoolean(category, key, defaultValue, description);
	}

	@Override
	protected Property propertyGetOrCreate(Configuration forgeConfiguration) {
		return forgeConfiguration.get(category, key, defaultValue, descriptionFull());
	}

	@Override
	protected Boolean valueGet(Property property) {
		return property.getBoolean();
	}

	@Override
	protected void valueSet(Property property, Boolean value) {
		property.setValue(value);
	}
}
