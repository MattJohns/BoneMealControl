package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;

public class GrowJungle {
	protected CommonConfiguration configuration;

	public GrowJungle(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean growLog(World world, BlockPos position) {
		// don't use bone meal if every single side is blocked
		boolean isOpenSide = false;
		for (EnumFacing sideTest : EnumFacing.HORIZONTALS) {
			IBlockState blockState = world.getBlockState(position.offset(sideTest));
			if (blockState.getBlock() == Blocks.AIR) {
				isOpenSide = true;
				break;
			}
		}
		
		if (!isOpenSide) {
			return false;
		}
		
		// pick a random side of the log
		int sideIndex = General.randomGetIntegerExclusive(4);
		EnumFacing side = EnumFacing.getHorizontal(sideIndex);

		BlockPos sidePosition = position.offset(side);
		
		// check if air
		if (world.getBlockState(sidePosition).getBlock() != Blocks.AIR) {
			// no, use up bone meal
			return true;
		}

		// chance
		double chance = configuration.elementGet(CommonConfiguration.ElementJungleLogChance);
		if (!General.randomChance(chance)) {
			// chance failed but use up bone meal
			return true;
		}

		IBlockState cocoaNew = Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, side.getOpposite());
				
		world.setBlockState(sidePosition, cocoaNew);
		
		return true;
	}
	
	public boolean growLeaf(World world, BlockPos position) {
		// don't use bone meal if every single side is blocked
		boolean isOpenSide = false;
		for (EnumFacing sideTest : EnumFacing.VALUES) {
			if (sideTest != EnumFacing.UP) {
				IBlockState blockState = world.getBlockState(position.offset(sideTest));
				if (blockState.getBlock() == Blocks.AIR) {
					isOpenSide = true;
					break;
				}
			}
		}
		
		if (!isOpenSide) {
			return false;
		}
				
		// pick a random side of the leaf
		int sideIndex = General.randomGetIntegerExclusive(5);
		
		EnumFacing side;
		if (sideIndex == 4) {
			side = EnumFacing.DOWN;
		}
		else {
			side = EnumFacing.getHorizontal(sideIndex);
		}

		BlockPos sidePosition = position.offset(side);
		
		// check if air
		if (world.getBlockState(sidePosition).getBlock() != Blocks.AIR) {
			// no, use up bone meal
			return true;
		}

		// chance
		double chance = configuration.elementGet(CommonConfiguration.ElementJungleLeafChance);
		if (!General.randomChance(chance)) {
			// chance failed but use up bone meal
			return true;
		}

		IBlockState vineNew = Blocks.VINE.getDefaultState(); 
		switch (side) {
		case NORTH: {
			vineNew = vineNew.withProperty(BlockVine.SOUTH, true);
			break;
		}
		case SOUTH: {
			vineNew = vineNew.withProperty(BlockVine.NORTH, true);
			break;
		}
		case EAST: {
			vineNew = vineNew.withProperty(BlockVine.WEST, true);
			break;
		}
		case WEST: {
			vineNew = vineNew.withProperty(BlockVine.EAST, true);
			break;
		}
		case DOWN: {
			vineNew = vineNew.withProperty(BlockVine.UP, true);
			break;
		}
		default: {
			// should never happen
			return false;
		}
		}
				
		world.setBlockState(sidePosition, vineNew);
		
		return true;
	}
}
