package mattjohns.common.math;

public class RangeD {
	public double minimum;
	public double maximum;

	public RangeD(double minimum, double maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	public double sizeGet() {
		return maximum - minimum;
	}

	public boolean validate() {
		if (sizeGet() < 0.0)
			return false;

		return true;
	}

	public boolean isInside(int item) {
		if (item >= minimum && item <= maximum) {
			return true;
		}

		return false;
	}

	public double cap(double item) {
		if (item < minimum)
			return minimum;

		if (item > maximum)
			return maximum;

		return item;
	}
}
