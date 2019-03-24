package mattjohns.common.immutable.math.geometry.dimension2;

import mattjohns.common.immutable.Immutable;

/**
 * Position and size can be negative (or any value).
 */
public final class RectangleInteger extends RectangleIntegerBase<VectorInteger, VectorInteger, RectangleInteger> {
	protected RectangleInteger(VectorInteger topLeft, VectorInteger bottomRight) {
		super(topLeft, bottomRight);
	}

	@Override
	protected final RectangleInteger copy(VectorInteger topLeft, VectorInteger bottomRight) {
		return new RectangleInteger(topLeft, bottomRight);
	}

	@Override
	protected RectangleInteger concreteCopy(Immutable<?> source) {
		return copy(topLeft, bottomRightExclusive);
	}

	@Override
	protected VectorInteger positionCreate(Integer x, Integer y) {
		return VectorInteger.of(x, y);
	}

	@Override
	protected VectorInteger sizeCreate(Integer x, Integer y) {
		return VectorInteger.of(x, y);
	}
}
