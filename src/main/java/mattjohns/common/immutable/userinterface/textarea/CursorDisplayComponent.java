package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

public final class CursorDisplayComponent extends DisplayComponent<CursorDisplayComponent> {
	public final DisplaySize emptySymbolSize;

	protected CursorDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition, DisplaySize emptySymbolSize) {
		super(isStale, isVisible, bound, containerPosition);

		this.emptySymbolSize = emptySymbolSize;
	}

	public static CursorDisplayComponent of(DisplaySize emptySymbolSize) {
		return new CursorDisplayComponent(true, false, DisplayBound.of(),
				DisplayPosition.Zero, emptySymbolSize);
	}

	@Override
	protected CursorDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected CursorDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new CursorDisplayComponent(isStale, isVisible, bound, containerPosition, emptySymbolSize);
	}
}
