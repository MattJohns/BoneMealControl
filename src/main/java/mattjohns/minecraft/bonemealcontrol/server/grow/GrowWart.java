package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;

public class GrowWart {
	protected static final int AgeMaximum = 3;

	protected CommonConfiguration configuration;

	public GrowWart(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, BlockPos position) {
		assert world.getBlockState(position).getBlock().equals(Blocks.NETHER_WART);

		IBlockState blockState = world.getBlockState(position);

		int age = ((Integer) blockState.getValue(BlockNetherWart.AGE)).intValue();
		if (age >= AgeMaximum) {
			// fully grown
			return false;
		}

		if (!net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, position, blockState, true)) {
			// grow blocked by forge
			return false;
		}

		int ageIncrement = ageIncrementDerive();

		int ageNew = age + ageIncrement;
		if (ageNew > AgeMaximum) {
			ageNew = AgeMaximum;
		}

		IBlockState blockStateNew = blockState.withProperty(BlockNetherWart.AGE, Integer.valueOf(ageNew));

		world.setBlockState(position, blockStateNew, 2);

		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, position, blockState, blockStateNew);

		return true;
	}

	private int ageIncrementDerive() {
		int fixed = configuration.elementGet(CommonConfiguration.ElementWartAgeIncrement);
		int randomMaximum = configuration.elementGet(CommonConfiguration.ElementWartAgeRandomIncrement);

		int randomAmount = General.randomGetIntegerExclusive(randomMaximum + 1);

		int result = fixed + randomAmount;
		if (result > AgeMaximum) {
			result = AgeMaximum;
		}

		return result;
	}
}
