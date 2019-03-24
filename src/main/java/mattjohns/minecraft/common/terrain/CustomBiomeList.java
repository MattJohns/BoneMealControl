package mattjohns.minecraft.common.terrain;

import java.util.ArrayList;

public class CustomBiomeList extends ArrayList<CustomBiome> {
	private static final long serialVersionUID = 1L;

	/// should be sorted by id at very least

	public CustomBiome getByBaseId(int id) {
		for (CustomBiome item : this) {
			if (item.baseBiomeId == id) {
				return item;
			}
		}

		return null;
	}
}
