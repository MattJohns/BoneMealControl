package mattjohns.minecraft.common.chunk;

import mattjohns.common.math.Vector2I;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

/**
 * Needs to be sortable when doing a binary search on a list of chunks.
 */
public class ChunkPosSortable extends ChunkPos implements Comparable<ChunkPosSortable> {
	public ChunkPosSortable(int x, int z) {
		super(x, z);
	}
	
	public ChunkPosSortable(ChunkPos source) {
		super(source.x, source.z);
	}

	public ChunkPosSortable(Chunk source) {
		super(source.x, source.z);
	}

	@Override
	public int compareTo(ChunkPosSortable item) {
		if (this.x < item.x)
			return -1;
		if (this.x > item.x)
			return 1;

		if (this.z < item.z)
			return -1;
		if (this.z > item.z)
			return 1;

		return 0;
	}
	
	public Vector2I subtract(ChunkPos item2) {
		return new Vector2I(x - item2.x, z - item2.z);
	}
}
