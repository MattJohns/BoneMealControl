package mattjohns.common.math;

/**
 * 2 dimensional integer vector. Implements Comparable so can be used in
 * sorting.
 */
public class Vector2I implements Comparable<Vector2I> {
	public static final Vector2I ZERO = new Vector2I(0, 0);

	public int x;
	public int y;

	public Vector2I(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2I(Vector2I item) {
		this(item.x, item.y);
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Vector2I add(Vector2I item) {
		return new Vector2I(x + item.x, y + item.y);
	}

	public Vector2I add(int x, int y) {
		return add(new Vector2I(x, y));
	}

	public Vector2I subtract(Vector2I item) {
		return new Vector2I(x - item.x, y - item.y);
	}

	public Vector2I multiply(Vector2I item) {
		return new Vector2I(x * item.x, y * item.y);
	}

	public Vector2I divide(Vector2I item) {
		return new Vector2I(x / item.x, y / item.y);
	}

	public float aspectRatioGet() {
		if (y == 0)
			return 0f;

		return (float)x / (float)y;
	}

	@Override
	public int compareTo(Vector2I item) {
		if (this.x < item.x)
			return -1;
		if (this.x > item.x)
			return 1;

		if (this.y < item.y)
			return -1;
		if (this.y > item.y)
			return 1;

		return 0;
	}
	
	public Vector2I absolute() {
		return new Vector2I(Math.abs(x), Math.abs(y)); 
	}
	
	public double length() {
		return (double)Math.sqrt((x * x) + (y * y));
	}
	
	public boolean isFlat() {
		return x == 0 || y == 0;
	}
	
	public boolean isPositiveNonFlat() {
		return x > 0 && y > 0;
	}

	public boolean isPositiveOrFlat() {
		return x >= 0 && y >= 0;
	}
}