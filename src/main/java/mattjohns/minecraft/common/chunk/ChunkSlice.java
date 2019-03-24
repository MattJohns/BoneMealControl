package mattjohns.minecraft.common.chunk;

import mattjohns.minecraft.common.block.BlockPos2d;

/**
 * A 16x16 array of the given class. Used to store per-column information for a
 * chunk.
 */
public class ChunkSlice<T> {
	private Object[][] array;

	/**
	 * allocate() needs to be called to create the array.
	 */
	public ChunkSlice() {
	}

	/**
	 * Allocates the memory for the array. Must be called before get() or set()
	 */
	public void allocate() {
		array = new Object[16][16];
	}

	public T get(int x, int y) {
		@SuppressWarnings("unchecked")
		final T e = (T)array[x][y];
		return e;
	}

	public T get(BlockPos2d position) {
		return get(position.x, position.y);
	}

	public void set(BlockPos2d position, T item) {
		set(position.x, position.y, item);
	}

	public void set(int x, int y, T item) {
		array[x][y] = item;
	}
}
