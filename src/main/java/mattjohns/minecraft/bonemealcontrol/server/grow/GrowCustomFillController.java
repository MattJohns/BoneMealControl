package mattjohns.minecraft.bonemealcontrol.server.grow;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.common.math.General;
import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;
import mattjohns.minecraft.common.system.SystemUtility;

// logic for custom bonemeal targets defined by the user
public class GrowCustomFillController {
	protected CommonConfiguration configuration;

	public GrowCustomFillController(CommonConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean grow(World world, BlockPos targetPosition, GrowCustomFillItem item) {
		IBlockState targetBlockState = world.getBlockState(targetPosition);
		Block targetBlock = targetBlockState.getBlock();

		assert targetBlock.equals(item.targetBlock());

		// get all blocks within custom radius
		ArrayList<BlockPos> fillOffsetList = SystemUtility.radiusGetBlockOffsetList(item.radius);

		// block that will be used to fill
		IBlockState fillBlockStateNew = item.fillBlock().getDefaultState();

		// fill each block in the radius
		for (BlockPos fillOffset : fillOffsetList) {
			BlockPos fillPosition;

			// ensure base block is same type as target
			BlockPos fillBasePosition = targetPosition.add(fillOffset);
			Block fillBaseBlock = world.getBlockState(fillBasePosition).getBlock();
			if (!fillBaseBlock.equals(targetBlock)) {
				continue;
			}

			// if placing on surface then fill position is up
			if (item.surfaceIsFill) {
				fillPosition = fillBasePosition.up();
			} else {
				fillPosition = fillBasePosition;
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
				IBlockState fillBlockStatePre = world.getBlockState(fillPosition);
				if (!fillBlockStatePre.getBlock().equals(Blocks.AIR)) {
					placementIsValid = false;
				}
			}

			// chance
			double effectiveChance = item.chance;

			// always place center block
			if (item.centerIsAlwaysFill) {
				if (fillOffset.getX() == 0 && fillOffset.getY() == 0) {
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
			world.setBlockState(fillPosition, fillBlockStateNew);
		}
		
		return true;
	}
}
