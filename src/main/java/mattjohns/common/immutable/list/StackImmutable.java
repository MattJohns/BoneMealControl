package mattjohns.common.immutable.list;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.Result;

public class StackImmutable<TElement> extends Immutable<StackImmutable<TElement>> {
	protected final ListImmutable<TElement> list;

	protected StackImmutable(ListImmutable<TElement> list) {
		this.list = list;
	}

	public static <TElement> StackImmutable<TElement> of() {
		return new StackImmutable<>(ListImmutable.of());
	}

	@Override
	protected StackImmutable<TElement> concreteCopy(Immutable<?> source) {
		return new StackImmutable<>(list);
	}

	protected StackImmutable<TElement> withList(ListImmutable<TElement> list) {
		return new StackImmutable<>(list);
	}

	public TElement top() {
		assert !isEmpty();

		return list.end();
	}

	public StackImmutable<TElement> withPush(TElement element) {
		return withList(list.withJoinItem(element));
	}

	public StackImmutable<TElement> withPopDiscard() {
		ListImmutable<TElement> newList = list.withRemoveLocation(list.endIndex());

		return withList(newList);
	}

	public PopResult<TElement> withPop() {
		assert !isEmpty();

		TElement element = list.end();

		return PopResult.of(withPopDiscard(), element);
	}

	public StackImmutable<TElement> withClear() {
		return withList(ListImmutable.of());
	}

	public int listSize() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public static class PopResult<TElement> extends Result<StackImmutable<TElement>, TElement> {
		protected PopResult(StackImmutable<TElement> self, boolean isChange, TElement data) {
			super(self, isChange, data);
		}

		public static <TElement> PopResult<TElement> of(StackImmutable<TElement> self, TElement data) {
			return new PopResult<TElement>(self, true, data);
		}
	}
}
