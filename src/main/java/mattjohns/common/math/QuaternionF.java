package mattjohns.common.math;

/**
 * Quaternion that handles all the different coordinate systems. For example
 * Minecraft requires rotations in y, z, x .
 * <p>
 * If you are suffering rotation problems then ensure the rotation sequence is
 * correct for your particular game's coordinate system. If there is a mismatch
 * you will notice it when converting a quaternion into x, y, z Euler angles.
 */
public class QuaternionF {
	public static final QuaternionF ZERO = new QuaternionF(0f, 0f, 0f, 0f);
	public static final QuaternionF IDENTITY = new QuaternionF(0.0f, 0.0f, 0.0f, 1.0f);

	/**
	 * Sequence of axis rotation when converting from quaternion to Euler
	 * angles. It is important to use the right sequence for your particular
	 * coordinate system.
	 */
	public enum RotSeq {
		zyx,
		zyz,
		zxy,
		zxz,
		yxz,
		yxy,
		yzx,
		yzy,
		xyz,
		xyx,
		xzy,
		xzx
	};

	public float w;
	public float x;
	public float y;
	public float z;

	public QuaternionF() {
	}

	public QuaternionF(float w, float x, float y, float z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public QuaternionF(QuaternionF item) {
		w = item.w;
		x = item.x;
		y = item.y;
		z = item.z;
	}

	public QuaternionF(Vector3F vector, float w) {
		this.w = w;
		x = vector.x;
		y = vector.y;
		z = vector.z;
	}

	public boolean isIdentity() {
		return this.equals(IDENTITY);
	}

	public boolean isNormalized() {
		return General.isNearlyEqual(1f, (w * w) + (x * x) + (y * y) + (z * z));
	}

	public float angle() {
		float length = (x * x) + (y * y) + (z * z);
		if (General.isNearlyZero(length))
			return 0.0f;

		return (float)(2d * Math.acos((double)General.ClampF(w, -1f, 1f)));
	}

	public Vector3F axis() {
		float length = (x * x) + (y * y) + (z * z);
		if (General.isNearlyZero(length))
			return Vector3F.UNIT_X;

		float inv = 1.0f / (float)Math.sqrt((double)length);
		return new Vector3F(x * inv, y * inv, z * inv);
	}

	/**
	 * Get w, x, y or z by index rather than by name. Returns 0 if index is out
	 * of range, otherwise would have to use exceptions.
	 */
	public float componentGet(int index) {
		switch (index) {
		case 0:
			return w;
		case 1:
			return x;
		case 2:
			return y;
		case 3:
			return z;
		default:
			return 0;
		}
	}

	/**
	 * Set w, x, y or z by index rather than by value. Does nothing if index is
	 * out of range.
	 */
	public void componentSet(int index, float item) {
		switch (index) {
		case 0:
			w = item;
			break;
		case 1:
			x = item;
			break;
		case 2:
			y = item;
			break;
		case 3:
			z = item;
			break;
		default:
			break;
		}
	}

	public QuaternionF invert() {
		float lengthSq = lengthSquared();
		if (General.isNearlyZero(lengthSq))
			return new QuaternionF(this);

		lengthSq = 1.0f / lengthSq;

		float newW = w * lengthSq;
		float newX = -x * lengthSq;
		float newY = -y * lengthSq;
		float newZ = -z * lengthSq;

		return new QuaternionF(newW, newX, newY, newZ);
	}

	public float length() {
		return (float)Math.sqrt((w * w) + (x * x) + (y * y) + (z * z));
	}

	private float lengthSquared() {
		return (w * w) + (x * x) + (y * y) + (z * z);
	}

	public QuaternionF normalize() {
		float length = this.length();
		if (General.isNearlyZero(length))
			return new QuaternionF(this);

		float inverse = 1.0f / length;

		QuaternionF returnValue = new QuaternionF(w * inverse, x * inverse, y * inverse, z * inverse);

		return returnValue;
	}

	public QuaternionF add(QuaternionF item) {
		float newW = w + item.w;
		float newX = x + item.x;
		float newY = y + item.y;
		float newZ = z + item.z;

		return new QuaternionF(newW, newX, newY, newZ);
	}

	public QuaternionF subtract(QuaternionF item) {
		float newW = w - item.w;
		float newX = x - item.x;
		float newY = y - item.y;
		float newZ = z - item.z;

		return new QuaternionF(newW, newX, newY, newZ);
	}

	public QuaternionF multiply(float scale) {
		float newW = w * scale;
		float newX = x * scale;
		float newY = y * scale;
		float newZ = z * scale;

		return new QuaternionF(newW, newX, newY, newZ);
	}

	public QuaternionF multiply(QuaternionF item) {
		float lw = w;
		float lx = x;
		float ly = y;
		float lz = z;

		float rw = item.w;
		float rx = item.x;
		float ry = item.y;
		float rz = item.z;

		float d = (lx * rx + ly * ry + lz * rz);
		float a = (ly * rz - lz * ry);
		float b = (lz * rx - lx * rz);
		float c = (lx * ry - ly * rx);

		float newW = lw * rw - d;
		float newX = (lx * rw + rx * lw) + a;
		float newY = (ly * rw + ry * lw) + b;
		float newZ = (lz * rw + rz * lw) + c;

		return new QuaternionF(newW, newX, newY, newZ);
	}

	public QuaternionF negate() {
		return multiply(-1f);
	}

	public static QuaternionF barycentric(QuaternionF value1, QuaternionF value2, QuaternionF value3, float amount1,
			float amount2) {
		QuaternionF result = new QuaternionF();
		QuaternionF start, end;

		start = slerp(value1, value2, amount1 + amount2);
		end = slerp(value1, value3, amount1 + amount2);
		result = slerp(start, end, amount2 / (amount1 + amount2));

		return result;
	}

	public QuaternionF conjugate() {
		return new QuaternionF(w, x * -1f, y * -1f, z * -1f);
	}

	public float dotProduct(QuaternionF item) {
		return (w * item.w) + (x * item.x) + (y * item.y) + (z * item.z);
	}

	public QuaternionF exponential() {
		float angle = (float)Math.sqrt((x * x) + (y * y) + (z * z));
		float sin = (float)Math.sin(angle);

		float newW;
		float newX;
		float newY;
		float newZ;

		if (General.isNearlyZero(sin))
			return new QuaternionF(this);

		float coeff = sin / angle;
		newW = (float)Math.cos(angle);
		newX = coeff * x;
		newY = coeff * y;
		newZ = coeff * z;

		return new QuaternionF(newW, newX, newY, newZ);
	}

	// Value between 0 and 1 indicating the weight of 'end'
	public static QuaternionF lerp(QuaternionF start, QuaternionF end, float amount) {
		QuaternionF result = new QuaternionF();

		float inverse = 1.0f - amount;

		if (start.dotProduct(end) >= 0.0f) {
			result.w = (inverse * start.w) + (amount * end.w);
			result.x = (inverse * start.x) + (amount * end.x);
			result.y = (inverse * start.y) + (amount * end.y);
			result.z = (inverse * start.z) + (amount * end.z);
		}
		else {
			result.w = (inverse * start.w) - (amount * end.w);
			result.x = (inverse * start.x) - (amount * end.x);
			result.y = (inverse * start.y) - (amount * end.y);
			result.z = (inverse * start.z) - (amount * end.z);
		}

		return result.normalize();
	}

	public QuaternionF logarithm() {
		if ((float)Math.abs(w) >= 1f)
			return new QuaternionF(this);

		float angle = (float)Math.acos(w);
		float sin = (float)Math.sin(angle);
		if (General.isNearlyZero(sin))
			return new QuaternionF(this);

		float coeff = angle / sin;

		float newX = x * coeff;
		float newY = y * coeff;
		float newZ = z * coeff;
		return new QuaternionF(0f, newX, newY, newZ);
	}

	public static QuaternionF createFromAxisAndAngle(Vector3F axis, float angle) {
		Vector3F unit = axis.unit();

		float half = angle * 0.5f;
		float sin = (float)Math.sin(half);
		float cos = (float)Math.cos(half);

		float newW = cos;
		float newX = unit.x * sin;
		float newY = unit.y * sin;
		float newZ = unit.z * sin;

		QuaternionF returnValue = new QuaternionF(newW, newX, newY, newZ);

		return returnValue.normalize();
	}

	public static QuaternionF slerp(QuaternionF start, QuaternionF end, float amount) {
		QuaternionF returnValue = new QuaternionF();

		float opposite;
		float inverse;
		float dot = start.dotProduct(end);

		if (Math.abs(dot) > 1.0f - General.CLOSE_ENOUGH_F) {
			inverse = 1.0f - amount;
			opposite = amount * Math.signum(dot);
		}
		else {
			float acos = (float)Math.acos(Math.abs(dot));
			float invSin = (float)(1.0 / Math.sin(acos));

			inverse = (float)Math.sin((1.0f - amount) * acos) * invSin;
			opposite = (float)Math.sin(amount * acos) * invSin * Math.signum(dot);
		}

		returnValue.w = (inverse * start.w) + (opposite * end.w);
		returnValue.x = (inverse * start.x) + (opposite * end.x);
		returnValue.y = (inverse * start.y) + (opposite * end.y);
		returnValue.z = (inverse * start.z) + (opposite * end.z);

		return returnValue;
	}

	public static QuaternionF squad(QuaternionF value1, QuaternionF value2, QuaternionF value3, QuaternionF value4,
			float amount) {
		QuaternionF returnValue = new QuaternionF();

		QuaternionF start, end;
		start = slerp(value1, value4, amount);
		end = slerp(value2, value3, amount);
		returnValue = slerp(start, end, 2.0f * amount * (1.0f - amount));

		return returnValue;
	}

	public boolean equals(QuaternionF other) {
		return General.isNearlyEqual(other.w, w) && General.isNearlyEqual(other.x, x)
				&& General.isNearlyEqual(other.y, y) && General.isNearlyEqual(other.z, z);
	}

	/**
	 * Get yaw, pitch and roll. Compatible with Minecraft coordinate system. For
	 * other coordinate systems it will be necessary to call
	 * QuaternionF.quaternion2Euler() directly with the appropriate rotation
	 * sequence.
	 */
	public TaitBryanAngleF taitBryanGetZyx() {
		Vector3F returnValue = QuaternionF.quaternion2Euler(this, RotSeq.yzx);

		return new TaitBryanAngleF(returnValue.z, returnValue.y, returnValue.x);
	}

	public Vector3F directionVectorX() {
		return Vector3F.UNIT_X.transform(this);
	}

	public Vector3F directionVectorY() {
		return Vector3F.UNIT_Y.transform(this);
	}

	public Vector3F directionVectorZ() {
		return Vector3F.UNIT_Z.transform(this);
	}

	public static QuaternionF createFromVector(Vector3F sourcePoint, Vector3F destPoint) {
		Vector3F forwardVector = destPoint.subtract(sourcePoint).unit();

		float dot = Vector3F.FORWARD.dotProduct(forwardVector);

		if (General.isNearlyZero(Math.abs(dot + 1f))) {
			return createFromAxisAndAngle(Vector3F.UP, General.PI_F);
		}

		if (General.isNearlyZero(Math.abs(dot - 1f))) {
			return QuaternionF.IDENTITY;
		}

		float rotAngle = (float)Math.acos(dot);
		Vector3F rotAxis = Vector3F.FORWARD.crossProduct(forwardVector);
		rotAxis = rotAxis.unit();
		return createFromAxisAndAngle(rotAxis, rotAngle);
	}

	static Vector3F twoaxisrot(float r11, float r12, float r21, float r31, float r32) {
		Vector3F ret = new Vector3F(Vector3F.ZERO);
		ret.x = (float)Math.atan2(r11, r12);
		ret.y = (float)Math.acos(r21);
		ret.z = (float)Math.atan2(r31, r32);
		return ret;
	}

	static Vector3F threeaxisrot(float r11, float r12, float r21, float r31, float r32) {
		Vector3F ret = new Vector3F(Vector3F.ZERO);
		ret.x = (float)Math.atan2(r31, r32);
		ret.y = (float)Math.asin(r21);
		ret.z = (float)Math.atan2(r11, r12);
		return ret;
	}

	/**
	 * Converts the quaternion to an x, y, z Euler set of rotations.
	 * 
	 * @param q
	 * The quaternion to convert.
	 * 
	 * @param rotSeq
	 * The sequence of rotations to use for the Euler angles. Ensure these match
	 * your coordinate system.
	 * 
	 * @return A vector with x, y and z holding a rotation in radians. Returns
	 * Vector3F.ZERO if invalid rotation sequence is given.
	 */
	public static Vector3F quaternion2Euler(QuaternionF q, RotSeq rotSeq) {
		switch (rotSeq) {
		case zyx:
			return threeaxisrot(2 * (q.x * q.y + q.w * q.z), q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z,
					-2 * (q.x * q.z - q.w * q.y), 2 * (q.y * q.z + q.w * q.x),
					q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z);

		case zyz:
			return twoaxisrot(2 * (q.y * q.z - q.w * q.x), 2 * (q.x * q.z + q.w * q.y),
					q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z, 2 * (q.y * q.z + q.w * q.x),
					-2 * (q.x * q.z - q.w * q.y));

		case zxy:
			return threeaxisrot(-2 * (q.x * q.y - q.w * q.z), q.w * q.w - q.x * q.x + q.y * q.y - q.z * q.z,
					2 * (q.y * q.z + q.w * q.x), -2 * (q.x * q.z - q.w * q.y),
					q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z);

		case zxz:
			return twoaxisrot(2 * (q.x * q.z + q.w * q.y), -2 * (q.y * q.z - q.w * q.x),
					q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z, 2 * (q.x * q.z - q.w * q.y),
					2 * (q.y * q.z + q.w * q.x));

		case yxz:
			return threeaxisrot(2 * (q.x * q.z + q.w * q.y), q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z,
					-2 * (q.y * q.z - q.w * q.x), 2 * (q.x * q.y + q.w * q.z),
					q.w * q.w - q.x * q.x + q.y * q.y - q.z * q.z);

		case yxy:
			return twoaxisrot(2 * (q.x * q.y - q.w * q.z), 2 * (q.y * q.z + q.w * q.x),
					q.w * q.w - q.x * q.x + q.y * q.y - q.z * q.z, 2 * (q.x * q.y + q.w * q.z),
					-2 * (q.y * q.z - q.w * q.x));

		case yzx:
			return threeaxisrot(-2 * (q.x * q.z - q.w * q.y), q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z,
					2 * (q.x * q.y + q.w * q.z), -2 * (q.y * q.z - q.w * q.x),
					q.w * q.w - q.x * q.x + q.y * q.y - q.z * q.z);

		case yzy:
			return twoaxisrot(2 * (q.y * q.z + q.w * q.x), -2 * (q.x * q.y - q.w * q.z),
					q.w * q.w - q.x * q.x + q.y * q.y - q.z * q.z, 2 * (q.y * q.z - q.w * q.x),
					2 * (q.x * q.y + q.w * q.z));

		case xyz:
			return threeaxisrot(-2 * (q.y * q.z - q.w * q.x), q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z,
					2 * (q.x * q.z + q.w * q.y), -2 * (q.x * q.y - q.w * q.z),
					q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z);

		case xyx:
			return twoaxisrot(2 * (q.x * q.y + q.w * q.z), -2 * (q.x * q.z - q.w * q.y),
					q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z, 2 * (q.x * q.y - q.w * q.z),
					2 * (q.x * q.z + q.w * q.y));

		case xzy:
			return threeaxisrot(2 * (q.y * q.z + q.w * q.x), q.w * q.w - q.x * q.x + q.y * q.y - q.z * q.z,
					-2 * (q.x * q.y - q.w * q.z), 2 * (q.x * q.z + q.w * q.y),
					q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z);

		case xzx:
			return twoaxisrot(2 * (q.x * q.z - q.w * q.y), 2 * (q.x * q.y + q.w * q.z),
					q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z, 2 * (q.x * q.z + q.w * q.y),
					-2 * (q.x * q.y - q.w * q.z));

		default:
			return Vector3F.ZERO;
		}
	}
}
