package mattjohns.minecraft.common.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import mattjohns.common.math.Vector2I;
import mattjohns.common.math.Vector3D;
import mattjohns.common.math.fractal.Mandelbulb;

public class ScatterTree extends ScatterGrid {
	public ScatterTree(int gridSize) {
		super(gridSize);
	}

	public ScatterTree2dData treeGet2dData(int positionX, int positionZ, int groundY, int centerGroundY) {
		Scatter2dData base2dData = super.get2dData(positionX, positionZ, groundY, centerGroundY);
		if (base2dData == null)
			return null;

		ScatterTree2dData returnValue = new ScatterTree2dData(base2dData);

		return returnValue;
	}

	// blind because it doesn't ensure within bounding box
	public IBlockState blockGet(Scatter2dData data, int positionY) {
		int centerY = data.centerGroundY + data.halfSizeY;
		int offsetY = positionY - centerY;

		if (Math.abs(offsetY) <= data.halfSizeY) {
			int offsetX = data.offsetToCenterX;
			int offsetZ = data.offsetToCenterZ;

			if (isTrunkMandelBulb(offsetX, offsetY, offsetZ, data)) {
				return Blocks.LOG.getDefaultState();
			}

			if (offsetY > 0) {
				// return Blocks.LEAVES.getDefaultState();
			}
		}

		return null;
	}

	// offset is from center at mid-y
	private boolean isTrunkMandelBulb(int offsetX, int offsetY, int offsetZ, Scatter2dData data) {

		double radiusX = (double)data.halfSizeX / 2.0;
		double radiusZ = (double)data.halfSizeZ / 2.0;

		if (radiusX <= 0.5)
			radiusX = 1.0;
		if (radiusZ <= 0.5)
			radiusZ = 1.0;

		double sizeX = (double)data.halfSizeX * 2.0 + 1.0;
		double sizeY = (double)data.halfSizeY * 2.0 + 1.0;
		double sizeZ = (double)data.halfSizeZ * 2.0 + 1.0;

		double x = (double)offsetX / sizeX;
		double y = (double)offsetY / sizeY;
		double z = (double)offsetZ / sizeZ;

		// transform
		double scaleAll = 0.01;
		Vector3D scale = new Vector3D(1.0, 0.1, 1.0);
		scale = scale.multiply(scaleAll);

		/////
		Vector2I grid = positionToGrid(data.positionX, data.positionZ);
		long seed = seedGet(grid.x, grid.y);
		random.setSeed(seed);
		double ox = random.nextDouble();
		double oz = random.nextDouble();

		Vector3D offset = new Vector3D(ox * 0.1, 2.345, oz * 0.1);

		x *= scale.x;
		y *= scale.y;
		z *= scale.z;

		x += offset.x;
		y += offset.y;
		z += offset.z;

		return Mandelbulb.get(x, y, z, 1.0);
	}
}
