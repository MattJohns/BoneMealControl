package mattjohns.common.immutable.userinterface.listbox;

import java.util.Optional;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;
import mattjohns.common.immutable.list.ListImmutableString;

public final class ListBoxElementList extends ListImmutableBase<ListBoxElement, ListBoxElementList> {
	protected ListBoxElementList(ImmutableList<ListBoxElement> internalList) {
		super(internalList);
	}

	public static ListBoxElementList of() {
		return new ListBoxElementList(ImmutableList.of());
	}

	public static ListBoxElementList ofIdEqualIndex(ListImmutableString elementTextList) {
		Builder builder = Builder.of();

		for (int i = 0; i < elementTextList.size(); i++) {
			int id = i;
			ListBoxElement element = ListBoxElement.of(id, elementTextList.get(i));

			builder.add(element);
		}

		return builder.build();
	}

	@Override
	protected ListBoxElementList concreteThis() {
		return this;
	}

	@Override
	protected final ListBoxElementList copy(ImmutableList<ListBoxElement> internalList) {
		return new ListBoxElementList(internalList);
	}

	public Optional<Integer> idToIndex(int id) {
		for (int i = 0; i < size(); i++) {
			if (get(i).id == id) {
				return Optional.of(i);
			}
		}

		return Optional.empty();
	}

	public boolean idCheck(int id) {
		return idToIndex(id).isPresent();
	}

	public static final class Builder extends ListImmutableBase.Builder<ListBoxElement, ListBoxElementList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected ListBoxElementList upcastList(ImmutableList<ListBoxElement> baseList) {
			return new ListBoxElementList(baseList);
		}
	}
}
