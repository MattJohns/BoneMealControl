package mattjohns.common.math.fractal;

import mattjohns.common.math.General;

public class NoiseSimplex2D {
	protected double frequency;
	protected double amplitude;

	public NoiseSimplex2D(double frequency, double amplitude) {
		this.frequency = frequency;
		if (General.isNearlyZero(this.frequency)) {
			this.frequency = 1.0;
		}

		this.amplitude = amplitude;
	}

	public int getInt(int x, int y) {
		return (int)get((double)x, (double)y);
	}

	public double get(double x, double y) {
		double returnValue = rawGet(x, y);
		returnValue *= amplitude;
		return returnValue;
	}

	public double rawGet(double x, double y) {
		return NoiseSimplex.noise(x / frequency, y / frequency);
	}

	public double normalGet(double x, double y) {
		return General.normalize(rawGet(x, y), -1, 1);
	}
}
