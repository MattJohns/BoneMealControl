package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.init.Blocks;

import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.common.storage.ConfigurationElementInteger;

// sugar cane
public class GrowReed extends GrowTower {
	public GrowReed(CommonConfiguration configuration) {
		super(configuration);
	}

	@Override
	protected ConfigurationElementInteger configurationElementAgeIncrement() {
		return CommonConfiguration.ElementReedAgeIncrement;
	}

	@Override
	protected ConfigurationElementInteger configurationElementAgeRandomIncrement() {
		return CommonConfiguration.ElementReedAgeRandomIncrement;
	}

	@Override
	protected boolean mediaCheck(Block block) {
		return (block.equals(Blocks.GRASS) || block.equals(Blocks.DIRT) || block.equals(Blocks.SAND));
	}

	@Override
	protected Block plantBlock() {
		return Blocks.REEDS;
	}

	@Override
	protected PropertyInteger ageProperty() {
		return BlockReed.AGE;
	}
}
