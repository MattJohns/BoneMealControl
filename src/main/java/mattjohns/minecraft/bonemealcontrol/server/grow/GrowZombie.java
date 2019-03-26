package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;

public class GrowZombie {
	protected CommonConfiguration configuration;

	public GrowZombie(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, EntityZombie zombie) {
		if (!zombie.isChild()) {
			// already an adult
			return false;
		}

		double chance = configuration.elementGet(CommonConfiguration.ElementZombieAdultChance);

		if (General.randomChance(chance)) {
			zombie.setChild(false);
		}

		return true;
	}
}
