package mattjohns.common.immutable.list;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;

/**
 * Generic immutable list. Use only when deriving a custom list class. Otherwise
 * use ListImmutable class if you don't need to override anything.
 * 
 * Google's ImmutableList class can't be subclassed because they wanted to
 * ensure the list is guaranteed immutable. So this class has to use composition
 * instead and hold an internal google list.
 * 
 * There are 2 types of indexes used for referencing in these lists. Normal
 * indexes and 'locations'. A location is the same as an index except it may
 * also point to one past the end of the list. Locations are used for defining
 * ranges and other operations.
 * 
 * @param <TElement>
 * The type of element the list holds.
 * 
 * @param <TConcrete>
 * The subclass type.
 */
public abstract class ListImmutableBase<TElement, TConcrete extends ListImmutableBase<TElement, TConcrete>>
		extends Immutable<TConcrete> implements Iterable<TElement> {
	// need to encapsulate Guava list because not allowed to derive from it
	public final ImmutableList<TElement> internalList;

	protected ListImmutableBase(ImmutableList<TElement> internalList) {
		this.internalList = internalList;

		assert this.internalList != null;
	}

	/**
	 * Subclass must override and return a copy of itself with the new internal
	 * list. This method allows base classes to create an instance of the
	 * concrete class.
	 * 
	 * @param internalList
	 * The new internal list to use.
	 */
	protected abstract TConcrete copy(ImmutableList<TElement> internalList);

	@Override
	protected TConcrete concreteCopy(Immutable<?> source) {
		return copy(internalList);
	}
	
	public TConcrete withInternalList(ImmutableList<TElement> internalList) {
		return copy(internalList);
	}

	public TConcrete withClear() {
		return copy(ImmutableList.of());
	}

	/**
	 * Returns all list elements up to the given index location.
	 * 
	 * @param endLocationExclusive
	 * The cut position. Note this is a location not an index. So it can point
	 * to one past the end of the list.
	 * 
	 * It is exclusive meaning this index and everything after it is not
	 * included in the result.
	 */
	public TConcrete withSplitBefore(int endLocationExclusive) {
		locationCheck(endLocationExclusive);

		ListImmutable<TConcrete> result = withSplit(endLocationExclusive);

		return result.start();
	}

	/**
	 * Returns all list elements after the given location.
	 * 
	 * @param startLocationInclusive
	 * The cut position. This is inclusive so the item at this index is
	 * included.
	 * 
	 * The location may point to one past the end of the list in which case an
	 * empty list will be returned.
	 */
	public TConcrete withSplitAfter(int startLocationInclusive) {
		ListImmutable<TConcrete> result = withSplit(startLocationInclusive);

		return result.end();
	}

	/**
	 * Splits the list into 2 parts.
	 * 
	 * @param location
	 * The cut location. The element at this index will be in the 2nd list
	 * returned.
	 * 
	 * Location may be one past end of list in which case the 2nd list will be
	 * empty.
	 */
	public ListImmutable<TConcrete> withSplit(int location) {
		assert locationCheck(location);

		TConcrete before;
		TConcrete after;

		if (location == 0) {
			// split at start
			before = copy(ImmutableList.of());
			after = concreteThis();
		}
		else {
			if (location == size()) {
				// split at end
				before = concreteThis();
				after = copy(ImmutableList.of());
			}
			else {
				// normal
				before = withSubset(0, location);
				assert before != null;

				after = withSubset(location, size());
				assert after != null;

			}
		}

		ListImmutable.Builder<TConcrete> builder = ListImmutable.Builder.of();

		return builder.add(before)
				.add(after)
				.build();
	}

	public TConcrete withSubset(RangeIntegerPositive range) {
		return withSubset(range.start, range.end);
	}

	/**
	 * Gets a part of the list.
	 * 
	 * @param locationStart
	 * Inclusive start location.
	 * 
	 * @param locationEndExclusive
	 * Exclusive end location.
	 */
	public TConcrete withSubset(int locationStart, int locationEndExclusive) {
		assert locationCheck(locationStart);
		assert locationCheck(locationEndExclusive);
		assert locationStart <= locationEndExclusive;

		return withInternalList(internalList.subList(locationStart, locationEndExclusive));
	}

	/**
	 * Removes the item from the list. Item must exist.
	 */
	public TConcrete withRemoveItem(TElement item) {
		assert item != null;
		assert contains(item);

		int oldSize = size();

		TConcrete result = withInternalList(internalList.stream()
				.filter(existingItem -> !(existingItem == item))
				.collect(collector()));

		assert result.size() == oldSize - 1;

		return result;
	}

	public TConcrete withRemoveByLocationRange(RangeIntegerPositive locationRange) {
		return withRemoveByLocationRange(locationRange.start, locationRange.end);
	}

	public TConcrete withRemoveByLocationRange(int startLocation, int endLocationExclusive) {
		assert locationCheck(startLocation);
		assert locationCheck(endLocationExclusive);

		TConcrete left = withSplitBefore(startLocation);
		TConcrete right = withSplitAfter(endLocationExclusive);

		return left.withJoinList(right);
	}

	/**
	 * All items must exist in this list.
	 */
	public TConcrete withRemoveListStrict(ListImmutableBase<TElement, TConcrete> list) {
		assert containsAnyOrder(list);

		return withRemoveList(list);
	}

	/**
	 * Doesn't require all items to exist.
	 */
	public TConcrete withRemoveList(ListImmutableBase<TElement, TConcrete> list) {
		assert list != null;

		return withInternalList(internalList.stream()
				.filter(existingItem -> !list.contains(existingItem))
				.collect(collector()));
	}

	/**
	 * All items are in this list. Order of given list doesn't matter.
	 */
	public boolean containsAnyOrder(ListImmutableBase<TElement, TConcrete> list) {
		for (TElement item : list) {
			if (!contains(item)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * One or more items from source list are in this this list.
	 */
	public boolean containsAny(ListImmutableBase<TElement, TConcrete> list) {
		for (TElement item : list) {
			if (contains(item)) {
				return true;
			}
		}

		return false;
	}
	
	public TConcrete withRemoveLocation(int location) {
		RangeIntegerPositive locationRange = RangeIntegerPositive.of(location, location + 1);
		assert locationCheck(locationRange);

		return withRemoveByLocationRange(locationRange);
	}

	/**
	 * Number to remove may be 0. Must not try to remove more elements than
	 * possible.
	 */
	public TConcrete withRemoveLocation(int location, int numberToRemove) {
		assert locationCheck(location);
		assert numberToRemove >= 0;

		if (numberToRemove == 0) {
			// don't want to remove anything
			return concreteThis();
		}

		int rightStartLocation = location + numberToRemove;
		assert locationCheck(rightStartLocation);

		TConcrete left = withSubset(0, location);
		TConcrete right = withSubset(rightStartLocation, size());

		return left.withJoinList(right);
	}

	public TConcrete withJoinItem(TElement item) {
		assert item != null;

		return withInternalList(new ImmutableList.Builder<TElement>().addAll(internalList)
				.add(item)
				.build());
	}

	public TConcrete withJoinList(ListImmutableBase<TElement, TConcrete> after) {
		assert after != null;

		return withInternalList(new ImmutableList.Builder<TElement>().addAll(internalList)
				.addAll(after)
				.build());
	}

	public TConcrete withReplaceIndexWithItem(int location, TElement newItem) {
		assert locationCheck(location);
		assert newItem != null;

		return withRemoveLocation(location).withInsertItem(location, newItem);
	}

	/**
	 * Replaces the single item at the given location with the new list.
	 */
	public TConcrete withReplaceIndexWithList(int location, TConcrete newList) {
		assert locationCheck(location);
		assert newList != null;

		return withRemoveLocation(location).withInsertLocation(location, newList);
	}

	public TConcrete withInsertItem(int location, TElement newItem) {
		assert locationCheck(location);
		assert newItem != null;

		TConcrete before = withSplitBefore(location);
		TConcrete after = withSplitAfter(location);

		return before.withJoinItem(newItem)
				.withJoinList(after);
	}

	public TConcrete withInsertLocation(int location, ListImmutableBase<TElement, TConcrete> newList) {
		assert locationCheck(location);
		assert newList != null;

		TConcrete before = withSplitBefore(location);
		TConcrete after = withSplitAfter(location);

		return before.withJoinList(newList)
				.withJoinList(after);
	}

	/**
	 * Collector for stream operations.
	 * 
	 * @return A new instance of the collector.
	 */
	protected static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> collector() {
		return Collector.of(ImmutableList.Builder::new, ImmutableList.Builder::add, (l, r) -> l.addAll(r.build()),
				ImmutableList.Builder<T>::build);
	}

	/**
	 * Calculates the sum of the elements in this list using the given function.
	 * <p>
	 * For example to sum the first 5 elements use
	 * {@code withSubset(0, 5).sum(MyElement::getAmount)} which assumes you have
	 * derived list class {@code MySubList<MyElement>} and also the method
	 * {@code int MyElement.getAmount()}.
	 * 
	 * @param function
	 * The function that returns an integer value that will be summed.
	 * 
	 * @return The total of all calls to the given function for each element in
	 * this list.
	 */
	protected int sum(ToIntFunction<? super TElement> function) {
		assert function != null;

		return stream().mapToInt(function)
				.sum();
	}

	public int size() {
		int result = internalList.size();
		assert result >= 0;
		return result;
	}

	public boolean contains(TElement item) {
		assert item != null;

		return internalList.contains(item);
	}

	public Stream<TElement> stream() {
		return internalList.stream();
	}

	public void forEach(Consumer<? super TElement> action) {
		internalList.forEach(action);
	}

	@Override
	public Iterator<TElement> iterator() {
		return internalList.iterator();
	}

	@Override
	public String toString() {
		// Guava's method is fine
		return internalList.toString();
	}

	/**
	 * Not valid for empty list.
	 */
	public int startIndex() {
		assert !isEmpty();

		return 0;
	}

	/**
	 * Not valid for empty list.
	 */
	public int endIndex() {
		assert !isEmpty();

		return size() - 1;
	}

	public int endLocation() {
		return size();
	}

	/**
	 * Not valid for empty list.
	 */
	public TElement start() {
		return get(startIndex());
	}

	/**
	 * Not valid for empty list.
	 */
	public TElement end() {
		return get(endIndex());
	}

	public TElement get(int index) {
		assert indexCheck(index);

		return internalList.get(index);
	}

	public TElement getCap(int index) {
		assert !isEmpty();

		return get(indexCap(index));
	}

	public boolean indexCheck(int index) {
		return index >= 0 && index < size();
	}

	/**
	 * Must not be empty.
	 */
	public int indexCap(int index) {
		assert !isEmpty();

		if (index < 0) {
			return 0;
		}

		if (index > endIndex()) {
			return endIndex();
		}

		return index;
	}

	/**
	 * Location is same as index except it's allowed to be one past last index.
	 */
	public boolean locationCheck(int location) {
		return location >= 0 && location <= size();
	}

	public boolean locationCheck(RangeIntegerPositive range) {
		return locationCheck(range.start) && locationCheck(range.end);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Used as base so derived lists can implement a builder easily.
	 * 
	 * @param <TElement>
	 * The type of element held by the list.
	 * 
	 * @param <TList>
	 * The concrete class of the list.
	 * 
	 * @param <TBuilder>
	 * The concrete class of the builder.
	 */
	public static abstract class Builder<TElement, TList extends ListImmutableBase<TElement, TList>, TBuilder extends Builder<TElement, TList, TBuilder>>

			/// rename TConcrete
			extends Immutable<TBuilder> {
		protected final ImmutableList.Builder<TElement> builder;

		public Builder() {
			builder = new ImmutableList.Builder<>();
		}

		// protected abstract TBuilder self();

		public TBuilder add(TElement item) {
			builder.add(item);
			return concreteThis();
		}

		public TBuilder add(Iterable<TElement> list) {
			builder.addAll(list);
			return concreteThis();
		}

		/**
		 * Custom list classes need to be able to return a MyCustomList type
		 * rather than a ListImmutableBase<MyElement> type. Override this method
		 * so it converts a base list into the concrete type. Just create a new
		 * instance and fill it with the elements.
		 * 
		 * It's annoying but overcomes one of the main issues with Java lists
		 * and generics when using subclassing.
		 */

		//// should be 'downCast' not up
		protected abstract TList upcastList(ImmutableList<TElement> baseList);

		public TList build() {
			return upcastList(builder.build());
		}
	}
}