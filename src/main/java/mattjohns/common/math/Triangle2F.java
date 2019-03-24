package mattjohns.common.math;

public class Triangle2F {
	private Vector2F vertex1;
	private Vector2F vertex2;
	private Vector2F vertex3;

	public Triangle2F(Vector2F vertex1, Vector2F vertex2, Vector2F vertex3) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.vertex3 = vertex3;
	}

	public float areaGet() {
		// Heron's formula
		float edgeSize1 = vertex1.subtract(vertex2).magnitude();
		float edgeSize2 = vertex2.subtract(vertex3).magnitude();
		float edgeSize3 = vertex3.subtract(vertex1).magnitude();

		float semiperimeter = (edgeSize1 + edgeSize2 + edgeSize3) / 2f;

		float square = semiperimeter * (semiperimeter - edgeSize1) * (semiperimeter - edgeSize2)
				* (semiperimeter - edgeSize3);

		// Can sometimes be slightly negative if triangle has zero area, due to
		// floating point precision.  Get rid of this check if you need high performance
		// although abs() is a very fast CPU instruction.
		square = Math.abs(square);

		return (float)Math.sqrt(square);
	}
}
