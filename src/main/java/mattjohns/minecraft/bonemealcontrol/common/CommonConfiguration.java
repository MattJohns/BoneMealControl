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
import mattjohns.minecraft.common.storage.ConfigurationElement;
import mattjohns.minecraft.common.storage.ConfigurationElementBoolean;
import mattjohns.minecraft.common.storage.ConfigurationElementDouble;
import mattjohns.minecraft.common.storage.ConfigurationElementInteger;
import mattjohns.minecraft.common.storage.ConfigurationElementList;
import mattjohns.minecraft.common.storage.ConfigurationElementString;

/**
 * Performs logging and tries to fix configuration if there are errors.
 */
public class CommonConfiguration extends ConfigurationBase {
	public static final String CategoryVanilla = "vanilla";
	public static final String CategoryCustom = "custom";
	public static final String CategoryAnimal = "animal";

	// configuration property

	// general
	public static final ConfigurationElementBoolean ElementShowDisableMessageVanilla = ConfigurationElementBoolean.of(
			CategoryGeneral, "ShowDisableMessageVanilla", false,
			"Display a message for the player if they attempt to use bone meal when it's disabled.\n"
					+ "Only for vanilla bone meal usage such as saplings.");

	// vanilla
	public static final ConfigurationElementBoolean ElementPlantEnable = ConfigurationElementBoolean.of(CategoryVanilla,
			"PlantEnable", false, "Enable bone meal on farmland plants.");
	public static final ConfigurationElementBoolean ElementSaplingEnable = ConfigurationElementBoolean
			.of(CategoryVanilla, "SaplingEnable", false, "Enable bone meal on saplings.");
	public static final ConfigurationElementBoolean ElementGrassBlockEnable = ConfigurationElementBoolean
			.of(CategoryVanilla, "GrassEnable", true, "Enable bone meal on grass blocks.");
	public static final ConfigurationElementBoolean ElementFlowerEnable = ConfigurationElementBoolean
			.of(CategoryVanilla, "FlowerEnable", true, "Enable bone meal on flowers and tall grass.");
	public static final ConfigurationElementBoolean ElementMushroomEnable = ConfigurationElementBoolean
			.of(CategoryVanilla, "MushroomEnable", true, "Enable bone meal on mushrooms.");
	public static final ConfigurationElementBoolean ElementCocoaEnable = ConfigurationElementBoolean.of(CategoryVanilla,
			"CocoaEnable", true, "Enable bone meal on cocoa.");

	// custom

	// cactus
	public static final ConfigurationElementBoolean ElementCactusEnable = ConfigurationElementBoolean.of(CategoryCustom,
			"CactusEnable", false, "Enable bone meal on cactus.");

	public static final ConfigurationElementInteger ElementCactusAgeIncrement = ConfigurationElementInteger
			.of(CategoryCustom, "CactusAgeIncrement", 1, 0, 16, "Fixed amount to add to the cactus age.\n"
					+ "The top block of a cactus has an age between 0 and 15.  When it reaches 16 a new block is grown.");

	public static final ConfigurationElementInteger ElementCactusAgeRandomIncrement = ConfigurationElementInteger
			.of(CategoryCustom, "CactusAgeRandomIncrement", 8, 0, 16, "Random amount to add to the cactus age.\n"
					+ "This is added to the fixed increment above and capped at 16 (i.e. a full block of growth).");

	// reed
	public static final ConfigurationElementBoolean ElementReedEnable = ConfigurationElementBoolean.of(CategoryCustom,
			"ReedEnable", false, "Enable bone meal on sugarcane.");

	public static final ConfigurationElementInteger ElementReedAgeIncrement = ConfigurationElementInteger.of(
			CategoryCustom, "ReedAgeIncrement", 1, 0, 16,
			"Fixed amount to add to the sugarcane age.  Works the same as cactus.");

	public static final ConfigurationElementInteger ElementReedAgeRandomIncrement = ConfigurationElementInteger
			.of(CategoryCustom, "ReedAgeRandomIncrement", 8, 0, 16, "Random amount to add to the sugarcane age.\n"
					+ "This is added to the fixed increment above and capped at 16 (i.e. a full block of growth).");

	// wart
	public static final ConfigurationElementBoolean ElementWartEnable = ConfigurationElementBoolean.of(CategoryCustom,
			"WartEnable", false, "Enable bone meal on nether wart.");

	public static final ConfigurationElementInteger ElementWartAgeIncrement = ConfigurationElementInteger
			.of(CategoryCustom, "WartAgeIncrement", 1, 0, 3, "Fixed amount to add to the nether wart age.\n"
					+ "Nether wart has an age between 0 and 3 where 3 is fully grown.");

