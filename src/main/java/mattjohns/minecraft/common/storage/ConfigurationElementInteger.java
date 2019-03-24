package mattjohns.minecraft.common.storage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigurationElementInteger extends ConfigurationElementNumber<Integer> {
	protected ConfigurationElementInteger(String category, String key, Integer defaultValue, Integer minimum,
			Integer maximum, String description) {
		super(category, key, defaultValue, minimum, maximum, description);
	}

	public static ConfigurationElementInteger of(String category, String key, Integer defaultValue, Integer minimum, Integer maximum, String description) {
		return new ConfigurationElementInteger(category, key, defaultValue, minimum, maximum, description);
	}
	
	@Override
	protected void valueSetDirect(Property property, Integer valueUnchecked) {
		property.setValue(valueUnchecked);
	}

	@Override
	protected Property propertyGetOrCreate(Configuration forgeConfiguration) {
		return forgeConfiguration.get(category, key, defaultValue, descriptionFull());
	}

	@Override
	protected Integer valueGet(Property property) {
		return property.getInt();
	}
}