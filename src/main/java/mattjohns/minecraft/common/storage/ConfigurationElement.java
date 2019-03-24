package mattjohns.minecraft.common.storage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public abstract class ConfigurationElement<T> {
	protected String category;
	protected String key;
	protected T defaultValue;
	protected String descriptionPartial;

	protected ConfigurationElement(String category, String key, T defaultValue, String description) {
		this.category = category;
		this.key = key;
		this.defaultValue = defaultValue;

		this.descriptionPartial = description;
	}

	public String category() {
		return category;
	}

	public String key() {
		return key;
	}

	public T defaultValue() {
		return defaultValue;
	}

	protected String descriptionPartial() {
		return descriptionPartial;
	}
	
	public String descriptionFull() {
		 return descriptionPartial() + additionalText();		
	}

	protected abstract Property propertyGetOrCreate(Configuration forgeConfiguration);

	protected abstract T valueGet(Property property);

	protected abstract void valueSet(Property property, T value);

	public T valueGet(Configuration forgeConfiguration) {
		return valueGet(propertyGetOrCreate(forgeConfiguration));
	}

	public void valueSet(T value, Configuration forgeConfiguration) {
		valueSet(propertyGetOrCreate(forgeConfiguration), value);
	}

	protected String additionalText() {
		return "\nDefault is " + defaultValueText() + ".";
	}

	protected String defaultValueText() {
		return valueSerialize(defaultValue);
	}

	protected String valueSerialize(T item) {
		return item.toString();
	}
}
