package mattjohns.common.immutable.math.geometry.dimension1;

/**
 * End is exclusive but start is still inclusive.
 * 
 * Exclusive lists are preferred over inclusive because they allow you to
 * specific a range of zero. An inclusive list can only have a range of 1 or
 * greater. That is why you see them used often in string operations like
 * subset().
 */
public abstract class RangeExclusive<T extends Comparable<T>, TConcrete extends RangeExclusive<T, TConcrete>>
		extends Range<T, TConcrete> {

	protected RangeExclusive(T start, T end) {
		super(start, end);
	}

	@Override
	public boolean isContain(T item) {
		assert item != null;

		if (item.compareTo(start) < 0) {
			return false;
		}

		// exclusive
		if (item.compareTo(end) >= 0) {
			return false;
		}

		return true;
	}
}
