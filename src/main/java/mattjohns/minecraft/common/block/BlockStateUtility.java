package mattjohns.minecraft.common.block;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public class BlockStateUtility {
	/**
	 * Converts a string to block state and adds the properties to an existing
	 * block state.
	 * 
	 * The format should have no brackets, just a list of key value pairs in the
	 * format "key:value, key:value".
	 */
	public static IBlockState deserialize(String text, IBlockState defaultBlockState) throws BlockStateException {
		// deserialize to nbt
		NBTTagCompound customTag = deserializeTextToNbt(text);

		// merge with default nbt
		NBTTagCompound defaultTag = NBTUtil.writeBlockState(new NBTTagCompound(), defaultBlockState);
		defaultTag.merge(customTag);

		// deserialize nbt to block state
		IBlockState newBlockState = NBTUtil.readBlockState(defaultTag);

		return newBlockState;
	}

	protected static NBTTagCompound deserializeTextToNbt(String text) throws BlockStateException {
		NBTTagCompound customTag = null;

		String textFull = textDecorate(text);

		try {
			customTag = JsonToNBT.getTagFromJson(textFull);
		} catch (NBTException e) {
			// log and ignore
			throw new BlockStateException("Invalid block state \"" + text + "\".");
		}

		assert customTag != null;

		return customTag;
	}

	// add quotes to all values and also the 'properties' header text that is
	// required by the json to nbt converter
	protected static String textDecorate(String source) throws BlockStateException {
		String resultPartial = "";

		String[] pairList = source.split(",");

		for (int i = 0; i < pairList.length; i++) {
			String pair = pairList[i];

			String[] single = pair.split(":");

			if (single.length != 2) {
				throw new BlockStateException("Malformed block state key value pair \"" + pair
						+ "\".  Expected 2 items separated by a colon but found \"" + single.length
						+ "\" items instead.");
			}

			if (i >= 1) {
				resultPartial += ", ";
			}

			resultPartial += single[0];
			resultPartial += ":";

			// add quotes around value
			resultPartial += "\"" + single[1] + "\"";
		}

		// need to prepend the 'properties' header for nbt converter
		String result = "{Properties:{" + resultPartial + "}" + "}";

		return result;
	}

	/**
	 * Checks all given text keys in subject to ensure subject has matching
	 * property values to criteria. If a key isn't specified by the text key
	 * list then it isn't checked.
	 * 
	 * That way you can have a criteria that checks just some of the block
	 * states while ignoring others.
	 * 
	 * Returns false if any subject contains any properties that are different
	 * than criteria.
	 */
	public static boolean compareSpecific(IBlockState subjectBlockState, IBlockState criteriaBlockState,
			ArrayList<String> criteriaKeyTextList) {

		ImmutableMap<IProperty<?>, Comparable<?>> criteriaPropertyMap = criteriaBlockState.getProperties();
		ImmutableMap<IProperty<?>, Comparable<?>> subjectPropertyMap = subjectBlockState.getProperties();

		Collection<IProperty<?>> subjectKeyList = subjectBlockState.getPropertyKeys();

		for (String criteriaKeyText : criteriaKeyTextList) {
			// check if it's a valid property in subject
			IProperty<?> subjectKey = keyGetByName(subjectKeyList, criteriaKeyText);
			if (subjectKey == null) {
				// criteria has special custom property so no match then
				return false;
			}

			Comparable<?> criteriaValue = criteriaPropertyMap.get(subjectKey);
			Comparable<?> subjectValue = subjectPropertyMap.get(subjectKey);

			// need to serialize values in order to compare because compiler
			// doesn't know
			// types are actually the same
			String criteriaValueText = criteriaValue.toString();
			String subjectValueText = subjectValue.toString();

			if (criteriaValueText.compareToIgnoreCase(subjectValueText) != 0) {
				// criteria is different than subject
				return false;
			}
		}

		// both the same
		return true;
	}

	// search keys by string rather than needing an IProperty<?>
	protected static IProperty<?> keyGetByName(Collection<IProperty<?>> keyList, String keyName) {
		for (IProperty<?> key : keyList) {
			if (key.getName().toString().compareToIgnoreCase(keyName) == 0) {
				return key;
			}
		}

		return null;
	}

	// ignores malformed properties
	public static ArrayList<String> keyListDerive(String blockStateText) {
		ArrayList<String> result = new ArrayList<>();

		String[] pairList = blockStateText.split(",");

		for (int i = 0; i < pairList.length; i++) {
			String pair = pairList[i];

			String[] single = pair.split(":");

			if (single.length != 2) {
				// malformed, ignore it because it should have been picked up
				// earlier when deserializing
				continue;
			}

			result.add(single[0].trim());
		}

		return result;
	}
}
