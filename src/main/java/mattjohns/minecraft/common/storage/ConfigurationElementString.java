package mattjohns.minecraft.common.storage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigurationElementString extends ConfigurationElement<String> {
	protected ConfigurationElementString(String category, String key, String defaultValue, String description) {
		super(category, key, defaultValue, description);
	}
	
	public static ConfigurationElementString of(String category, String key, String defaultValue, String description) {
		return new ConfigurationElementString(category, key, defaultValue, description);
	}

	@Override
	protected Property propertyGetOrCreate(Configuration forgeConfiguration) {
		return forgeConfiguration.get(category, key, defaultValue, descriptionFull());
	}

	@Override
	protected String valueGet(Property property) {
		return property.getString();
	}

	@Override
	protected void valueSet(Property property, String value) {
		property.setValue(value);
	}
	
	@Override
	protected String defaultValueText() {
		return super.defaultValueText();
	}
}
