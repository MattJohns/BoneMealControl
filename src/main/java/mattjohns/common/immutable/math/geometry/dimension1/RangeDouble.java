package mattjohns.common.immutable.math.geometry.dimension1;

import mattjohns.common.immutable.Immutable;

/**
 * End is inclusive.
 */
public final class RangeDouble extends RangeInclusive<Double, RangeDouble> {
	public static final RangeDouble Unit = RangeDouble.of(0d, 1d);

	protected RangeDouble(Double start, Double end) {
		super(start, end);
	}

	public static RangeDouble of(Double start, Double end) {
		return new RangeDouble(start, end);
	}

	@Override
	protected final RangeDouble copy(Double start, Double end) {
		return new RangeDouble(start, end);
	}

	@Override
	protected RangeDouble concreteCopy(Immutable<?> source) {
		return copy(start, end);
	}

	@Override
	public Double size() {
		return end - start;
	}
}
