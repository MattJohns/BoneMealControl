package mattjohns.minecraft.common.terrain;

import net.minecraft.world.biome.Biome;

public class CustomBiome {
	public String baseName;
	public Biome baseBiome;
	public int baseBiomeId;

	public CustomBiome(String baseName, double stoneMapNoiseFrequency, double stoneMapNoiseAmplitude,
			BiomeList fullBiomeList) {
		this.baseName = baseName;

		baseBiome = fullBiomeList.get(this.baseName);
		if (baseBiome == null) {
			// should have been added to biome manager first
			///

			baseBiomeId = -1;
		} else {
			baseBiomeId = Biome.getIdForBiome(baseBiome);
		}
	}
}
