package mattjohns.minecraft.bonemealcontrol.server.grow;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mattjohns.minecraft.bonemealcontrol.common.CommonConfiguration;

// central controller for bonemeal, both custom and vanilla targets 
public class GrowController {
	protected CommonConfiguration configuration;

	// logic for custom targets
	protected GrowCactus cactus;
	protected GrowReed reed;
	protected GrowWart wart;
	protected GrowStemBlock melon;
	protected GrowCustomFillController customFill;
	protected GrowVine vine;
	protected GrowChorusFlower chorusFlower;

	public GrowController(CommonConfiguration configuration) {
		this.configuration = configuration;

		cactus = new GrowCactus(configuration);
		reed = new GrowReed(configuration);
		wart = new GrowWart(configuration);
		melon = new GrowStemBlock(configuration);
		customFill = new GrowCustomFillController(configuration);
		vine = new GrowVine(configuration);
		chorusFlower = new GrowChorusFlower(configuration);
	}

	public GrowResult bonemealUse(World world, BlockPos targetPosition, IBlockState targetblockState) {
		Block targetBlock = targetblockState.getBlock();

		// vanilla

		// farmland plant
		if (targetBlock.equals(Blocks.CARROTS) || targetBlock.equals(Blocks.POTATOES)
				|| targetBlock.equals(Blocks.WHEAT) || targetBlock.equals(Blocks.BEETROOTS)) {
			return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementPlantEnable));
		}

		// sapling
		if (targetBlock.equals(Blocks.SAPLING)) {
			return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementSaplingEnable));
		}

		// grass block
		if (targetBlock.equals(Blocks.GRASS)) {
			return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementGrassBlockEnable));
		}

		// flower
		if (targetBlock.equals(Blocks.TALLGRASS) || targetBlock.equals(Blocks.DOUBLE_PLANT)) {
			return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementFlowerEnable));
		}

		// mushroom
		if (targetBlock.equals(Blocks.BROWN_MUSHROOM) || targetBlock.equals(Blocks.RED_MUSHROOM)) {
			return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementMushroomEnable));
		}

		// cocoa
		if (targetBlock.equals(Blocks.COCOA)) {
			return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementCocoaEnable));
		}

		// custom

		// cactus
		if (targetBlock.equals(Blocks.CACTUS)) {
			if (configuration.elementGet(CommonConfiguration.ElementCactusEnable)) {
				return GrowResult.ofCustom(cactus.grow(world, targetPosition));
			} else {
				return GrowResult.ofCustom(false);
			}
		}

		// reed
		if (targetBlock.equals(Blocks.REEDS)) {
			if (configuration.elementGet(CommonConfiguration.ElementReedEnable)) {
				return GrowResult.ofCustom(reed.grow(world, targetPosition));
			} else {
				return GrowResult.ofCustom(false);
			}
		}

		// wart
		if (targetBlock.equals(Blocks.NETHER_WART)) {
			if (configuration.elementGet(CommonConfiguration.ElementWartEnable)) {
				return GrowResult.ofCustom(wart.grow(world, targetPosition));
			} else {
				return GrowResult.ofCustom(false);
			}
		}

		// melon block
		if (targetBlock.equals(Blocks.MELON_STEM) || targetBlock.equals(Blocks.PUMPKIN_STEM)) {
			boolean stemIsMature = melon.stemCheckMature(world, targetPosition);

			if (stemIsMature) {
				// mature stem, try custom block grow
				if (configuration.elementGet(CommonConfiguration.ElementMelonBlockEnable)) {
					return GrowResult.ofCustom(melon.grow(world, targetPosition));
				} else {
					return GrowResult.ofCustom(false);
				}
			} else {
				// vanilla
				return GrowResult.ofVanilla(configuration.elementGet(CommonConfiguration.ElementMelonEnable));
			}
		}

		// custom fill
		boolean customFillAtLeastOneSuccess = false;

		for (GrowCustomFillItem item : configuration.customFillList) {
			if (targetBlock.equals(item.targetBlock())) {
				// found one
				if (customFill.grow(world, targetPosition, item)) {
					customFillAtLeastOneSuccess = true;
				}
			}
		}

		// Use up bonemeal even if some of the custom fills failed. Only matters
		// for cases where the custom fill list has the same target block for
		// multiple entries.
		if (customFillAtLeastOneSuccess) {
			return GrowResult.ofCustom(true);
		}

		// vine
		if (targetBlock.equals(Blocks.VINE)) {
			if (configuration.elementGet(CommonConfiguration.ElementVineEnable)) {
				return GrowResult.ofCustom(vine.grow(world, targetPosition));
			} else {
				return GrowResult.ofCustom(false);
			}
		}

		// chorus flower
		if (targetBlock.equals(Blocks.CHORUS_FLOWER)) {
			if (configuration.elementGet(CommonConfiguration.ElementChorusFlowerEnable)) {
				return GrowResult.ofCustom(chorusFlower.grow(world, targetPosition));
			} else {
				return GrowResult.ofCustom(false);
			}
		}

		// not a valid target block and must be custom because all vanilla
		// blocks have been tested above
		return GrowResult.ofCustom(false);
	}
}
