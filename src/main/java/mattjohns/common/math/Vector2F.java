package mattjohns.common.math;

/**
 * 2 dimensional floating point vector. 
 */
public class Vector2F {
	public static final Vector2F ZERO = new Vector2F(0f, 0f);

	public float x;
	public float y;

	public Vector2F(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2F(Vector2F item) {
		this(item.x, item.y);
	}

	public static Vector2F convertFrom(Vector2I item) {
		return new Vector2F((float)item.x, (float)item.y);
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public Vector2F add(float x, float y) {
		return add(new Vector2F(x, y));
	}

	public Vector2F add(Vector2F item) {
		return new Vector2F(x + item.x, y + item.y);
	}

	public Vector2F subtract(Vector2F item) {
		return new Vector2F(x - item.x, y - item.y);
	}

	public Vector2F multiply(float item) {
		return new Vector2F(x * item, y * item);
	}

	public Vector2F multiply(Vector2F item) {
		return new Vector2F(x * item.x, y * item.y);
	}

	public Vector2F divide(float item) {
		if (General.isNearlyZero(item))
			return ZERO;

		return new Vector2F(x / item, y / item);
	}

	public Vector2F divide(Vector2F item) {
		return new Vector2F(x / item.x, y / item.y);
	}

	public float aspectRatio() {
		if (General.isNearlyEqual(y, 0f))
			return 0f;

		return (float)x / (float)y;
	}

	public float angle() {
		return (float)Math.atan2(y, x);
	}

	public Vector2F unit() {
		return divide(magnitude());
	}

	public float magnitude() {
		float square = (float)(x * x) + (float)(y * y);
		if (General.isNearlyZero(square))
			return 0f;

		return (float)Math.sqrt(square);
	}
}