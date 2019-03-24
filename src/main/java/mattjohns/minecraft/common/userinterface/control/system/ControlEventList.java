package mattjohns.minecraft.common.userinterface.control.system;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.list.ListImmutableBase;

public final class ControlEventList<TEvent extends ControlEvent<?>> extends ListImmutableBase<TEvent, ControlEventList<TEvent>> {
	protected ControlEventList(ImmutableList<TEvent> internalList) {
		super(internalList);
	}

	public static <TEvent extends ControlEvent<?>> ControlEventList<TEvent> of() {
		return new ControlEventList<>(ImmutableList.of());
	}

	@Override
	protected ControlEventList<TEvent> copy(ImmutableList<TEvent> internalList) {
		return new ControlEventList<>(internalList);
	}

	@Override
	protected ControlEventList<TEvent> concreteThis() {
		return this;
	}
}
