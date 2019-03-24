package mattjohns.minecraft.common.userinterface.control.system;

import java.util.function.Consumer;

import mattjohns.common.immutable.list.ListImmutableInteger;
import mattjohns.common.immutable.math.geometry.dimension2.VectorInteger;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.minecraft.common.userinterface.device.DeviceSet;
import mattjohns.minecraft.common.userinterface.style.StyleMapControl;

public class Component<T extends Component<T>> extends Control<T> {
	public final DisplayBound parentAbsoluteBound;

	protected Component(Control<?> superObject, DisplayBound parentAbsoluteBound) {

		super(superObject, superObject.visibleIs, superObject.enableIs, superObject.readOnlyIs, superObject.deviceSet,
				superObject.controlDisplay, superObject.controlStyleMap, superObject.styleEnumCurrent,
				superObject.controlStateOverride);

		this.parentAbsoluteBound = parentAbsoluteBound;
	}

	public static Component<?> ofComponent(int id, DeviceSet deviceSet, StyleMapControl controlStyleMap) {

		Control<?> control = Control.of(id, deviceSet, controlStyleMap);

		return new Component<>(control, DisplayBound.Tiny);
	}

	protected T concreteCopy(Component<?> source) {
		return concreteTo(new Component<>(source, source.parentAbsoluteBound));
	}

	@Override
	protected T concreteCopy(Control<?> source) {
		return concreteCopy(new Component<>(source, parentAbsoluteBound));
	}

	public T withParentAbsoluteBound(DisplayBound parentAbsoluteBound) {
		return concreteCopy(new Component<>(this, parentAbsoluteBound));
	}

	protected T withComponentPrepareLayout() {
		return concreteThis();
	}

	public T withComponentLayout() {
		T result = withComponentPrepareLayout();
		return result;
	}

	public void componentForEachInclusive(Consumer<Component<?>> consume) {
		consume.accept(this);

		componentForEachExclusive(consume);
	}

	public void componentForEachExclusive(Consumer<Component<?>> consume) {
	}

	public ListImmutableInteger componentIdList() {
		return ListImmutableInteger.of(id);
	}

	public ComponentList componentFromId(ListImmutableInteger idList) {
		if (idList.contains(id)) {
			return ComponentList.of()
					.withJoinItem(this);
		}
		else {
			return ComponentList.of();
		}
	}

	public ListImmutableInteger componentDisplayIdList() {
		if (visibleIs) {
			return ListImmutableInteger.of(id);
		}
		else {
			return ListImmutableInteger.of();
		}
	}

	public ListImmutableInteger componentMouseoverIdList(DisplayPosition mousePositionAbsolute) {
		// if (mouseoverCheckAbsolute(mousePositionAbsolute)) {
		// return ListImmutableInteger.of(id());
		// }
		// else {
		return ListImmutableInteger.of();
		// }
	}

	public DisplayPosition positionAbsolute() {
		return parentAbsoluteBound.topLeft.withTranslate(position());
	}

	// prefer this overload when dealing with components and subclasses
	public void render() {
		render(parentAbsoluteBound.topLeft);
	}

	public void render(DisplayPosition positionOffset) {
		super.render(positionOffset);
	}

	// public boolean mouseoverCheckAbsolute(DisplayPosition
	// mousePositionAbsolute) {
	// DisplayPosition mousePositionRelative =
	// positionAbsoluteToControl(mousePositionAbsolute);
	//
	//// return control.mouseoverCheck(mousePositionRelative);
	// }

	protected DisplayPosition positionAbsoluteToControl(DisplayPosition absolute) {
		DisplayPosition result = absolutePositionToRelativeUnsafe(absolute);
		return result;
	}

	protected DisplayPosition absolutePositionToRelativeUnsafe(DisplayPosition absolute) {
		VectorInteger delta = VectorInteger.of(positionAbsolute())
				.withNegate();
		return absolute.withTranslate(delta);
	}

	public DisplayBound boundAbsolute() {
		return DisplayBound.of(positionAbsolute(), size());
	}

	public DisplayBound contentBoundAbsolute() {
		return this.contentBound()
				.withTranslate(positionAbsolute());
	}
}
