package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

public final class TextAreaDisplayComponent extends DisplayComponent<TextAreaDisplayComponent> {
	public TextAreaDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		super(isStale, isVisible, bound, containerPosition);
	}

	@Override
	protected TextAreaDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected TextAreaDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new TextAreaDisplayComponent(isStale, isVisible, bound, containerPosition);
	}

	public static TextAreaDisplayComponent of(DisplaySize size, int lineDisplaySizeY) {
		return new TextAreaDisplayComponent(true, true, DisplayBound.ofSize(size), DisplayPosition.Zero);
	}
}
