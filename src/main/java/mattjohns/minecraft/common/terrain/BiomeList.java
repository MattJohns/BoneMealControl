package mattjohns.minecraft.common.terrain;

import java.util.HashMap;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.biome.Biome;

import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class BiomeList {
	private HashMap<Integer, Biome> map = new HashMap<>();

	public void add(int id, Biome item) {
		map.putIfAbsent(id, item);
	}

	public Biome get(int id) {
		return map.get(id);
	}

	public Biome get(String name) {
		for (Biome biome : map.values()) {
			if (biome.getBiomeName().compareToIgnoreCase(name) == 0)
				return biome;
		}

		return null;
	}

	public void derive() {
		map.clear();

		for (BiomeType biomeType : BiomeType.values()) {
			derive(biomeType);
		}
	}

	private void derive(BiomeType biomeType) {
		ImmutableList<BiomeEntry> entryList = BiomeManager.getBiomes(biomeType);

		for (BiomeEntry entry : entryList) {
			Biome biome = entry.biome;
			int id = Biome.getIdForBiome(biome);

			add(id, biome);
		}
	}
}
