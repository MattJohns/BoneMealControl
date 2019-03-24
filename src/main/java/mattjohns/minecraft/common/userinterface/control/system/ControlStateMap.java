package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.immutable.list.ListImmutable;
import mattjohns.common.immutable.list.MapImmutable;

public class ControlStateMap extends MapImmutable<EnumControlState, Boolean, ControlStateMap> {
	protected ControlStateMap(ListImmutable<EnumControlState> keyList, ListImmutable<Boolean> valueList) {
		super(keyList, valueList);
	}

	public static ControlStateMap of() {
		ControlStateMap result = new ControlStateMap(ListImmutable.of(), ListImmutable.of());

		return result.withClear()
				.withVisible(true)
				.withEnable(true);
	}

	@Override
	protected ControlStateMap copy(ListImmutable<EnumControlState> keyList, ListImmutable<Boolean> valueList) {
		return new ControlStateMap(keyList, valueList);
	}

	@Override
	protected ControlStateMap concreteThis() {
		return this;
	}

	/**
	 * Sets all values to false.
	 */
	@Override
	public ControlStateMap withClear() {
		ControlStateMap result = concreteThis();

		for (EnumControlState state : EnumControlState.values()) {
			result = result.withValueSet(state, false);
		}

		return result;
	}

	public ControlStateMap withVisible(boolean visible) {
		return withValueSet(EnumControlState.Visible, visible);
	}

	public ControlStateMap withEnable(boolean enable) {
		return withValueSet(EnumControlState.Enable, enable);
	}

	public ControlStateMap withReadOnly(boolean readOnly) {
		return withValueSet(EnumControlState.ReadOnly, readOnly);
	}

	public ControlStateMap withMouseover(boolean mouseover) {
		return withValueSet(EnumControlState.Mouseover, mouseover);
	}

	public ControlStateMap withSelect(boolean select) {
		return withValueSet(EnumControlState.Select, select);
	}

	public boolean visible() {
		return value(EnumControlState.Visible);
	}

	public boolean enable() {
		return value(EnumControlState.Enable);
	}

	public boolean readOnly() {
		return value(EnumControlState.ReadOnly);
	}

	public boolean mouseover() {
		return value(EnumControlState.Mouseover);
	}

	public boolean select() {
		return value(EnumControlState.Select);
	}
}
