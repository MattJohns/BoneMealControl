package mattjohns.minecraft.common.storage;

import net.minecraftforge.common.config.Property;

public abstract class ConfigurationElementNumber<T extends Comparable<T>> extends ConfigurationElement<T> {
	protected T minimum;
	protected T maximum;
	
	protected ConfigurationElementNumber(String category, String key, T defaultValue, T minimum, T maximum, String description) {
		super(category, key, defaultValue, description);

		this.minimum = minimum;
		this.maximum = maximum;
	}

	@Override
	protected void valueSet(Property property, T value) {
		T valueValid = value;
		
		if (value.compareTo(minimum) < 0) {
			valueValid = minimum;
		}
		
		if (value.compareTo(maximum) > 0) {
			valueValid = maximum;
		}
		
		valueSetDirect(property, valueValid);
	}
	
	protected abstract void valueSetDirect(Property property, T valueUnchecked);
	
	public T minimumGet() {
		return minimum;
	}
	
	public T maximumGet() {
		return maximum;
	}
	
	@Override
	protected String additionalText() {
		String result = super.additionalText();
		
		result += "\nMinimum: " + minimum.toString() + ", Maximum: " + maximum.toString() + ".";
		
		return result;
	}
}
