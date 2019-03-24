package mattjohns.common.math;

/**
 * 3 dimensional vector of type double.
 */
public class Vector3D {
	public static final Vector3D ZERO = new Vector3D(0f, 0f, 0f);

	public static final Vector3D UNIT_X = new Vector3D(1f, 0f, 0f);
	public static final Vector3D UNIT_Y = new Vector3D(0f, 1f, 0f);
	public static final Vector3D UNIT_Z = new Vector3D(0f, 0f, 1f);

	public static final Vector3D FORWARD = UNIT_X;
	public static final Vector3D BACKWARD = UNIT_X.multiply(-1f);
	public static final Vector3D RIGHT = UNIT_Z;
	public static final Vector3D LEFT = UNIT_Z.multiply(-1f);
	public static final Vector3D UP = UNIT_Y;
	public static final Vector3D DOWN = UNIT_Y.multiply(-1f);

	public double x;
	public double y;
	public double z;

	public Vector3D() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(Vector3D item) {
		this(item.x, item.y, item.z);
	}

	public static Vector3D convertFrom(Vector2F item) {
		return new Vector3D(item.x, item.y, 0f);
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public Vector3D add(double x, double y, double z) {
		return add(new Vector3D(x, y, z));
	}

	public Vector3D add(Vector3D item) {
		return new Vector3D(x + item.x, y + item.y, z + item.z);
	}

	public Vector3D subtract(Vector3D item) {
		return new Vector3D(x - item.x, y - item.y, z - item.z);
	}

	public Vector3D multiply(double item) {
		return new Vector3D(x * item, y * item, z * item);
	}

	public Vector3D multiply(Vector3D item) {
		return new Vector3D(x * item.x, y * item.y, z * item.z);
	}

	public Vector3D divide(double item) {
		if (General.isNearlyZero(item))
			return new Vector3D(ZERO);

		return new Vector3D(x / item, y / item, z / item);
	}

	public Vector3D divide(Vector3D item) {
		return new Vector3D(x / item.x, y / item.y, z / item.z);
	}

	public double magnitude() {
		double square = (double)(x * x) + (double)(y * y) + (double)(z * z);
		if (General.isNearlyZero(square))
			return 0;

		return (double)Math.sqrt(square);
	}

	public Vector3D unit() {
		return this.divide(magnitude());
	}

	public double dotProduct(Vector3D vector2) {
		return (x * vector2.x) + (y * vector2.y) + (z * vector2.z);
	}

	public double angle(Vector3D vector2) {
		double dotProduct = dotProduct(vector2);

		double magnitude1 = magnitude();
		double magnitude2 = vector2.magnitude();

		double denominator = (magnitude1 * magnitude2);
		if (General.isNearlyZero(denominator))
			return 0; // probably should be an exception, i'm not sure

		double dividedDot = dotProduct / denominator;

		return (double)Math.acos((double)dividedDot);
	}

	public Vector3D crossProduct(Vector3D vector2) {
		Vector3D returnValue = new Vector3D((y * vector2.z) - (z * vector2.y), (z * vector2.x) - (x * vector2.z),
				(x * vector2.y) - (y * vector2.x));

		return returnValue;
	}

	public Vector3D transform(QuaternionF rotation) {
		double x = rotation.x + rotation.x;
		double y = rotation.y + rotation.y;
		double z = rotation.z + rotation.z;
		double wx = rotation.w * x;
		double wy = rotation.w * y;
		double wz = rotation.w * z;
		double xx = rotation.x * x;
		double xy = rotation.x * y;
		double xz = rotation.x * z;
		double yy = rotation.y * y;
		double yz = rotation.y * z;
		double zz = rotation.z * z;

		double newX = ((this.x * ((1.0f - yy) - zz)) + (this.y * (xy - wz))) + (this.z * (xz + wy));
		double newY = ((this.x * (xy + wz)) + (this.y * ((1.0f - xx) - zz))) + (this.z * (yz - wx));
		double newZ = ((this.x * (xz - wy)) + (this.y * (yz + wx))) + (this.z * ((1.0f - xx) - yy));

		return new Vector3D(newX, newY, newZ);
	}

	public Vector3D magnitudeClamp(double sizeMaximum) {
		double magnitudeCurrent = magnitude();

		if (magnitudeCurrent > sizeMaximum) {
			double scaleFactor = sizeMaximum / magnitudeCurrent;

			return multiply(scaleFactor);
		} else {
			return new Vector3D(this);
		}
	}

	public double projectScalar(Vector3D destination) {
		return dotProduct(destination.unit());
	}

	public Vector3D projectVector(Vector3D destination) {
		double scalar = projectScalar(destination);

		return destination.unit().multiply(scalar);
	}
}