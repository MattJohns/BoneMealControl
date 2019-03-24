package mattjohns.minecraft.bonemealcontrol.server.grow;

import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.common.storage.ConfigurationElementInteger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.init.Blocks;

public class GrowCactus extends GrowTower {
	public GrowCactus(CommonConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected ConfigurationElementInteger configurationElementAgeIncrement() {
		return CommonConfiguration.ElementCactusAgeIncrement;
	}

	@Override
	protected ConfigurationElementInteger configurationElementAgeRandomIncrement() {
		return CommonConfiguration.ElementCactusAgeRandomIncrement;
	}

	@Override
	protected boolean mediaCheck(Block block) {
		return block.equals(Blocks.SAND);
	}

	@Override
	protected Block plantBlock() {
		return Blocks.CACTUS;
	}

	@Override
	protected PropertyInteger ageProperty() {
		return BlockCactus.AGE;
	}
}