	public static final ConfigurationElementInteger ElementWartAgeRandomIncrement = ConfigurationElementInteger.of(
			CategoryCustom, "WartAgeRandomIncrement", 1, 0, 3,
			"Random amount to add to the nether wart age.\n" + " This is added to the fixed increment above.");

	// jungle log
	public static final ConfigurationElementBoolean ElementJungleLogEnable = ConfigurationElementBoolean.of(
			CategoryCustom, "JungleLogEnable", false,
			"Enable bone meal on jungle logs to create cocoa.");

	public static final ConfigurationElementDouble ElementJungleLogChance = ConfigurationElementDouble.of(
			CategoryCustom, "JungleLogChance", 0.5d, 0d, 1d,
			"Chance of a cocoa growing in response to bone meal on the log.\n"
					+ "The chance is affected by the number of surrounding air blocks.");
	
	// jungle leaf
	public static final ConfigurationElementBoolean ElementJungleLeafEnable = ConfigurationElementBoolean.of(
			CategoryCustom, "JungleLeafEnable", false,
			"Enable bone meal on jungle leaves to create vines.");

	public static final ConfigurationElementDouble ElementJungleLeafChance = ConfigurationElementDouble.of(
			CategoryCustom, "JungleLeafChance", 0.5d, 0d, 1d,
			"Chance of a vine growing in response to bone meal on the leaf.\n"
					+ "The chance is affected by the number of surrounding air blocks.");
	
	// melon block
	public static final ConfigurationElementBoolean ElementMelonEnable = ConfigurationElementBoolean.of(CategoryCustom,
			"MelonEnable", true,
			"Enable bone meal on melon and pumpkin stems to grow the stem itself (as in vanilla).");

	public static final ConfigurationElementBoolean ElementMelonBlockEnable = ConfigurationElementBoolean.of(
			CategoryCustom, "MelonBlockEnable", false,
			"Force melon and pumpkin blocks to grow when applied to a mature stem.");

	public static final ConfigurationElementDouble ElementMelonBlockChance = ConfigurationElementDouble.of(
			CategoryCustom, "MelonBlockChance", 0.5d, 0d, 1d,
			"Chance of a melon block growing in response to bone meal on the stem.\n"
					+ "Note that the number of surrounding dirt patches also affects the chance.");

	public static final ConfigurationElementBoolean ElementVineEnable = ConfigurationElementBoolean.of(CategoryCustom,
			"VineEnable", false, "Enable bone meal on vines.");

	public static final ConfigurationElementInteger ElementVineUpdateIncrement = ConfigurationElementInteger
			.of(CategoryCustom, "VineUpdateIncrement", 3, 0, 128, "Fixed amount of updates to grow the vine."
					+ "Each update has around 50% chance of growing a new vine but the rules are complicated so check the Minecraft wiki for details.");

	public static final ConfigurationElementInteger ElementVineUpdateRandomIncrement = ConfigurationElementInteger.of(
			CategoryCustom, "VineUpdateRandomIncrement", 5, 0, 128,
			"Random amount of updates for the vine.\n" + " This is added to the fixed increment above.");

	public static final ConfigurationElementBoolean ElementChorusFlowerEnable = ConfigurationElementBoolean
			.of(CategoryCustom, "ChorusFlowerEnable", false, "Enable bone meal on chorus flowers.");

	public static final ConfigurationElementDouble ElementChorusFlowerUpdateChance = ConfigurationElementDouble.of(
			CategoryCustom, "ChorusFlowerUpdateChance", 0.5d, 0d, 1d,
			"The chance a chorus flower will perform an update when bone meal is applied.");

	// custom rule
	public static final ConfigurationElementString ElementCustomFillFilename = ConfigurationElementString.of(
			CategoryCustom, "CustomFillFilename", "",
			"Json file that contains any custom bone meal tranformations.\n" + "See wiki for format.\n"
					+ "File should exist in main Minecraft configuration directory but you can also reference a subfolder.");

	// animal

	public static final ConfigurationElementBoolean ElementZombieEnable = ConfigurationElementBoolean.of(CategoryAnimal,
			"ZombieEnable", false, "Enable bone meal on baby zombies.");

	public static final ConfigurationElementDouble ElementZombieAdultChance = ConfigurationElementDouble.of(
			CategoryAnimal, "ZombieAdultChance", 0.25d, 0d, 1d,
			"The chance a baby zombie will become an adult when bone meal is applied.");

	public static final ConfigurationElementBoolean ElementAnimalEnable = ConfigurationElementBoolean.of(CategoryAnimal,
			"AnimalEnable", false, "Enable bone meal on baby animals.");

