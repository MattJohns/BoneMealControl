package mattjohns.minecraft.common.chunk;

import mattjohns.minecraft.common.block.BlockPos2d;
import mattjohns.minecraft.common.terrain.BiomeList;
import net.minecraft.world.biome.Biome;

/**
 * Effectively ChunkSlice<Biome> .
 */
public class ChunkSliceBiome {
	public Biome[] array;

	public ChunkSliceBiome() {
	}

	public void allocate() {
		array = new Biome[256];
	}

	public void set(BlockPos2d offset, Biome biome) {
		array[(offset.x * 16) + offset.y] = biome;
	}

	public Biome get(BlockPos2d offset) {
		return array[(offset.x * 16) + offset.y];
	}

	public void copy(ChunkSliceInteger idList, BiomeList fullBiomeList) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int id = idList.array[x][z];

				Biome biome = fullBiomeList.get(id);
				if (biome == null) {
					///
				}

				set(new BlockPos2d(x, z), biome);
			}
		}
	}
}
