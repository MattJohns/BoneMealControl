package mattjohns.minecraft.bonemealcontrol.server.grow;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrowVine {
	protected CommonConfiguration configuration;

	public GrowVine(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, BlockPos position) {
		IBlockState blockState = world.getBlockState(position);

		assert blockState.getBlock().equals(Blocks.VINE);

		int updateSize = updateIncrementDerive();

		// update the vine multiple times to let it grow
		for (int i = 0; i < updateSize; i++) {
			blockState = world.getBlockState(position);

			blockState.getBlock().updateTick(world, position, blockState, world.rand);
		}

		return true;
	}

	private int updateIncrementDerive() {
		int fixed = configuration.elementGet(CommonConfiguration.ElementVineUpdateIncrement);
		int randomMaximum = configuration.elementGet(CommonConfiguration.ElementVineUpdateRandomIncrement);

		int randomAmount = General.randomGetIntegerExclusive(randomMaximum + 1);

		int incrementMaximum = CommonConfiguration.ElementVineUpdateIncrement.maximumGet();

		int result = fixed + randomAmount;
		if (result > incrementMaximum) {
			result = incrementMaximum;
		}

		return result;
	}
}
