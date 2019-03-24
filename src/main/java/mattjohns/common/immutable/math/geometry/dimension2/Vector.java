package mattjohns.common.immutable.math.geometry.dimension2;

import mattjohns.common.immutable.Immutable;

/**
 * 
 * 2 dimensional vector base class.
 *
 * @param <TNumber>
 * The type of number used for each dimension.
 * 
 * @param <TConcrete>
 * The concrete subclass.
 */
public abstract class Vector<TNumber extends Comparable<TNumber>, TConcrete extends Vector<TNumber, TConcrete>>
		extends Immutable<TConcrete> implements Comparable<Vector<TNumber, ?>> {
	public final TNumber x;
	public final TNumber y;

	protected Vector(TNumber x, TNumber y) {
		this.x = x;
		this.y = y;
	}

	protected abstract TConcrete copy(TNumber x, TNumber y);

	public TConcrete withX(TNumber x) {
		return copy(x, y);
	}

	public TConcrete withY(TNumber y) {
		return copy(x, y);
	}

	public TConcrete withPositiveX() {
		if (x.compareTo(dimensionZero()) < 0) {
			return withX(dimensionNegate(x));
		}
		else {
			return concreteThis();
		}
	}

	public TConcrete withPositiveY() {
		if (y.compareTo(dimensionZero()) < 0) {
			return withY(dimensionNegate(y));
		}
		else {
			return concreteThis();
		}
	}

	public TConcrete withPositive() {
		return withPositiveX().withPositiveY();
	}

	public TConcrete withTranslateX(TNumber delta) {
		return withX(dimensionTranslate(x, delta));
	}

	public TConcrete withTranslateY(TNumber delta) {
		return withY(dimensionTranslate(y, delta));
	}

	public TConcrete withTranslate(TNumber xDelta, TNumber yDelta) {
		return withTranslateX(xDelta).withTranslateY(yDelta);
	}

	public TConcrete withTranslate(Vector<TNumber, ?> delta) {
		return withTranslate(delta.x, delta.y);
	}

	public TConcrete withSubtract(Vector<TNumber, ?> delta) {
		return withTranslate(dimensionNegate(delta.x), dimensionNegate((delta.y)));
	}

	public TConcrete withScaleX(TNumber factor) {
		return withX(dimensionScale(x, factor));
	}

	public TConcrete withScaleY(TNumber factor) {
		return withY(dimensionScale(y, factor));
	}

	public TConcrete withScale(TNumber factor) {
		return withScaleX(factor).withScaleY(factor);
	}

	public TConcrete withScale(Vector<TNumber, ?> factor) {
		return withScaleX(factor.x).withScaleY(factor.y);
	}

	public TConcrete withNegateX() {
		return withX(dimensionNegate(x));
	}

	public TConcrete withNegateY() {
		return withY(dimensionNegate(y));
	}

	public TConcrete withNegate() {
		return withNegateX().withNegateY();
	}

	public boolean isAboveMinimum(Vector<TNumber, ?> minimum) {
		return concreteThis().compareTo(withMinimum(minimum)) == 0;
	}

	public TConcrete withMinimum(Vector<TNumber, ?> minimum) {
		assert minimum != null;

		return withMinimumX(minimum.x).withMinimumY(minimum.y);
	}

	public TConcrete withMinimumX(TNumber minimum) {
		if (x.compareTo(minimum) > 0) {
			return withX(minimum);
		}
		else {
			return concreteThis();
		}
	}

	public TConcrete withMinimumY(TNumber minimum) {
		if (y.compareTo(minimum) > 0) {
			return withY(minimum);
		}
		else {
			return concreteThis();
		}
	}

	public TConcrete withMaximum(Vector<TNumber, ?> maximum) {
		assert maximum != null;

		return withMaximumX(maximum.x).withMaximumY(maximum.y);
	}

	public TConcrete withMaximumX(TNumber maximum) {
		if (x.compareTo(maximum) < 0) {
			return withX(maximum);
		}
		else {
			return concreteThis();
		}
	}

	public TConcrete withMaximumY(TNumber maximum) {
		if (y.compareTo(maximum) < 0) {
			return withY(maximum);
		}
		else {
			return concreteThis();
		}
	}

	@Override
	public int compareTo(Vector<TNumber, ?> item) {
		int xResult = x.compareTo(item.x);
		if (xResult != 0) {
			return xResult;
		}

		int yResult = y.compareTo(item.y);
		if (yResult != 0) {
			return yResult;
		}

		return 0;
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	/**
	 * Both elements are positive.
	 */
	public boolean isPositive() {
		if (x.compareTo(dimensionZero()) < 0) {
			return false;
		}

		if (y.compareTo(dimensionZero()) < 0) {
			return false;
		}

		return true;
	}

	/**
	 * One or both elements are 0.
	 */
	public boolean isFlat() {
		if (x.compareTo(dimensionZero()) == 0) {
			return true;
		}

		if (y.compareTo(dimensionZero()) == 0) {
			return true;
		}

		return false;
	}

	public boolean isPositiveNonFlat() {
		return isPositive() && !isFlat();
	}

	public boolean isZero() {
		if (x.compareTo(dimensionZero()) != 0) {
			return false;
		}

		if (y.compareTo(dimensionZero()) != 0) {
			return false;
		}

		return true;
	}

	public abstract double length();

	public abstract TNumber dimensionZero();

	public abstract TNumber dimensionOne();

	public abstract TNumber dimensionNegate(TNumber item);

	public abstract TNumber dimensionTranslate(TNumber item, TNumber delta);

	public abstract TNumber dimensionScale(TNumber item, TNumber factor);

	public TNumber lengthSquare() {
		TNumber xSquare = dimensionScale(x, x);
		TNumber ySquare = dimensionScale(y, y);

		return dimensionTranslate(xSquare, ySquare);
	}

	/**
	 * Checks if the given item is less than or equal to this vector. Both axis
	 * must be less than or equal.
	 */
	public boolean isContain(TConcrete item) {
		return (item.x.compareTo(x) <= 0) && (item.y.compareTo(y) <= 0);
	}
}