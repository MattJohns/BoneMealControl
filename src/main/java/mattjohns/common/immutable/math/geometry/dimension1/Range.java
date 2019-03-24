package mattjohns.common.immutable.math.geometry.dimension1;

import mattjohns.common.immutable.Immutable;

/**
 * Start must be less than or equal to end (i.e. size must be positive).
 */
public abstract class Range<T extends Comparable<T>, TConcrete extends Range<T, TConcrete>>
		extends Immutable<TConcrete> {
	public final T start;
	public final T end;

	protected Range(T start, T end) {
		this.start = start;
		this.end = end;

		assert this.start != null;
		assert this.end != null;
		assert this.start.compareTo(this.end) <= 0 : "Range is negative.";
	}

	protected abstract TConcrete copy(T start, T end);

	public TConcrete withStart(T start) {
		return copy(start, end);
	}

	public TConcrete withEnd(T end) {
		return copy(start, end);
	}

	public abstract T size();

	public abstract boolean isContain(T item);
}
