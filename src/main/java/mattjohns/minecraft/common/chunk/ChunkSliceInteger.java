package mattjohns.minecraft.common.chunk;

import mattjohns.minecraft.common.block.BlockPos2d;

/**
 * Effectively ChunkSlice<Integer> .
 */
public class ChunkSliceInteger {
	public int[][] array;

	public ChunkSliceInteger() {
	}

	public void allocate() {
		array = new int[16][16];
	}

	public int get(BlockPos2d position) {
		return get(position.x, position.y);
	}

	public int get(int x, int y) {
		return array[x][y];
	}

	public void set(BlockPos2d position, int item) {
		set(position.x, position.y, item);
	}

	public void set(int x, int y, int item) {
		array[x][y] = item;
	}
}
