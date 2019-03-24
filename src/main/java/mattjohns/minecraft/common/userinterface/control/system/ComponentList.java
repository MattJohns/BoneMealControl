package mattjohns.minecraft.common.userinterface.control.system;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;

public final class ComponentList extends ListImmutableBase<Component<?>, ComponentList> {
	protected ComponentList(ImmutableList<Component<?>> internalList) {
		super(internalList);
	}

	public static ComponentList of() {
		return new ComponentList(ImmutableList.of());
	}

	public static ComponentList of(Component<?> item) {
		return new ComponentList(ImmutableList.of(item));
	}

	@Override
	protected ComponentList copy(ImmutableList<Component<?>> internalList) {
		return new ComponentList(internalList);
	}

	@Override
	protected ComponentList concreteThis() {
		return this;
	}

	public ComponentList withRemoveId(int id) {
		assert idIsContain(id);

		int index = getIndexById(id);

		return withRemoveLocation(index);
	}

	public boolean idIsContain(int id) {
		for (Component<?> element : this) {
			if (element.id == id) {
				return true;
			}
		}

		return false;
	}

	public Component<?> getById(int id) {
		assert idIsContain(id);

		return get(getIndexById(id));
	}

	protected int getIndexById(int id) {
		assert idIsContain(id);
		assert !isEmpty();

		for (int i = 0; i < size(); i++) {
			if (get(i).id == id) {
				return i;
			}
		}

		assert false;
		return -1;
	}

	public static final class Builder extends ListImmutableBase.Builder<Component<?>, ComponentList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected ComponentList upcastList(ImmutableList<Component<?>> baseList) {
			return new ComponentList(baseList);
		}
	}

}
