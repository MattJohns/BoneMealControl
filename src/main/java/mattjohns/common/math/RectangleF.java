package mattjohns.common.math;

/**
 * Stores all 4 corners as positions rather than width and height.
 */
public class RectangleF {
	private Vector2F topLeft;
	private Vector2F bottomRight;

	public RectangleF() {
		topLeft = Vector2F.ZERO;
		bottomRight = Vector2F.ZERO;
	}

	public RectangleF(Vector2F topLeft, Vector2F bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}

	public RectangleF(RectangleF item) {
		topLeft = new Vector2F(item.topLeftGet());
		bottomRight = new Vector2F(item.bottomRightGet());
	}

	public static RectangleF createFromPositionAndSize(Vector2F position, Vector2F size) {
		return new RectangleF(position, position.add(size));
	}

	public Vector2F topLeftGet() {
		return topLeft;
	}

	public void topLeftSet(Vector2F item) {
		topLeft = new Vector2F(item);
	}

	public Vector2F bottomRightGet() {
		return bottomRight;
	}

	public void bottomRightSet(Vector2F item) {
		bottomRight = new Vector2F(item);
	}

	public Vector2F sizeGet() {
		return bottomRight.subtract(topLeft);
	}

	public String toString() {
		return "(" + topLeft.toString() + ", " + bottomRight.toString() + ")";
	}

	public RectangleF multiply(Vector2F item) {
		return new RectangleF(topLeft.multiply(item), bottomRight.multiply(item));
	}

	public Vector2F centerGet() {
		return sizeGet().divide(new Vector2F(2f, 2f)).add(topLeft);
	}
}