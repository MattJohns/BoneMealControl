package mattjohns.common.immutable.list;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;

/**
 * Stores an immutable list of the given type.
 * 
 * If you need to derive a custom list class use ListImmutableBase instead.
 * 
 * @param <TElement>
 * The type of element to be held by the list.
 */
public final class ListImmutable<TElement> extends ListImmutableBase<TElement, ListImmutable<TElement>> {
	protected ListImmutable(ImmutableList<TElement> internalList) {
		super(internalList);
	}

	@Override
	protected final ListImmutable<TElement> copy(ImmutableList<TElement> internalList) {
		return new ListImmutable<TElement>(internalList);
	}

	public static <T> ListImmutable<T> of() {
		return new ListImmutable<T>(ImmutableList.of());
	}

	public static <T> ListImmutable<T> of(T item) {
		return new ListImmutable<T>(ImmutableList.of(item));
	}

	public static <T> ListImmutable<T> of(Iterable<T> list) {
		return new ListImmutable<T>(ImmutableList.copyOf(list));
	}

	public static final class Builder<TElement>
			extends ListImmutableBase.Builder<TElement, ListImmutable<TElement>, Builder<TElement>> {
		/**
		 * If you're having problems with the compiler inferring generics
		 * properly, try declaring a builder of type ListImmutable.Builder
		 * <MyElement> then use that. If you try to create the builder instance
		 * and chain add() methods all on one line it sometimes doesn't infer
		 * correctly.
		 */
		public static <T> Builder<T> of() {
			return new Builder<T>();
		}

		@Override
		protected Builder<TElement> concreteCopy(Immutable<?> source) {
			return new Builder<>();
		}

		@Override
		protected ListImmutable<TElement> upcastList(ImmutableList<TElement> baseList) {
			return new ListImmutable<TElement>(baseList);
		}
	}
}
