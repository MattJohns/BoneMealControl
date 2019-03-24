package mattjohns.common.immutable.math.geometry.dimension1;

import mattjohns.common.immutable.Immutable;

/**
 * End is exclusive.
 */
public final class RangeInteger extends RangeExclusive<Integer, RangeInteger> {
	protected RangeInteger(Integer start, Integer end) {
		super(start, end);
	}

	public static RangeInteger of(Integer start, Integer end) {
		return new RangeInteger(start, end);
	}

	@Override
	protected final RangeInteger copy(Integer start, Integer end) {
		return new RangeInteger(start, end);
	}

	@Override
	protected RangeInteger concreteCopy(Immutable<?> source) {
		return copy(start, end);
	}

	@Override
	public Integer size() {
		return end - start;
	}
}
