package mattjohns.common.immutable.userinterface.listbox;

import java.util.Optional;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;

public final class NewListBoxElementList extends ListImmutableBase<NewListBoxElement, NewListBoxElementList> {
	protected NewListBoxElementList(ImmutableList<NewListBoxElement> internalList) {
		super(internalList);
	}

	public static NewListBoxElementList of() {
		return new NewListBoxElementList(ImmutableList.of());
	}

	@Override
	protected final NewListBoxElementList copy(ImmutableList<NewListBoxElement> internalList) {
		return new NewListBoxElementList(internalList);
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

	public static final class Builder
			extends ListImmutableBase.Builder<NewListBoxElement, NewListBoxElementList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected NewListBoxElementList upcastList(ImmutableList<NewListBoxElement> baseList) {
			return new NewListBoxElementList(baseList);
		}
	}
}
