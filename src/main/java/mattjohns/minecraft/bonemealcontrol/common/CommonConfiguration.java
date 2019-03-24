package mattjohns.minecraft.bonemealcontrol.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mattjohns.common.storage.StorageException;
import mattjohns.common.storage.StorageJson;
import mattjohns.minecraft.bonemealcontrol.server.grow.GrowCustomFillJson;
import mattjohns.minecraft.bonemealcontrol.server.grow.GrowCustomFillList;
import mattjohns.minecraft.common.log.Log;
import mattjohns.minecraft.common.storage.ConfigurationBase;
import mattjohns.minecraft.common.storage.ConfigurationElementBoolean;
import mattjohns.minecraft.common.storage.ConfigurationElementDouble;
import mattjohns.minecraft.common.storage.ConfigurationElementInteger;
import mattjohns.minecraft.common.storage.ConfigurationElementList;
import mattjohns.minecraft.common.storage.ConfigurationElementString;

/**
 * Performs logging and tries to fix configuration if there are errors.
 */
public class CommonConfiguration extends ConfigurationBase {
	// configuration property

	// vanilla bonemeal types
	public static final ConfigurationElementBoolean ElementPlantEnable = ConfigurationElementBoolean.of(CategoryGeneral,
			"PlantEnable", false, "Enable bonemeal on farmland plants.");
	public static final ConfigurationElementBoolean ElementSaplingEnable = ConfigurationElementBoolean
			.of(CategoryGeneral, "SaplingEnable", false, "Enable bonemeal on saplings.");
	public static final ConfigurationElementBoolean ElementGrassBlockEnable = ConfigurationElementBoolean
			.of(CategoryGeneral, "GrassEnable", true, "Enable bonemeal on grass blocks.");
	public static final ConfigurationElementBoolean ElementFlowerEnable = ConfigurationElementBoolean
			.of(CategoryGeneral, "FlowerEnable", true, "Enable bonemeal on flowers and tall grass.");
	public static final ConfigurationElementBoolean ElementMushroomEnable = ConfigurationElementBoolean
			.of(CategoryGeneral, "MushroomEnable", true, "Enable bonemeal on mushrooms.");
	public static final ConfigurationElementBoolean ElementCocoaEnable = ConfigurationElementBoolean.of(CategoryGeneral,
			"CocoaEnable", true, "Enable bonemeal on cocoa.");

	// cactus
	public static final ConfigurationElementBoolean ElementCactusEnable = ConfigurationElementBoolean
			.of(CategoryGeneral, "CactusEnable", false, "Enable bonemeal on cactus.");

	public static final ConfigurationElementInteger ElementCactusAgeIncrement = ConfigurationElementInteger
			.of(CategoryGeneral, "CactusAgeIncrement", 1, 0, 16, "Fixed amount to add to the cactus age.\n"
					+ "The top block of a cactus has an age between 0 and 15.  When it reaches 16 a new block is grown.");

	public static final ConfigurationElementInteger ElementCactusAgeRandomIncrement = ConfigurationElementInteger
			.of(CategoryGeneral, "CactusAgeRandomIncrement", 8, 0, 16, "Random amount to add to the cactus age.\n"
					+ "This is added to the fixed increment above and capped at 16 (i.e. a full block of growth).");

	// reed
	public static final ConfigurationElementBoolean ElementReedEnable = ConfigurationElementBoolean.of(CategoryGeneral,
			"ReedEnable", false, "Enable bonemeal on sugar cane.");

	public static final ConfigurationElementInteger ElementReedAgeIncrement = ConfigurationElementInteger.of(
			CategoryGeneral, "ReedAgeIncrement", 1, 0, 16,
			"Fixed amount to add to the sugar cane age.  Works the same as cactus.");

	public static final ConfigurationElementInteger ElementReedAgeRandomIncrement = ConfigurationElementInteger
			.of(CategoryGeneral, "ReedAgeRandomIncrement", 8, 0, 16, "Random amount to add to the sugar cane age.\n"
					+ "This is added to the fixed increment above and capped at 16 (i.e. a full block of growth).");

