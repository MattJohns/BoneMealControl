package mattjohns.common.math;

/**
 * 3 dimensional floating point vector. Update Vector3D with any changes in this
 * class. They are only separate because java does not support generics that use
 * value types.
 */
public class Vector3F {
	public static final Vector3F ZERO = new Vector3F(0f, 0f, 0f);

	public static final Vector3F UNIT_X = new Vector3F(1f, 0f, 0f);
	public static final Vector3F UNIT_Y = new Vector3F(0f, 1f, 0f);
	public static final Vector3F UNIT_Z = new Vector3F(0f, 0f, 1f);

	public static final Vector3F FORWARD = UNIT_X;
	public static final Vector3F BACKWARD = UNIT_X.multiply(-1f);
	public static final Vector3F RIGHT = UNIT_Z;
	public static final Vector3F LEFT = UNIT_Z.multiply(-1f);
	public static final Vector3F UP = UNIT_Y;
	public static final Vector3F DOWN = UNIT_Y.multiply(-1f);

	public float x;
	public float y;
	public float z;

	public Vector3F() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
	}

	public Vector3F(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3F(Vector3F item) {
		this(item.x, item.y, item.z);
	}

	public static Vector3F convertFrom(Vector2F item) {
		return new Vector3F(item.x, item.y, 0f);
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public Vector3F add(float x, float y, float z) {
		return add(new Vector3F(x, y, z));
	}

	public Vector3F add(Vector3F item) {
		return new Vector3F(x + item.x, y + item.y, z + item.z);
	}

	public Vector3F subtract(Vector3F item) {
		return new Vector3F(x - item.x, y - item.y, z - item.z);
	}

	public Vector3F multiply(float item) {
		return new Vector3F(x * item, y * item, z * item);
	}

	public Vector3F multiply(Vector3F item) {
		return new Vector3F(x * item.x, y * item.y, z * item.z);
	}

	public Vector3F divide(float item) {
		if (General.isNearlyZero(item))
			return new Vector3F(ZERO);

		return new Vector3F(x / item, y / item, z / item);
	}

	public Vector3F divide(Vector3F item) {
		return new Vector3F(x / item.x, y / item.y, z / item.z);
	}

	public float magnitude() {
		float square = (float)(x * x) + (float)(y * y) + (float)(z * z);
		if (General.isNearlyZero(square))
			return 0f;

		return (float)Math.sqrt(square);
	}

	public Vector3F unit() {
		return this.divide(magnitude());
	}

	public float dotProduct(Vector3F vector2) {
		return (x * vector2.x) + (y * vector2.y) + (z * vector2.z);
	}

	public float angle(Vector3F vector2) {
		float dotProduct = dotProduct(vector2);

		float magnitude1 = magnitude();
		float magnitude2 = vector2.magnitude();

		float denominator = (magnitude1 * magnitude2);
		if (General.isNearlyZero(denominator))
			return 0f; // probably should be an exception, i'm not sure

		float dividedDot = dotProduct / denominator;

		return (float)Math.acos((double)dividedDot);
	}

	public Vector3F crossProduct(Vector3F vector2) {
		Vector3F returnValue = new Vector3F((y * vector2.z) - (z * vector2.y), (z * vector2.x) - (x * vector2.z),
				(x * vector2.y) - (y * vector2.x));

		return returnValue;
	}

	public Vector3F transform(QuaternionF rotation) {
		float x = rotation.x + rotation.x;
		float y = rotation.y + rotation.y;
		float z = rotation.z + rotation.z;
		float wx = rotation.w * x;
		float wy = rotation.w * y;
		float wz = rotation.w * z;
		float xx = rotation.x * x;
		float xy = rotation.x * y;
		float xz = rotation.x * z;
		float yy = rotation.y * y;
		float yz = rotation.y * z;
		float zz = rotation.z * z;

		float newX = ((this.x * ((1.0f - yy) - zz)) + (this.y * (xy - wz))) + (this.z * (xz + wy));
		float newY = ((this.x * (xy + wz)) + (this.y * ((1.0f - xx) - zz))) + (this.z * (yz - wx));
		float newZ = ((this.x * (xz - wy)) + (this.y * (yz + wx))) + (this.z * ((1.0f - xx) - yy));

		return new Vector3F(newX, newY, newZ);
	}

	public Vector3F magnitudeClamp(float sizeMaximum) {
		float magnitudeCurrent = magnitude();

		if (magnitudeCurrent > sizeMaximum) {
			float scaleFactor = sizeMaximum / magnitudeCurrent;

			return multiply(scaleFactor);
		}
		else {
			return new Vector3F(this);
		}
	}

	public float projectScalar(Vector3F destination) {
		return dotProduct(destination.unit());
	}

	public Vector3F projectVector(Vector3F destination) {
		float scalar = projectScalar(destination);

		return destination.unit().multiply(scalar);
	}
}