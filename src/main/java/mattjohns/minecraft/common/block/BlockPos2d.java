package mattjohns.minecraft.common.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import mattjohns.common.math.Vector2I;

/**
 * Horizontal block position, ignores height.
 */
public class BlockPos2d extends Vector2I {
	public BlockPos2d(int x, int z) {
		super(x, z);
	}

	public BlockPos2d(BlockPos source3d) {
		super(source3d.getX(), source3d.getZ());
	}

	public BlockPos2d(BlockPos2d source) {
		super(source.x, source.y);
	}
	
	public BlockPos2d(ChunkPos chunkPosition) {
		super(chunkPosition.x * 16, chunkPosition.z * 16);
	}

	public BlockPos2d add(BlockPos2d item) {
		return new BlockPos2d(x + item.x, y + item.y);
	}
	
	public BlockPos convertToBlockPos() {
		return new BlockPos(x, 0, y);
	}
}
