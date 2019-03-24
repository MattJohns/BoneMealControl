package mattjohns.common.immutable.math.geometry.dimension2;

/**
 * Separated into a base class so different kinds of integer vectors can be derived.  For example
 * a vector that is always positive.
 */
public abstract class VectorIntegerBase<TConcrete extends VectorIntegerBase<TConcrete>>
		extends Vector<Integer, TConcrete> {
	protected VectorIntegerBase(Integer x, Integer y) {
		super(x, y);
	}

	@Override
	public double length() {
		return Math.sqrt(lengthSquare());
	}

	@Override
	public Integer dimensionZero() {
		return 0;
	}

	@Override
	public Integer dimensionNegate(Integer item) {
		return item * -1;
	}

	@Override
	public Integer dimensionTranslate(Integer item, Integer delta) {
		return item + delta;
	}

	@Override
	public Integer dimensionScale(Integer item, Integer factor) {
		return item * factor;
	}
}
