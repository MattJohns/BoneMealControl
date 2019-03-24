package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;

// melon and pumpkin blocks, both have same growth type
public class GrowStemBlock {
	protected static final int AgeSize = 8;

	protected CommonConfiguration configuration;

	public GrowStemBlock(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	// need to check if mature so can decide whether to do vanilla bonemeal
	// or custom, block-producing bonemeal
	public boolean stemCheckMature(World world, BlockPos position) {
		IBlockState blockState = world.getBlockState(position);

		int age = ((Integer) blockState.getValue(BlockStem.AGE)).intValue();
		return age >= (AgeSize - 1);
	}

	// Forces stem to produce fruit block. Immature stem should be grown by
	// caller
	// using vanilla code.
	public boolean grow(World world, BlockPos targetPosition) {
		IBlockState targetBlockState = world.getBlockState(targetPosition);

		assert targetBlockState.getBlock() instanceof BlockStem;

		BlockStem targetBlock = (BlockStem) targetBlockState.getBlock();

		if (!stemCheckMature(world, targetPosition)) {
			return false;
		}

		if (!net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, targetPosition, targetBlockState, true)) {
			// grow blocked by forge
			return false;
		}

		// check if a fruit already exists for this stem
		Block fruitBlock;
		if (targetBlock.equals(Blocks.MELON_STEM)) {
			fruitBlock = Blocks.MELON_BLOCK;
		} else {
			fruitBlock = Blocks.PUMPKIN;
		}

		for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
			if (world.getBlockState(targetPosition.offset(enumfacing)).getBlock() == fruitBlock) {
				// already a fruit
				return false;
			}
		}

		// choose a random position to grow the fruit
		BlockPos fruitPosition = targetPosition.offset(EnumFacing.Plane.HORIZONTAL.random(world.rand));
		IBlockState fruitSoilBlockState = world.getBlockState(fruitPosition.down());
		Block fruitSoilBlock = fruitSoilBlockState.getBlock();

		// ensure nothing blocking it
		if (world.isAirBlock(fruitPosition)) {
			// check media
			if (fruitSoilBlock.canSustainPlant(fruitSoilBlockState, world, fruitPosition.down(), EnumFacing.UP,
					targetBlock) || fruitSoilBlock == Blocks.DIRT || fruitSoilBlock == Blocks.GRASS) {

				// chance
				double chance = configuration.elementGet(CommonConfiguration.ElementMelonBlockChance);

				if (General.randomChance(chance)) {
					// place it
					world.setBlockState(fruitPosition, fruitBlock.getDefaultState());
				}
			}
		}

		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, fruitPosition, targetBlockState,
				world.getBlockState(fruitPosition));

		// use up bonemeal even if placement failed
		return true;
	}
}
