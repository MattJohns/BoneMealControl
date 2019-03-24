package mattjohns.common.math;

import java.util.Random;

/**
 * General math utility functions.
 */
public class General {
	// Close enough to zero that it should be considered zero. Needed
	// because doubles and floats can lose precision during calculations
	// and be slightly off from zero, when in fact it should be exactly zero.
	public static float CLOSE_ENOUGH_F = 0.0000000001f;
	public static double CLOSE_ENOUGH_D = (double)CLOSE_ENOUGH_F;

	public static float PI_F = (float)Math.PI;

	private static Random random = new Random();

	public static boolean isNearlyEqual(float item1, float item2) {
		float delta = Math.abs(item1 - item2);
		return (delta <= CLOSE_ENOUGH_F);
	}

	public static boolean isNearlyEqual(double item1, double item2) {
		double delta = Math.abs(item1 - item2);
		return (delta <= CLOSE_ENOUGH_D);
	}

	public static boolean isNearlyZero(float item) {
		return isNearlyEqual(item, 0);
	}

	public static boolean isNearlyZero(double item) {
		return isNearlyEqual(item, 0);
	}

	public static boolean isDoubleLessOrEqual(double item1, double item2) {
		if (isNearlyEqual(item1, item2)) {
			return true;
		}
		
		return item1 < item2;
	}

	public static boolean isDoubleGreaterOrEqual(double item1, double item2) {
		if (isNearlyEqual(item1, item2)) {
			return true;
		}
		
		return item1 > item2;
	}

	public static float ClampF(float item, float minimum, float maximum) {
		float returnValue = item;

		if (returnValue < minimum)
			returnValue = minimum;
		if (returnValue > maximum)
			returnValue = maximum;

		return returnValue;
	}

	public static float convertDegreesToRadians(float item) {
		return (item * PI_F) / 180f;
	}

	public static float convertRadiansToDegrees(float item) {
		return item * (180f / PI_F);
	}

	/**
	 * Converts the given angle to an angle between 0 and 360 degrees. So -30
	 * becomes 330 degrees. 500 becomes 140 degrees.
	 * <p>
	 * All angles are in radians.
	 * 
	 * @param item
	 * Angle that can have any value either inside or outside the 360 degree
	 * range.
	 * 
	 * @return The angle converted to the range 0 to 360 degrees.
	 */
	public static float angleClamp(float item) {
		return angleClamp(item, PI_F * -1f, PI_F);
	}

	// range needs to be symmetrical around zero
	public static float angleClamp(float item, float minimumExclusive, float maximum) {
		float rangeSize = maximum - minimumExclusive;
		float halfRangeSize = rangeSize / 2f;

		float returnValue;
		if (item > maximum) {
			float angle = item + halfRangeSize;

			float remainder = angle % rangeSize;

			returnValue = remainder - halfRangeSize;
		}
		else {
			if (item <= minimumExclusive) {
				float angle = item - halfRangeSize;
				angle *= -1f;

				float remainder = angle % rangeSize;

				returnValue = remainder - halfRangeSize;
				returnValue *= -1f;
			}
			else {
				returnValue = item;
			}
		}

		return returnValue;
	}

	/**
	 * Finds the difference between 2 angles. Chooses the shortest path so that
	 * 100 degrees to 600 degrees is 240 degrees, not 500 degrees.
	 * <p>
	 * All angles are in radians.
	 * 
	 * @param baseAngle
	 * The angle to move from.
	 * 
	 * @param newAngle
	 * The angle to move to.
	 * 
	 * @return The difference between the angles from -180 and +180 degrees
	 * (in radians).
	 */
	public static float angleGetSmallestDelta(float baseAngle, float newAngle) {
		float baseClamped = angleClamp(baseAngle);
		float newClamped = angleClamp(newAngle);

		float delta = newClamped - baseClamped;
		if (Math.abs(delta) > PI_F) {
			if (baseClamped < newClamped) {
				float bottom = baseClamped - (PI_F * -1f);
				float top = PI_F - newClamped;
				delta = bottom + top;

				delta *= -1f;
			}
			else {
				float bottom = newClamped - (PI_F * -1f);
				float top = PI_F - baseClamped;
				delta = bottom + top;

			}
		}

		return delta;
	}

	public static float angleMoveSmallestDistance(float oldAngle, float newAngle) {
		float delta = General.angleGetSmallestDelta(oldAngle, newAngle);
		return oldAngle + delta;
	}

	public static int randomGetIntegerExclusive(int maximumSize) {
		if (maximumSize < 1)
			return 0;

		return random.nextInt(maximumSize);
	}

	// inclusive
	public static int randomGetInteger(int minimum, int maximum) {
		int maximumSize = maximum - minimum;
		if (maximumSize <= 0 ) {
			return 0;
		}
		
		int randomSize = randomGetIntegerExclusive(maximumSize);
		
		int result = randomSize + minimum;
		
		return result;
	}	

	// inclusive
	public static boolean randomChance(double chance) {
		if (chance < 0d) {
			chance = 0d;
		}
		
		if (chance > 1.0) {
			chance = 1.0;
		}
		
		double randomValue = random.nextDouble();
		
		return isDoubleGreaterOrEqual(chance, randomValue);
	}

	/**
	 * Map the given number to the range 0 - 1 .
	 * 
	 * @param item
	 * The number to map.
	 * 
	 * @param oldRangeStart
	 * The start of the old range.
	 * 
	 * @param oldRangeEnd
	 * The end of the old range.
	 * 
	 * @return A value between 0 and 1 that represents the position of the given
	 * number in the old range.
	 * 
	 */
	public static double normalize(double item, double oldRangeStart, double oldRangeEnd) {
		double shiftToZero = item + (oldRangeStart * -1);

		double oldRangeSize = oldRangeEnd - oldRangeStart;
		if (isNearlyZero(oldRangeSize))
			return 0;

		return shiftToZero / oldRangeSize;
	}
}