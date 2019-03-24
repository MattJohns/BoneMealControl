package mattjohns.minecraft.common.userinterface.control.system;

import java.util.Optional;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.minecraft.common.userinterface.device.DeviceSet;
import mattjohns.minecraft.common.userinterface.style.EnumStyle;
import mattjohns.minecraft.common.userinterface.style.StyleControl;
import mattjohns.minecraft.common.userinterface.style.StyleMapControl;

public class Control<T> extends DisplayInputBound<T> {
	public final boolean visibleIs;
	public final boolean enableIs;
	public final boolean readOnlyIs;
	protected final Optional<EnumControlState> controlStateOverride;

	protected final StyleMapControl controlStyleMap;
	public final EnumStyle styleEnumCurrent;

	// has mutable display crop stack, mouse queue etc.
	public final DeviceSet deviceSet;
	protected final ControlDisplay controlDisplay;

	protected Control(DisplayInputBound<?> superObject, boolean visibleIs, boolean enableIs, boolean readOnlyIs,
			DeviceSet deviceSet, ControlDisplay controlDisplay, StyleMapControl controlStyleMap,
			EnumStyle styleEnumCurrent, Optional<EnumControlState> controlStateOverride) {

		super(superObject.id, superObject.boundDesire, superObject.mouseHold, superObject.eventListMouseClick);

		this.visibleIs = visibleIs;
		this.enableIs = enableIs;
		this.readOnlyIs = readOnlyIs;
		this.deviceSet = deviceSet;
		this.controlDisplay = controlDisplay;
		this.controlStyleMap = controlStyleMap;
		this.styleEnumCurrent = styleEnumCurrent;
		this.controlStateOverride = controlStateOverride;
	}

	public static Control<?> of(int id, DeviceSet deviceSet, StyleMapControl controlStyleMap) {
		DisplayInputBound<?> superObject = DisplayInputBound.of(id);

		return new Control<>(superObject, true, true, false, deviceSet, ControlDisplay.of(deviceSet.display()),
				controlStyleMap, EnumStyle.Normal, Optional.empty());
	}

	protected T concreteCopy(Control<?> source) {
		
		//// seems like it's being copied twice, need to rethink all this
		///
		/// For a start naming it copy() rather than concreteCopy()
		
		return concreteTo(new Control<>(source, source.visibleIs, source.enableIs, source.readOnlyIs, source.deviceSet,
				source.controlDisplay, source.controlStyleMap, source.styleEnumCurrent, source.controlStateOverride));
	}

	@Override
	protected T concreteCopy(DisplayInputBound<?> source) {
		return concreteCopy(new Control<>(source, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	public T withVisibleIs(boolean visibleIs) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	public T withEnableIs(boolean enableIs) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	public T withReadOnlyIs(boolean readOnlyIs) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	protected T withDeviceSet(DeviceSet deviceSet) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	protected T withControlDisplay(ControlDisplay controlDisplay) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	protected T withControlStyleMap(StyleMapControl controlStyleMap) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	public T withStyleEnumCurrent(EnumStyle styleEnumCurrent) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	protected T withControlStateOverride(Optional<EnumControlState> controlStateOverride) {
		return concreteCopy(new Control<>(this, visibleIs, enableIs, readOnlyIs, deviceSet, controlDisplay,
				controlStyleMap, styleEnumCurrent, controlStateOverride));
	}

	public T withControlStateOverrideSet(EnumControlState newState) {
		if (this.controlStateOverride.isPresent()) {
			if (this.controlStateOverride.get() == newState) {
				// no change
				return concreteThis();
			}
		}

		return withControlStateOverride(Optional.of(newState));
	}

	protected StyleControl styleControl() {
		return controlStyleMap.value(styleEnumCurrent);
	}

	protected ControlStateMap stateMap() {
		ControlStateMap result = ControlStateMap.of()
				.withVisible(visibleIs)
				.withEnable(enableIs)
				.withReadOnly(readOnlyIs)
				.withMouseover(false)
				.withSelect(false);

		if (controlStateOverride.isPresent()) {
			result = result.withValueSet(controlStateOverride.get(), true);
		}

		return result;
	}

	public void render(DisplayPosition positionOffset) {
		if (!visibleIs) {
			// invisible
			//
			// derived controls should also check this
			return;
		}

		controlDisplay.controlRender(bound().withTranslate(positionOffset), styleControl());
	}

	protected EnumStyle controlStateToStyleEnum(ControlStateMap stateMap) {
		// not enable
		if (!stateMap.value(EnumControlState.Enable)) {
			return EnumStyle.NotEnable;
		}

		// read only
		if (stateMap.value(EnumControlState.ReadOnly)) {
			return EnumStyle.ReadOnly;
		}

		// mouseover
		if (stateMap.value(EnumControlState.Mouseover)) {
			return EnumStyle.Mouseover;
		}

		// select
		if (stateMap.value(EnumControlState.Select)) {
			return EnumStyle.Select;
		}

		// default to normal
		return EnumStyle.Normal;
	}

	public DisplaySize sizeDesire() {
		return super.boundDesire.size();
	}

	public DisplayBound bound() {
		// automatically cap at minimum size etc..
		return super.bound().withSize(sizeEffective());
	}

	protected DisplaySize sizeEffective() {
		return sizeDesire().withMaximum(sizeMinimum());
	}

	protected DisplaySize sizeMinimum() {
		// allow at least the padding
		return styleControl().content.paddingEffective()
				.sizeTotal();
	}

	public DisplayBound contentBound() {
		return DisplayBound.ofSize(size())
				.withPadding(styleControl().contentPaddingEffective());
	}

	public DisplayPosition contentPosition() {
		return contentBound().topLeft;
	}

	public DisplaySize contentSize() {
		return contentBound().size();
	}

	// public DisplaySize size() {
	// return displayInputBound.size();
	// }
	//
	// public TConcrete withSizeSetByContent(DisplaySize contentSize) {
	// DisplaySize newControlSize = styleControl().contentPaddingEffective()
	// .sizeTotal()
	// .withTranslate(contentSize);
	//
	// return withSizeDesireSet(newControlSize);
	// }
}
