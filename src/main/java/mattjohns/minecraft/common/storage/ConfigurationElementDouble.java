package mattjohns.minecraft.common.storage;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigurationElementDouble extends ConfigurationElementNumber<Double> {
	protected ConfigurationElementDouble(String category, String key, Double defaultValue, Double minimum,
			Double maximum, String description) {
		super(category, key, defaultValue, minimum, maximum, description);
	}

	public static ConfigurationElementDouble of(String category, String key, Double defaultValue, Double minimum, Double maximum, String description) {
		return new ConfigurationElementDouble(category, key, defaultValue, minimum, maximum, description);
	}
	
	@Override
	protected void valueSetDirect(Property property, Double valueUnchecked) {
		property.setValue(valueUnchecked);
	}

	@Override
	protected Property propertyGetOrCreate(Configuration forgeConfiguration) {
		return forgeConfiguration.get(category, key, defaultValue, descriptionFull());
	}

	@Override
	protected Double valueGet(Property property) {
		return property.getDouble();
	}
}
