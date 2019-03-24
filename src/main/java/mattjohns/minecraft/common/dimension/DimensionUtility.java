package mattjohns.minecraft.common.dimension;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

import mattjohns.common.immutable.list.ListImmutableInteger;
import mattjohns.common.immutable.list.ListImmutableString;

public class DimensionUtility {
	public static ListImmutableString dimensionIdToName(ListImmutableInteger dimensionIdList) {
		ListImmutableString.Builder builder = ListImmutableString.Builder.of();

		for (int dimensionId : dimensionIdList) {
			builder.add(dimensionIdToName(dimensionId));
		}

		return builder.build();
	}

	public static String dimensionIdToName(int dimensionId) {
		try {
			DimensionType dimensionType = DimensionManager.getProviderType(dimensionId);

			// dimension is registered, use proper name
			String name = dimensionType.getName();
			if (name == null || name.isEmpty()) {
				// invalid name, use numeric
				return Integer.toString(dimensionId);
			}

			return name;
		} catch (IllegalArgumentException exception) {
			// not registered, use numeric
			return Integer.toString(dimensionId);
		}
	}

}
