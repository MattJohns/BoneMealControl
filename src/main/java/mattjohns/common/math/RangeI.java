package mattjohns.common.math;

// inclusive
public class RangeI {
	public int minimum;
	public int maximum;
	public boolean isZero;

	public RangeI() {
		this.isZero = true;
		this.minimum = -1;
		this.maximum = -1;
	}

	public RangeI(int minimum, int maximum) {
		this.isZero = false;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	public int sizeGet() {
		return maximum - minimum;
	}

	public boolean isEmpty() {
		return sizeGet() <= 0;
	}

	// inclusive
	public boolean isInside(int item) {
		if (item >= minimum && item <= maximum) {
			return true;
		}

		return false;
	}

	// inclusive
	public boolean isPositiveNonZero() {
		return delta() > 0;
	}
	
	public boolean isPositiveOrZero() {
		return delta() >= 0;
	}

	public int delta() {
		return maximum - minimum;
	}

	public int cap(int item) {
		if (item < minimum)
			return minimum;

		if (item > maximum)
			return maximum;

		return item;
	}
}
