package mattjohns.common.math;

/**
 * 3 dimensional integer vector.
 */
public class Vector3I implements Comparable<Vector3I> {
	public static final Vector3I ZERO = new Vector3I(0, 0, 0);

	public int x;
	public int y;
	public int z;

	public Vector3I(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3I(Vector3I item) {
		this(item.x, item.y, item.z);
	}

	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public Vector3I add(Vector3I item) {
		return new Vector3I(x + item.x, y + item.y, z + item.z);
	}

	public Vector3I add(int x, int y, int z) {
		return add(new Vector3I(x, y, z));
	}

	public Vector3I subtract(Vector3I item) {
		return new Vector3I(x - item.x, y - item.y, z - item.z);
	}

	public Vector3I multiply(Vector3I item) {
		return new Vector3I(x * item.x, y * item.y, z * item.z);
	}

	/**
	 * Warning: Unsafe in that it doesn't check for zero denominator.
	 */
	public Vector3I divide(Vector3I item) {
		return new Vector3I(x / item.x, y / item.y, z / item.z);
	}

	@Override
	public int compareTo(Vector3I item) {
		if (this.x < item.x)
			return -1;
		if (this.x > item.x)
			return 1;

		if (this.y < item.y)
			return -1;
		if (this.y > item.y)
			return 1;

		if (this.z < item.z)
			return -1;
		if (this.z > item.z)
			return 1;

		return 0;
	}
}