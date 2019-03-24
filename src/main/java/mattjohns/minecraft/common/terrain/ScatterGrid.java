package mattjohns.minecraft.common.terrain;

import java.util.Random;

import mattjohns.common.math.Vector2I;
import mattjohns.common.math.Vector3I;
import mattjohns.minecraft.common.block.BlockPos2d;

public class ScatterGrid {
	private int gridSize;

	Random random = new Random();

	public ScatterGrid(int gridSize) {
		this.gridSize = gridSize;
	}

	public boolean checkPosition(int x, int z) {
		int gridX = x / gridSize;
		int gridZ = z / gridSize;

		int offsetX = x % gridSize;
		if (offsetX < 0) {
			offsetX = gridSize + offsetX;
		}
		int offsetZ = z % gridSize;
		if (offsetZ < 0) {
			offsetZ = gridSize + offsetZ;
		}

		long seed = seedGet(gridX, gridZ);
		random.setSeed(seed);

		int randomPositionX = random.nextInt(gridSize);
		int randomPositionY = random.nextInt(gridSize);

		int randomHalfSizeX = random.nextInt(3);
		int randomHalfSizeY = random.nextInt(3);

		/// won't extend past grid, so bounce it back off the wall if go too far

		// all structures have a maximum size of the grid, so large structures
		// need to be spaced out more than small ones roughly speaking
		if (offsetX >= randomPositionX - randomHalfSizeX && offsetX <= randomPositionX + randomHalfSizeX) {
			if (offsetZ >= randomPositionY - randomHalfSizeY && offsetZ <= randomPositionY + randomHalfSizeY) {
				return true;
			}
		}

		return false;
	}

	protected Vector2I positionToGrid(int positionX, int positionZ) {
		int gridX = Math.floorDiv(positionX, gridSize);
		int gridZ = Math.floorDiv(positionZ, gridSize);

		return new Vector2I(gridX, gridZ);
	}

	protected BlockPos2d positionToGridOffset(int positionX, int positionZ) {
		int offsetX = Math.floorMod(positionX, gridSize);
		int offsetZ = Math.floorMod(positionZ, gridSize);

		return new BlockPos2d(offsetX, offsetZ);
	}

	protected BlockPos2d gridOffsetToAbsolute(int gridX, int gridZ, int offsetX, int offsetZ) {
		BlockPos2d gridBase = gridGetBasePosition(gridX, gridZ);

		return gridBase.add(new BlockPos2d(offsetX, offsetZ));
	}

	protected BlockPos2d gridGetBasePosition(int gridX, int gridZ) {
		int baseX = gridX * gridSize;
		int baseZ = gridZ * gridSize;

		return new BlockPos2d(baseX, baseZ);
	}

	public Scatter2dData get2dData(int x, int z, int groundY, int centerGroundY) {
		Vector2I grid = positionToGrid(x, z);

		BlockPos2d centerOffsetToGrid = centerGetOffsetByGrid(grid.x, grid.y);

		Vector3I halfSize = sizeGetHalfByGrid(grid.x, grid.y);

		Vector2I offsetToGrid = positionToGridOffset(x, z);

		if (offsetToGrid.x < centerOffsetToGrid.x - halfSize.x)
			return null;
		if (offsetToGrid.x > centerOffsetToGrid.x + halfSize.x)
			return null;
		if (offsetToGrid.y < centerOffsetToGrid.y - halfSize.z)
			return null;
		if (offsetToGrid.y > centerOffsetToGrid.y + halfSize.z)
			return null;

		Scatter2dData returnValue = new Scatter2dData();

		returnValue.positionX = x;
		returnValue.positionZ = z;

		returnValue.offsetToGridX = offsetToGrid.x;
		returnValue.offsetToGridZ = offsetToGrid.y;

		returnValue.offsetToCenterX = centerOffsetToGrid.x - offsetToGrid.x;
		returnValue.offsetToCenterZ = centerOffsetToGrid.y - offsetToGrid.y;

		returnValue.halfSizeX = halfSize.x;
		returnValue.halfSizeY = halfSize.y;
		returnValue.halfSizeZ = halfSize.z;

		returnValue.groundY = groundY;
		returnValue.centerGroundY = centerGroundY;

		return returnValue;
	}

	public BlockPos2d centerGetAbsolute(int positionX, int positionZ) {
		Vector2I grid = positionToGrid(positionX, positionZ);

		BlockPos2d offsetToGrid = centerGetOffsetByGrid(grid.x, grid.y);

		return gridOffsetToAbsolute(grid.x, grid.y, offsetToGrid.x, offsetToGrid.y);
	}

	protected BlockPos2d centerGetOffsetByGrid(int gridX, int gridZ) {
		long seed = seedGet(gridX, gridZ);
		random.setSeed(seed);

		int centerOffsetToGridX = 50;
		int centerOffsetToGridZ = 50;

		return new BlockPos2d(centerOffsetToGridX, centerOffsetToGridZ);
	}

	protected Vector3I sizeGetHalfByGrid(int gridX, int gridZ) {
		long seed = seedGet(gridX, gridZ);
		random.setSeed(seed);

		int randomHalfSizeX = 10;
		int randomHalfSizeY = 10;
		int randomHalfSizeZ = 10;

		return new Vector3I(randomHalfSizeX, randomHalfSizeY, randomHalfSizeZ);
	}

	private int seed2(int _s) {
		long s = 192837463l ^ Math.abs(_s);
		long a = 1664525l;
		long c = 1013904223l;
		long m = 4294967296l;
		return (int)((s * a + c) % m);
	}

	// https://stackoverflow.com/questions/10693111/how-to-generate-a-seed-from-an-xy-coordinate
	protected int seedGet(int x, int y) {
		int sx = seed2(x * 1947);
		int sy = seed2(y * 2904);
		return seed2(sx ^ sy);
	}
}