	// wart
	public static final ConfigurationElementBoolean ElementWartEnable = ConfigurationElementBoolean.of(CategoryGeneral,
			"WartEnable", false, "Enable bonemeal on nether wart.");

	public static final ConfigurationElementInteger ElementWartAgeIncrement = ConfigurationElementInteger
			.of(CategoryGeneral, "WartAgeIncrement", 1, 0, 3, "Fixed amount to add to the nether wart age.\n"
					+ "Nether wart has an age between 0 and 3 where 3 is fully grown.");

	public static final ConfigurationElementInteger ElementWartAgeRandomIncrement = ConfigurationElementInteger.of(
			CategoryGeneral, "WartAgeRandomIncrement", 1, 0, 3,
			"Random amount to add to the nether wart age.\n" + " This is added to the fixed increment above.");

	// melon block
	public static final ConfigurationElementBoolean ElementMelonEnable = ConfigurationElementBoolean.of(CategoryGeneral,
			"MelonEnable", false,
			"Enable bonemeal on melon and pumpkin stems to grow the stem itself (as in vanilla).");

	public static final ConfigurationElementBoolean ElementMelonBlockEnable = ConfigurationElementBoolean.of(
			CategoryGeneral, "MelonBlockEnable", false,
			"Force melon and pumpkin blocks to grow when applied to a mature stem.");

	public static final ConfigurationElementDouble ElementMelonBlockChance = ConfigurationElementDouble.of(
			CategoryGeneral, "MelonBlockChance", 0.5d, 0d, 1d,
			"Chance of a melon block growing in response to bonemeal on the stem.\n"
					+ "Note that the number of surrounding dirt patches also affects the chance.");

	// custom block transform
	public static final ConfigurationElementString ElementCustomFillFilename = ConfigurationElementString.of(
			CategoryGeneral, "CustomFillFilename", "",
			"Json file that contains any custom bonemeal tranformations.\n" + "See wiki for format.\n"
					+ "File should exist in main Minecraft configuration directory but you can also reference a subfolder.");

	public static final ConfigurationElementBoolean ElementVineEnable = ConfigurationElementBoolean.of(CategoryGeneral,
			"VineEnable", false, "Enable bonemeal on vines.");

	public static final ConfigurationElementInteger ElementVineUpdateIncrement = ConfigurationElementInteger
			.of(CategoryGeneral, "VineUpdateIncrement", 3, 0, 128, "Fixed amount of updates to grow the vine."
					+ "Each update has around 50% chance of growing a new vine but the rules are complicated so check the Minecraft wiki for details.");

	public static final ConfigurationElementInteger ElementVineUpdateRandomIncrement = ConfigurationElementInteger.of(
			CategoryGeneral, "VineUpdateRandomIncrement", 5, 0, 128,
			"Random amount of updates for the vine.\n" + " This is added to the fixed increment above.");

	public static final ConfigurationElementBoolean ElementChorusFlowerEnable = ConfigurationElementBoolean
			.of(CategoryGeneral, "ChorusFlowerEnable", false, "Enable bonemeal on chorus flowers.");

	public static final ConfigurationElementDouble ElementChorusFlowerUpdateChance = ConfigurationElementDouble.of(
			CategoryGeneral, "ChorusFlowerUpdateChance", 0.5d, 0d, 1d,
			"The chance a chorus flower will perform an update when bonemeal is applied.");

	// loaded from custom fill json file, otherwise empty list
	public GrowCustomFillList customFillList;

	public CommonConfiguration(Log log) {
		super(Internal.ConfigurationFilename, Internal.ConfigurationFolder, log);
	}

