package mattjohns.common.immutable.math.geometry.dimension1;

import mattjohns.common.immutable.Immutable;

/**
 * Both start and end values are zero or positive.
 * 
 * End is exclusive.
 */
public final class RangeIntegerPositive extends RangeExclusive<Integer, RangeIntegerPositive> {
	// Be careful not to use this for a comparison because an empty range from 0
	// to 0 is different than the empty range 7 to 7, for example.
	public static RangeIntegerPositive Zero = RangeIntegerPositive.of(0, 0);

	protected RangeIntegerPositive(Integer start, Integer end) {
		super(start, end);

		assert this.start >= 0;
		assert this.end >= 0;
	}

	public static RangeIntegerPositive of(Integer start, Integer end) {
		return new RangeIntegerPositive(start, end);
	}

	@Override
	protected final RangeIntegerPositive copy(Integer start, Integer end) {
		return new RangeIntegerPositive(start, end);
	}

	@Override
	protected RangeIntegerPositive concreteCopy(Immutable<?> source) {
		return copy(start, end);
	}

	@Override
	public Integer size() {
		return end - start;
	}
}
