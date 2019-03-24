package mattjohns.minecraft.common.userinterface.control.system;

import mattjohns.common.immutable.Immutable;

/// can do select and mouseover as a 'state override' in Control class .
/// So Window keeps a list of mouseover and then forces mouseover state for
/// those controls (also clears non mouseover Controls).

///
public final class ControlState extends Immutable<ControlState> {
	public final EnumControlState name;
	public final boolean value;

	public ControlState(EnumControlState name, boolean value) {
		this.name = name;
		this.value = value;
	}

	public static ControlState of(EnumControlState name, boolean value) {
		return new ControlState(name, value);
	}

	@Override
	protected ControlState concreteCopy(Immutable<?> source) {
		return new ControlState(name, value);
	}
}
