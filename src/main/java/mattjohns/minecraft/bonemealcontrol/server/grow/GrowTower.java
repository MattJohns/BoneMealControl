package mattjohns.minecraft.bonemealcontrol.server.grow;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.common.storage.ConfigurationElementInteger;

// Common code for cactus and reed because they grow the same way. 
public abstract class GrowTower {
	protected static final int AgeSize = 16;
	protected static final int SizeYMaximum = 3;

	protected CommonConfiguration configuration;

	public GrowTower(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	// false if plant is malformed or already full height
	public boolean grow(World world, BlockPos targetPosition) {
		// base position
		Optional<BlockPos> basePosition = getBasePosition(world, targetPosition);
		if (!basePosition.isPresent()) {
			// invalid media, ignore bonemeal request
			return false;
		}

		// height
		int sizeY = sizeYGet(world, basePosition.get());

		// ensure air exists above it
		BlockPos airTestPosition = basePosition.get().add(0, sizeY, 0);
		Block airTestBlock = world.getBlockState(airTestPosition).getBlock();
		if (!airTestBlock.equals(Blocks.AIR)) {
			// blocked
			return false;
		}

		// grow it
		return grow(world, basePosition.get(), sizeY);
	}

	// doesn't check anything
	// returns false if already full height
	private boolean grow(World world, BlockPos basePosition, int sizeY) {
		assert sizeY >= 1;
		assert sizeY <= SizeYMaximum;

		if (sizeY >= SizeYMaximum) {
			// already grown
			return false;
		}

		BlockPos airPosition = basePosition.add(0, sizeY, 0);
		IBlockState airBlockState = world.getBlockState(airPosition);

		if (!net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, basePosition, airBlockState, true)) {
			// grow blocked by forge
			return false;
		}

		// top block is the highest plant block in the tower
		BlockPos topPosition = basePosition.add(0, sizeY - 1, 0);
		IBlockState topBlockState = world.getBlockState(topPosition);
		Block topBlock = topBlockState.getBlock();

		int topAge = ((Integer) topBlockState.getValue(ageProperty())).intValue();
		int topAgeIncrement = ageIncrementDerive();
		int topNewAge = topAge + topAgeIncrement;

		if (topNewAge >= AgeSize) {
			// top is mature
			int ageLeftover = topNewAge - AgeSize;
			IBlockState airNewBlockState = topBlock.getDefaultState().withProperty(ageProperty(),
					Integer.valueOf(ageLeftover));
			world.setBlockState(airPosition, airNewBlockState);

			IBlockState topNewBlockState = topBlockState.withProperty(ageProperty(), Integer.valueOf(0));
			world.setBlockState(topPosition, topNewBlockState, 4);

			topNewBlockState.neighborChanged(world, airPosition, topBlock, topPosition);
		} else {
			// just increment top age
			IBlockState topNewBlockState = topBlockState.withProperty(ageProperty(), Integer.valueOf(topNewAge));
			world.setBlockState(topPosition, topNewBlockState, 4);

			// need to update block state for top block because it doesn't
			// happen automatically if they are aiming the
			// bonemeal at some other part of the plant
			world.notifyBlockUpdate(topPosition, topBlockState, topNewBlockState, 4);
		}

		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, topPosition, topBlockState,
				world.getBlockState(topPosition));

		return true;
	}

	private int ageIncrementDerive() {
		int fixed = configuration.elementGet(configurationElementAgeIncrement());
		int randomMaximum = configuration.elementGet(configurationElementAgeRandomIncrement());

		int randomAmount = General.randomGetIntegerExclusive(randomMaximum + 1);

		int result = fixed + randomAmount;

		// only grow one plant block at a time maximum
		if (result > AgeSize) {
			result = AgeSize;
		}

		return result;
	}

	protected abstract ConfigurationElementInteger configurationElementAgeIncrement();

	protected abstract ConfigurationElementInteger configurationElementAgeRandomIncrement();

	// assumes a plant block exists at given position
	private int sizeYGet(World world, BlockPos basePosition) {
		for (int i = 0; i < (SizeYMaximum - 1); i++) {
			int offsetY = i + 1;

			BlockPos testPosition = basePosition.add(0, offsetY, 0);

			Block block = world.getBlockState(testPosition).getBlock();

			if (!block.equals(plantBlock())) {
				// found non plant block
				return offsetY;
			}
		}

		// full size
		return 3;
	}

	// position of bottom-most plant block segment
	private Optional<BlockPos> getBasePosition(World world, BlockPos startPosition) {
		for (int i = 0; i < SizeYMaximum; i++) {
			// check block below
			int offsetY = (i + 1) * -1;

			BlockPos testPosition = startPosition.add(0, offsetY, 0);

			Block block = world.getBlockState(testPosition).getBlock();

			if (!block.equals(plantBlock())) {
				if (!mediaCheck(block)) {
					return Optional.empty();
				}
				// found media below current block
				return Optional.of(testPosition.up());
			}
		}

		// plant is more than 3 blocks high
		return Optional.empty();
	}

	// valid growth media
	/// use Block.canSustainPlant() instead
	protected abstract boolean mediaCheck(Block block);

	// type of block either cactus or reed
	protected abstract Block plantBlock();

	// the age property of the block
	protected abstract PropertyInteger ageProperty();
}