package mattjohns.minecraft.bonemealcontrol.server.grow;

import java.util.ArrayList;

import net.minecraft.block.Block;

import mattjohns.minecraft.common.system.SystemUtility;

/**
 * User defined bonemeal target. Allows them to specify the type of block to
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

	// block that the target turns into
	public String fillBlockName;

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
	public Block targetBlock() {
		Block result = SystemUtility.blockGet(targetBlockName);
		assert result != null;
		return result;
	}

	// assumes block name is valid
	public Block fillBlock() {
		Block result = SystemUtility.blockGet(fillBlockName);
		assert result != null;
		return result;
	}
}