	@Override
	protected ConfigurationElementList elementList() {
		ConfigurationElementList result = new ConfigurationElementList();

		result.add(ElementPlantEnable);
		result.add(ElementSaplingEnable);
		result.add(ElementGrassBlockEnable);
		result.add(ElementFlowerEnable);
		result.add(ElementMushroomEnable);
		result.add(ElementCocoaEnable);

		result.add(ElementCactusEnable);
		result.add(ElementCactusAgeIncrement);
		result.add(ElementCactusAgeRandomIncrement);

		result.add(ElementReedEnable);
		result.add(ElementReedAgeIncrement);
		result.add(ElementReedAgeRandomIncrement);

		result.add(ElementWartEnable);
		result.add(ElementWartAgeIncrement);
		result.add(ElementWartAgeRandomIncrement);

		result.add(ElementMelonEnable);
		result.add(ElementMelonBlockEnable);

		result.add(ElementCustomFillFilename);

		result.add(ElementVineEnable);
		result.add(ElementVineUpdateIncrement);
		result.add(ElementVineUpdateRandomIncrement);

		result.add(ElementChorusFlowerEnable);
		result.add(ElementChorusFlowerUpdateChance);

		return result;
	}

	@Override
	protected void copyFromStoragePost() {
		super.copyFromStoragePost();

		// load json as the configuration class doesn't do it automatically like
		// the elements
		customFillList = customFillCopyFromStorage();
	}

	// load custom fill types from json
	protected GrowCustomFillList customFillCopyFromStorage() {
		GrowCustomFillList result = new GrowCustomFillList();

		String filename = elementGet(ElementCustomFillFilename).trim();

		if (!filename.isEmpty()) {
			File file = new File(directory(), filename);
			if (file == null || !file.exists()) {
				// cna't find file, log and continue
				log.error("Custom fill file not found \"" + filename + "\".");
				return result;
			}

			// load file
			try {
				GrowCustomFillJson customFillJson = StorageJson.copyFromFile(file.getPath(), GrowCustomFillJson.class);

				result = customFillJson.list;
			} catch (StorageException e) {
				// json load failed
				log.error("Unable to load custom fill file \"" + filename + "\": " + e.getMessage());
				return result;
			}

			// validate
			ArrayList<String> errorList = result.validateAndFix();
			if (!errorList.isEmpty()) {
				// some items failed, log it
				log.error("Some custom fill items were invalid: ");

				for (String error : errorList) {
					log.error("    " + error);
				}
			}
		}

		return result;
	}

	// custom fill is enabled
	public boolean customFillIs() {
		return !customFillList.isEmpty();
	}

	// need to manually sort elements in the file because it's alphabetical by
	// default
	@Override
	protected void sortOrderSet() {
		super.sortOrderSet();

		List<String> keyList = new ArrayList<>();

		for (int i = 0; i < elementList.size(); i++) {
			keyList.add(elementList.get(i).key());
		}

		forgeConfiguration.setCategoryPropertyOrder(CategoryGeneral, keyList);
	}

	public static final class Internal {
		/**
		 * Name of the mod without any spaces and all lower case.
		 */
		public static final String ModNameInternal = "bonemealcontrol";

		/**
		 * Name of the mod for display purposes.
		 */
		public static final String ModNameDisplay = "Bonemeal Control";
		public static final String ModNameDisplayNoSpace = "BonemealControl";

		public static final String VersionMajor = "0";
		public static final String VersionMinor = "1";
		public static final String VersionTestCycle = "0";

		public static final String Version = VersionMajor + "." + VersionMinor;
		public static final String VersionInternal = Version + "." + VersionTestCycle;

		public static final String PackageRoot = "mattjohns.minecraft.bonemealcontrol";
		public static final String PackageCommon = PackageRoot + ".common";
		public static final String PackageClient = PackageRoot + ".client";
		public static final String PackageServer = PackageRoot + ".server";

		public static final String ProxyClassIntegrated = PackageCommon + ".ProxyIntegrated";
		public static final String ProxyClassDedicated = PackageCommon + ".ProxyDedicated";

		public static final String ResourceBaseDirectory = "assets/" + ModNameInternal + "/";

		public static final Optional<String> ConfigurationFolder = Optional.empty();
		public static final String ConfigurationFilename = ModNameDisplayNoSpace + ".cfg";
	}
}
