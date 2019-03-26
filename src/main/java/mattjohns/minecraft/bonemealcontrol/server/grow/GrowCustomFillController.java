package mattjohns.minecraft.bonemealcontrol.server.grow;

import java.util.ArrayList;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.common.block.BlockStateUtility;
import mattjohns.minecraft.common.system.SystemUtility;

// logic for custom bone meal targets defined by the user
public class GrowCustomFillController {
	protected CommonConfiguration configuration;

	public GrowCustomFillController(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, BlockPos targetPosition, GrowCustomFillItem item) {
		IBlockState targetExistBlockState = world.getBlockState(targetPosition);
		Block targetExistBlock = targetExistBlockState.getBlock();

		IBlockState targetDesireBlockState = item.targetBlockStateCache;
		Block targetDesireBlock = targetExistBlockState.getBlock();

		assert targetExistBlock.equals(targetDesireBlock);

		// ensure center target block state matches criteria otherwise don't
		// attempt to fill radius

		ArrayList<String> targetDesireBlockStateKeyList = item.targetBlockStateKeyListCache;
		
		if (!BlockStateUtility.compareSpecific(targetExistBlockState, targetDesireBlockState, targetDesireBlockStateKeyList)) {
			// block state didn't match
			return false;
		}

		// get all blocks within custom radius
		ArrayList<BlockPos> radiusOffsetList = SystemUtility.radiusGetBlockOffsetList(item.radius);

		// block that will be used to fill
		IBlockState fillDesireBlockState = item.fillBlockStateCache;

		// special handling when placing double-high blocks
		Optional<IBlockState> fillDesireBlockStateTopHalf = blockStateGetTopHalf(fillDesireBlockState);

		// fill each block in the radius
		for (BlockPos radiusOffset : radiusOffsetList) {
			BlockPos fillPosition;

			// ensure base block is same type as target
			BlockPos targetSubPosition = targetPosition.add(radiusOffset);
			IBlockState targetSubBlockState = world.getBlockState(targetSubPosition);
			Block targetSubBlock = targetSubBlockState.getBlock();
			if (!targetSubBlock.equals(targetExistBlock)) {
				// radius block is not the target type, ignore it
				continue;
			}

			// also check block state for target
			if (!BlockStateUtility.compareSpecific(targetSubBlockState, targetDesireBlockState, targetDesireBlockStateKeyList)) {
				// block state didn't match
				continue;
			}

			// if placing on surface then fill position is up
			if (item.surfaceIsFill) {
				fillPosition = targetSubPosition.up();
			} else {
				fillPosition = targetSubPosition;
			}

			boolean placementIsValid = true;

			IBlockState fillAboveBlockState = world.getBlockState(fillPosition.up());
			Block fillAboveBlock = fillAboveBlockState.getBlock();

			// ensure not covered

			// by air
			if (item.aboveIsRequireAir) {
				if (!fillAboveBlock.equals(Blocks.AIR)) {
					placementIsValid = false;
				}
			}

			// by opaque block
			if (item.aboveIsRequireTransparent) {
				if (fillAboveBlockState.getMaterial().isOpaque()) {
					placementIsValid = false;
				}
			}

			// don't overwrite anything except air if placing on surface
			if (item.surfaceIsFill) {
				Block fillExistBlock = world.getBlockState(fillPosition).getBlock();
				if (!fillExistBlock.equals(Blocks.AIR)) {
					placementIsValid = false;
				}
			}

			// chance
			double effectiveChance = item.chance;

			// always place center block
			if (item.centerIsAlwaysFill) {
				if (radiusOffset.getX() == 0 && radiusOffset.getY() == 0) {
					effectiveChance = 1.0;
				}
			}

			if (!General.randomChance(effectiveChance)) {
				// chance failed
				placementIsValid = false;
			}

			if (!placementIsValid) {
				continue;
			}

			// place it
			if (fillDesireBlockStateTopHalf.isPresent()) {
				// double-high block, only place if there's space
				if (world.getBlockState(fillPosition.up()).getBlock().equals(Blocks.AIR)) {
					// bottom
					world.setBlockState(fillPosition, fillDesireBlockState, 2);

					// top
					world.setBlockState(fillPosition.up(), fillDesireBlockStateTopHalf.get(), 2);
				}
			} else {
				// normal single block
				world.setBlockState(fillPosition, fillDesireBlockState, 2);
			}
		}

		return true;
	}

	// gets the top block if the given block is double-high, otherwise returns empty 
	protected Optional<IBlockState> blockStateGetTopHalf(IBlockState bottomBlockState) {
		Block bottomBlock = bottomBlockState.getBlock();

		if (bottomBlock.equals(Blocks.DOUBLE_PLANT)) {
			return Optional
					.of(bottomBlockState.withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.UPPER));
		}

		if (bottomBlock.equals(Blocks.ACACIA_DOOR) || bottomBlock.equals(Blocks.BIRCH_DOOR)
				|| bottomBlock.equals(Blocks.DARK_OAK_DOOR) || bottomBlock.equals(Blocks.JUNGLE_DOOR)
				|| bottomBlock.equals(Blocks.OAK_DOOR) || bottomBlock.equals(Blocks.SPRUCE_DOOR)
				|| bottomBlock.equals(Blocks.IRON_DOOR)) {
			return Optional.of(bottomBlockState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
		}

		return Optional.empty();
	}
}
