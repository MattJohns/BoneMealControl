package mattjohns.minecraft.bonemealcontrol.server.grow;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import mattjohns.minecraft.common.block.BlockStateException;
import mattjohns.minecraft.common.block.BlockStateUtility;
import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.system.SystemUtility;

/**
 * User defined bone meal target. Allows them to specify the type of block to
 * replace the target with.
 * 
 * Can set a radius with partial fill rather than a target block. Can place
 * blocks on the surface of the target rather than replacing the target (e.g.
 * mushrooms on mycelium).
 */
public class GrowCustomFillItem {
	public static final int RadiusMinimum = 1;
	public static final int RadiusMaximum = 8;
	public static final double ChanceMinimum = 0d;
	public static final double ChanceMaximum = 1d;

	// not used anywhere, just for documenting the JSON file
	public String description;

	// block that the bonemeal is used on
	public String targetBlockName;

	public String targetBlockStateText;

	// Cache block state so it doesn't have to be derived from text each time.
	// Transient marks this as internal (i.e. not deserialized by gson).
	protected transient IBlockState targetBlockStateCache;

	// List of block state property keys to check against target, derived
	// automatically from targetBlockStateText member.
	//
	// Need this list so that you can compare a just a few properties against a
	// block rather than checking every single property. That way the user can
	// specify a partial criteria.
	//
	// For example you can specify that a chest must be facing south without
	// caring about the chest content.
	protected transient ArrayList<String> targetBlockStateKeyListCache;

	// block that the target turns into
	public String fillBlockName;

	public String fillBlockStateText;

	// Block state used to fill. Doesn't need text key list because no
	// comparisons are needed, it's simply placed into the world.
	protected transient IBlockState fillBlockStateCache;

	// Radius of the bonemeal effect. This is direct line radius as opposed to
	// taxi cab distance. It's measured from the center of surrounding blocks to
	// the center of the target block.
	public double radius;

	// Chance of growth occurring. Applied on each placement when radius is
	// used.
	public double chance;

	// ignore chance for the target block and always fill it
	public boolean centerIsAlwaysFill;

	// Place block on the surface of the target rather than replace it. Only
	// places if there is air above the target.
	public boolean surfaceIsFill;

	// Only place if there is a transparent block above the target. If surface
	// fill is used then transparent block must be 2 above target.
	public boolean aboveIsRequireTransparent;

	// Same as transparent but requires air instead.
	public boolean aboveIsRequireAir;

	// Tries to fix any errors by using default values, returns false if can't
	// be fixed.
	//
	// Error list is appended in with errors.
	public boolean validateAndFix(ArrayList<String> errorList) {
		boolean result = true;

		// target block name
		if (targetBlockName == null) {
			result = false;
			errorList.add("targetBlockName is missing");
		} else {
			if (!SystemUtility.blockIsExist(targetBlockName)) {
				result = false;
				errorList.add("targetBlockName \"" + targetBlockName + "\" not found.");
			}
		}

		if (targetBlockStateText == null) {
			targetBlockStateText = "";
		}

		// fill block name
		if (fillBlockName == null) {
			result = false;
			errorList.add("fillBlockName is missing");
		} else {
			if (!SystemUtility.blockIsExist(fillBlockName)) {
				result = false;
				errorList.add("fillBlockName \"" + fillBlockName + "\" not found.");
			}
		}

		if (fillBlockStateText == null) {
			fillBlockStateText = "";
		}

		// radius
		if (radius < RadiusMinimum) {
			radius = RadiusMinimum;
			errorList.add("radius is less than minimum of \"" + RadiusMinimum + "\".  Value set to minimum.");
		}

		if (radius > RadiusMaximum) {
			radius = RadiusMaximum;
			errorList.add("fillRadius is greater than maximum of \"" + RadiusMaximum + "\".  Value set to maximum.");
		}

		// chance
		if (chance < ChanceMinimum) {
			chance = ChanceMinimum;
			errorList.add("fillChance is less than minimum of \"" + ChanceMinimum + "\".  Value set to minimum.");
		}

		if (chance > ChanceMaximum) {
			chance = ChanceMaximum;
			errorList.add("fillChance is greater than maximum of \"" + ChanceMaximum + "\".  Value set to maximum.");
		}

		return result;
	}

	// assumes block name is valid
	protected Block targetBlock() {
		Block result = SystemUtility.blockGet(targetBlockName);
		assert result != null;
		return result;
	}

	// assumes block name is valid
	protected Block fillBlock() {
		Block result = SystemUtility.blockGet(fillBlockName);
		assert result != null;
		return result;
	}

	public void blockStateCacheDerive(Log log) {
		// target
		String targetBlockStateTextTrim = targetBlockStateText.trim();
		if (targetBlockStateTextTrim.isEmpty()) {
			targetBlockStateCache = targetBlock().getDefaultState();
		} else {
			targetBlockStateCache = blockStateDerive(targetBlockStateTextTrim, targetBlock().getDefaultState(), log);
		}

		targetBlockStateKeyListCache = BlockStateUtility.keyListDerive(targetBlockStateTextTrim);

		// fill
		String fillBlockStateTextTrim = fillBlockStateText.trim();
		if (fillBlockStateTextTrim.isEmpty()) {
			fillBlockStateCache = fillBlock().getDefaultState();
		} else {
			fillBlockStateCache = blockStateDerive(fillBlockStateTextTrim, fillBlock().getDefaultState(), log);
		}
	}

	public IBlockState targetBlockStateCache() {
		return targetBlockStateCache;
	}

	public IBlockState fillBlockStateCache() {
		return fillBlockStateCache;
	}

	// get block state from text and merge with given state
	protected IBlockState blockStateDerive(String text, IBlockState defaultBlockState, Log log) {
		try {
			return BlockStateUtility.deserialize(text, defaultBlockState);
		} catch (BlockStateException e) {
			// failed, just use default state
			String errorText = e.getMessage() + "  Custom fill block \""
					+ defaultBlockState.getBlock().getLocalizedName() + "\".";
			log.error(errorText);

			return defaultBlockState;
		}
	}
}
