package mattjohns.minecraft.bonemealcontrol.server.grow;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Chorus flower doesn't seem to do anything when multiple update ticks 
// are performed in a row.  So the user only has the option of 'chance' for this one.
public class GrowChorusFlower {
	protected CommonConfiguration configuration;

	public GrowChorusFlower(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, BlockPos position) {
		IBlockState blockState = world.getBlockState(position);

		assert blockState.getBlock().equals(Blocks.CHORUS_FLOWER);

		double chance = configuration.elementGet(CommonConfiguration.ElementChorusFlowerUpdateChance);

		if (General.randomChance(chance)) {
			blockState.getBlock().updateTick(world, position, blockState, world.rand);
		} else {
			// chance didn't happen but return true to ensure the bonemeal is
			// used up anyway
		}

		return true;
	}
}
