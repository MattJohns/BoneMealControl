package mattjohns.common.math;

/**
 * Stores all 4 corners as positions rather than width and height.
 */
public class RectangleI {
	private Vector2I topLeft;
	private Vector2I bottomRight;

	public RectangleI() {
		topLeft = Vector2I.ZERO;
		bottomRight = Vector2I.ZERO;
	}

	public RectangleI(Vector2I topLeft, Vector2I bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}

	public RectangleI(RectangleI item) {
		topLeft = new Vector2I(item.topLeftGet());
		bottomRight = new Vector2I(item.bottomRightGet());
	}

	public static RectangleI convertFrom(RectangleF rectangleF) {
		Vector2I topLeft = new Vector2I((int)rectangleF.topLeftGet().x, (int)rectangleF.topLeftGet().y);
		Vector2I bottomRight = new Vector2I((int)rectangleF.bottomRightGet().x, (int)rectangleF.bottomRightGet().y);

		return new RectangleI(topLeft, bottomRight);
	}

	public static RectangleI convertFrom(Vector2I bottomRight) {
		return new RectangleI(Vector2I.ZERO, bottomRight);
	}

	public static RectangleI createFromPositionAndSize(Vector2I position, Vector2I size) {
		return new RectangleI(position, position.add(size));
	}

	public Vector2I topLeftGet() {
		return topLeft;
	}

	public void topLeftSet(Vector2I item) {
		topLeft = new Vector2I(item);
	}

	public Vector2I bottomRightGet() {
		return bottomRight;
	}

	public void bottomRightSet(Vector2I item) {
		bottomRight = new Vector2I(item);
	}
	
	public int leftGet() {
		return topLeft.x;
	}

	public int topGet() {
		return topLeft.y;
	}

	public int rightGet() {
		return bottomRight.x;
	}

	public int bottomGet() {
		return bottomRight.y;
	}
	
	public Vector2I sizeGet() {
		return bottomRight.subtract(topLeft);
	}

	public String toString() {
		return "(" + topLeft.toString() + ", " + bottomRight.toString() + ")";
	}

	public Vector2I centerGet() {
		return sizeGet().divide(new Vector2I(2, 2)).add(topLeft);
	}
	
	public void translate(Vector2I offset) {
		topLeft = topLeft.add(offset);
		bottomRight = bottomRight.add(offset);
	}
}