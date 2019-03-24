package mattjohns.common.math.fractal;

// from https://github.com/errollw/3d-fractal-explorer/blob/master/src/com/erroll/math/fractal/SierpinskiGasket.java
public class SierpinskiGasket {
	private double scale;
	private double distance;

	public SierpinskiGasket(double scale, double distance) {
		this.scale = scale;
		this.distance = distance;
	}

	public boolean get(int x, int y, int z) {
		return get((double)x, (double)y, (double)z);
	}

	public boolean get(double x, double y, double z) {
		final double MI = 100;

		// number of iterations and point being tested
		int i;
		double r = x * x + y * y + z * z;

		for (i = 0; i < MI && r < 7; i++) {
			if ((x + y) < 0) {
				double x1 = -y;
				y = -x;
				x = x1;
			}
			if ((x + z) < 0) {
				double x1 = -z;
				z = -x;
				x = x1;
			}
			if (y + z < 0) {
				double y1 = -z;
				z = -y;
				y = y1;
			}

			x = scale * x - 1 * (scale - 1);
			y = scale * y - 1 * (scale - 1);
			z = scale * z - 1 * (scale - 1);
			r = x * x + y * y + z * z;
		}

		// checks if the distance to fractal is less than d
		return ((Math.sqrt(r) - 2) * Math.pow(scale, (-i)) < distance);
	}

}
