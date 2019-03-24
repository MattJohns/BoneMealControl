package mattjohns.minecraft.common.userinterface.control;

import java.util.function.Function;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.minecraft.common.userinterface.control.system.Component;
import mattjohns.minecraft.common.userinterface.control.system.ComponentList;
import mattjohns.minecraft.common.userinterface.control.system.Container;
import mattjohns.minecraft.common.userinterface.control.system.ControlFont;
import mattjohns.minecraft.common.userinterface.control.system.IdSupply;
import mattjohns.minecraft.common.userinterface.device.DeviceSet;
import mattjohns.minecraft.common.userinterface.style.StyleMapButton;

public class ControlButton<T extends ControlButton<T>> extends Container<T> {
	protected final StyleMapButton styleMapButton;

	protected final ControlLabel<?> titleControl;

	protected ControlButton(Container<?> superObject, StyleMapButton styleMapButton, ControlLabel<?> titleControl) {
		super(superObject, superObject.containerStyleMap, superObject.idSupply);

		this.styleMapButton = styleMapButton;
		this.titleControl = titleControl;
	}

	public static ControlButton<?> of(IdSupply idSupply, DeviceSet deviceSet, StyleMapButton styleMapButton,
			ControlFont<?> font) {

		Container<?> container = Container.of(idSupply, deviceSet, styleMapButton.container);

		ControlLabel<?> titleControl = ControlLabel.of(idSupply.get(), deviceSet, styleMapButton.title, font);

		return new ControlButton<>(container, styleMapButton, titleControl);
	}

	protected T concreteCopy(ControlButton<?> source) {
		return concreteTo(new ControlButton<>(source, styleMapButton, titleControl));
	}

	@Override
	protected T concreteCopy(Container<?> source) {
		return concreteCopy(new ControlButton<>(source, styleMapButton, titleControl));
	}

	protected T withStyleMapButton(StyleMapButton styleMapButton) {
		return concreteCopy(new ControlButton<>(this, styleMapButton, titleControl));
	}

	protected T withTitleControl(ControlLabel<?> titleControl) {
		return concreteCopy(new ControlButton<>(this, styleMapButton, titleControl));
	}

	public T withText(String text) {
		return withTitleControl(titleControl.withText(text));
	}

	@Override
	protected T withComponentFunctionAndSave(int id, Function<Component<?>, Component<?>> mainFunction) {
		return concreteThis();///////
	}

	@Override
	public T withComponentPrepareLayout() {
		T result = concreteThis();

		DisplayBound titleBound = result.contentBound();

		ControlLabel<?> newTitleControl = titleControl.withBoundDesire(titleBound);

		result = result.withTitleControl(newTitleControl);

		return result;
	}

	@Override
	protected ComponentList componentList() {

		return ComponentList.of()
				.withJoinItem(titleControl);
	}

	public String text() {
		return titleControl.text;
	}
}