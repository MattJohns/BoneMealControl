package mattjohns.minecraft.common.chunk;

import mattjohns.minecraft.common.block.BlockPos2d;

/**
 * Effectively ChunkSlice<Boolean> .
 */
public class ChunkSliceBoolean {
	public boolean[][] array;

	public void allocate() {
		array = new boolean[16][16];
	}

	public boolean get(BlockPos2d position) {
		return get(position.x, position.y);
	}

	public boolean get(int x, int y) {
		return array[x][y];
	}

	public void set(BlockPos2d position, boolean item) {
		set(position.x, position.y, item);
	}

	public void set(int x, int y, boolean item) {
		array[x][y] = item;
	}
}
