package mattjohns.minecraft.common.terrain;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import mattjohns.common.math.Vector3D;
import mattjohns.common.math.fractal.Mandelbulb;
import mattjohns.common.math.fractal.NoiseSimplex2D;

public class GroundFractal {
	NoiseSimplex2D noiseField1 = new NoiseSimplex2D(120.1, 24);
	NoiseSimplex2D noiseField2 = new NoiseSimplex2D(211.21, 14);
	NoiseSimplex2D noiseField3 = new NoiseSimplex2D(511.21, 64);

	public IBlockState getNoise(int x, int y, int z) {
		double groundY1 = noiseField1.rawGet(x, z);
		groundY1 = Math.abs(groundY1);

		groundY1 *= 2.0;
		groundY1 = 1.0 - groundY1;

		groundY1 *= 24;

		double groundY2 = noiseField2.rawGet(x, z);
		groundY2 *= 10;

		double groundY3 = noiseField3.rawGet(x, z);
		groundY3 *= 64;

		double groundYD = groundY1 + groundY2 + groundY3;

		int groundY = (int)groundYD;

		if (y < groundY)
			return Blocks.STONE.getDefaultState();

		return null;
	}

	public IBlockState getFractal(int x, int y, int z) {
		if (y > 64)
			return null;

		// transform
		double scaleHorizontal = 0.001;
		double scaleVertical = 0.01;
		Vector3D scale = new Vector3D(scaleHorizontal, scaleVertical, scaleHorizontal);

		Vector3D offset = new Vector3D(0.0, 0, 0.0);

		double dx = (double)x;
		double dy = (double)y;
		double dz = (double)z;

		dx += offset.x;
		dy += offset.y;
		dz += offset.z;

		dx *= scale.x;
		dy *= scale.y;
		dz *= scale.z;

		if (Mandelbulb.get(dx, dy, dz, 0.1))
			return Blocks.GRASS.getDefaultState();

		return null;
	}
}
