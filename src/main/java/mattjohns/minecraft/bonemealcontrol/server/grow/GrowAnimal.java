package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;

public class GrowAnimal {
	protected CommonConfiguration configuration;

	public GrowAnimal(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, EntityAnimal animal) {
		if (!animal.isChild()) {
			// already an adult
			return false;
		}

		int ageIncrement = ageIncrementDerive();

		animal.addGrowth(ageIncrement);

		return true;
	}

	private int ageIncrementDerive() {
		int fixed = configuration.elementGet(CommonConfiguration.ElementAnimalAgeIncrement);
		int randomMaximum = configuration.elementGet(CommonConfiguration.ElementAnimalAgeRandomIncrement);

		int randomAmount = General.randomGetIntegerExclusive(randomMaximum + 1);

		int incrementMaximum = CommonConfiguration.ElementAnimalAgeIncrement.maximumGet();

		int result = fixed + randomAmount;
		if (result > incrementMaximum) {
			result = incrementMaximum;
		}

		return result;
	}
}
