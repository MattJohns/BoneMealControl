package mattjohns.common.immutable.math.geometry.dimension1;

/**
 * End is inclusive.
 */
public abstract class RangeInclusive<T extends Comparable<T>, TConcrete extends RangeInclusive<T, TConcrete>>
		extends Range<T, TConcrete> {
	protected RangeInclusive(T start, T end) {
		super(start, end);
	}

	@Override
	public boolean isContain(T item) {
		assert item != null;

		if (item.compareTo(start) < 0) {
			return false;
		}

		// inclusive
		if (item.compareTo(end) > 0) {
			return false;
		}

		return true;
	}
}
