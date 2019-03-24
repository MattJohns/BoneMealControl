package mattjohns.minecraft.common.userinterface.control;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.minecraft.common.userinterface.control.system.Component;
import mattjohns.minecraft.common.userinterface.control.system.ControlFont;
import mattjohns.minecraft.common.userinterface.device.DeviceSet;
import mattjohns.minecraft.common.userinterface.style.StyleLabel;
import mattjohns.minecraft.common.userinterface.style.StyleMapLabel;

public class ControlLabel<T extends ControlLabel<T>> extends Component<T> {

	public final StyleMapLabel styleMapLabel;
	protected final ControlFont<?> font;
	public final String text;
	protected final DisplayBound textBound;

	protected ControlLabel(Component<?> superObject, StyleMapLabel styleMapLabel, ControlFont<?> font, String text,
			DisplayBound textBound) {

		super(superObject, superObject.parentAbsoluteBound);

		this.styleMapLabel = styleMapLabel;
		this.font = font;
		this.text = text;
		this.textBound = textBound;
	}

	public static ControlLabel<?> of(int id, DeviceSet deviceSet, StyleMapLabel styleMapLabel, ControlFont<?> font) {

		Component<?> component = Component.ofComponent(id, deviceSet, styleMapLabel.control);

		return new ControlLabel<>(component, styleMapLabel, font, "", DisplayBound.Tiny);
	}

	protected T concreteCopy(ControlLabel<?> source) {
		return concreteTo(new ControlLabel<>(source, source.styleMapLabel, source.font, source.text, source.textBound));
	}

	@Override
	protected T concreteCopy(Component<?> source) {
		return concreteCopy(new ControlLabel<>(source, styleMapLabel, font, text, textBound));
	}

	protected T withFont(ControlFont<?> font) {
		return concreteCopy(new ControlLabel<>(this, styleMapLabel, font, text, textBound));
	}

	public T withText(String text) {
		return concreteCopy(new ControlLabel<>(this, styleMapLabel, font, text, textBound));
	}

	protected T withTextBound(DisplayBound textBound) {
		return concreteCopy(new ControlLabel<>(this, styleMapLabel, font, text, textBound));
	}

	@Override
	protected T withComponentPrepareLayout() {
		DisplayBound textBound = textBoundDerive(size());

		return withTextBound(textBound);
	}

	@Override
	public T withComponentLayout() {
		return super.withComponentLayout();
	}

	@Override
	public void render() {
		if (!visibleIs) {
			return;
		}
		super.render();

		DisplayBound textBoundAbsolute = textBound.withTranslate(positionAbsolute());

		controlDisplay.textRender(text, font, textBoundAbsolute, styleLabel().text);
	}

	protected DisplaySize newSizeAutomaticDerive(DisplaySize sizeDesiredOrMinimum) {

		return sizeDesiredOrMinimum;//////
	}

	// relative
	protected DisplayBound textBoundDerive(DisplaySize controlSize) {
		if (this.text.length() < 1) {
			return DisplayBound.Tiny;
		}

		DisplayBound contentBound = contentBound();

		// DisplayBound result = controlDisplay.textBoundDerive(text,
		// styleLabel().text, contentBound.size());

		DisplayBound result = font.textBoundDerive(text, styleLabel().text, contentBound.size());

		// move from inside bound to control reference frame
		result = result.withTranslate(contentBound.topLeft);

		return result;
	}

	protected StyleLabel styleLabel() {
		return styleMapLabel.value(styleEnumCurrent);
	}
}
