package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;

/**
 * End location is exclusive.
 */
abstract class LocationRange<TConcrete extends LocationRange<TConcrete, TLocation>, TLocation extends Location<TLocation>>
		extends Immutable<TConcrete> {
	public final TLocation start;
	public final TLocation end;

	protected abstract TConcrete concreteThis();

	protected abstract TConcrete copy(TLocation start, TLocation end);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(start, end);
	}

	public LocationRange(TLocation start, TLocation end) {
		this.start = start;
		this.end = end;

		assert start != null;
		assert end != null;

		assert this.start.compareTo(this.end) <= 0 : "Negative location range.";
	}

	public TConcrete withStart(TLocation start) {
		return copy(start, end);
	}

	public TConcrete withEnd(TLocation end) {
		return copy(start, end);
	}

	public boolean isEmpty() {
		return start.compareTo(end) == 0;
	}

	public boolean contains(TLocation containedItem) {
		assert containedItem != null;

		return (containedItem.compareTo(start) >= 0) && (containedItem.compareTo(end) < 0);
	}
}