	public static final ConfigurationElementInteger ElementAnimalAgeIncrement = ConfigurationElementInteger.of(
			CategoryAnimal, "AnimalAgeIncrement", 100, 0, 1000,
			"Fixed number of seconds to add the animal's age for each bone meal application.");

	public static final ConfigurationElementInteger ElementAnimalAgeRandomIncrement = ConfigurationElementInteger.of(
			CategoryAnimal, "AnimalAgeRandomIncrement", 200, 0, 1000,
			"Random number of seconds to add the animal's age.\n" + "This is added to the fixed increment above.");

	// loaded from custom fill json file, otherwise empty list
	public GrowCustomFillList customFillList;

	public CommonConfiguration(Log log) {
		super(Internal.ConfigurationFilename, Internal.ConfigurationFolder, log);
	}

	@Override
	protected ConfigurationElementList elementList() {
		ConfigurationElementList result = new ConfigurationElementList();

		result.add(ElementShowDisableMessageVanilla);

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
		result.add(ElementVineEnable);
		result.add(ElementVineUpdateIncrement);
		result.add(ElementVineUpdateRandomIncrement);
		result.add(ElementJungleLogEnable);
		result.add(ElementJungleLogChance);
		result.add(ElementJungleLeafEnable);
		result.add(ElementJungleLeafChance);
		result.add(ElementChorusFlowerEnable);
		result.add(ElementChorusFlowerUpdateChance);
		result.add(ElementCustomFillFilename);

		result.add(ElementZombieEnable);
		result.add(ElementZombieAdultChance);
		result.add(ElementAnimalEnable);
		result.add(ElementAnimalAgeIncrement);
		result.add(ElementAnimalAgeRandomIncrement);

		return result;
	}

	// load custom fill types from json
	public void customFillCopyFromStorage() {
		customFillList = new GrowCustomFillList();

		String filename = elementGet(ElementCustomFillFilename).trim();

		if (!filename.isEmpty()) {
			File file = new File(directory(), filename);
			if (file == null || !file.exists()) {
				// cna't find file, log and continue
				log.error("Custom fill file not found \"" + filename + "\".");
				return;
			}

			// load file
			try {
				GrowCustomFillJson customFillJson = StorageJson.copyFromFile(file.getPath(), GrowCustomFillJson.class);

				customFillList = customFillJson.list;
			} catch (StorageException e) {
				// json load failed
				log.error("Unable to load custom fill file \"" + filename + "\": " + e.getMessage());
				return;
			}

			// validate
			ArrayList<String> errorList = customFillList.validateAndFix();
			if (!errorList.isEmpty()) {
				// some items failed, log it
				log.error("Some custom fill items were invalid: ");

				for (String error : errorList) {
					log.error("    " + error);
				}
			}

			// convert block state text to actual states
			customFillList.blockStateCacheDerive(log);
		}
	}

	// need to manually sort elements in the file because it's alphabetical by
	// default
	@Override
	protected void sortOrderSet() {
		super.sortOrderSet();

		List<String> keyListGeneral = new ArrayList<>();
		List<String> keyListVanilla = new ArrayList<>();
		List<String> keyListCustom = new ArrayList<>();
		List<String> keyListAnimal = new ArrayList<>();

		for (ConfigurationElement<?> element : elementList) {
			if (element.category() == CategoryGeneral) {
				keyListGeneral.add(element.key());
			}

			if (element.category() == CategoryVanilla) {
				keyListVanilla.add(element.key());
			}

			if (element.category() == CategoryCustom) {
				keyListCustom.add(element.key());
			}

			if (element.category() == CategoryAnimal) {
				keyListAnimal.add(element.key());
			}
		}

		forgeConfiguration.setCategoryPropertyOrder(CategoryGeneral, keyListGeneral);
		forgeConfiguration.setCategoryPropertyOrder(CategoryVanilla, keyListVanilla);
		forgeConfiguration.setCategoryPropertyOrder(CategoryCustom, keyListCustom);
		forgeConfiguration.setCategoryPropertyOrder(CategoryAnimal, keyListAnimal);
	}

	public static final class Internal {
		/**
		 * Name of the mod without any spaces and all lower case.
		 */
		public static final String ModNameInternal = "bonemealcontrol";

		/**
		 * Name of the mod for display purposes.
		 */
		public static final String ModNameDisplay = "Bone Meal Control";
		public static final String ModNameDisplayNoSpace = "BoneMealControl";

		public static final String VersionMajor = "1";
		public static final String VersionMinor = "2";
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
