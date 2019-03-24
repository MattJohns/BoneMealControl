package mattjohns.common.immutable.userinterface.listbox;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;

public final class NewListBoxElementDisplayComponent extends DisplayComponent<NewListBoxElementDisplayComponent> {
	public final boolean isSelect;

	public NewListBoxElementDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition, boolean isSelect) {
		super(isStale, isVisible, bound, containerPosition);

		this.isSelect = isSelect;
	}

	@Override
	protected NewListBoxElementDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected NewListBoxElementDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new NewListBoxElementDisplayComponent(isStale, isVisible, bound, containerPosition, isSelect);
	}

	public static NewListBoxElementDisplayComponent of() {
		return new NewListBoxElementDisplayComponent(true, true, DisplayBound.of(), DisplayPosition.Zero, false);
	}

	public NewListBoxElementDisplayComponent withText(String text) {
		return new NewListBoxElementDisplayComponent(isStale, isVisible, bound, containerPosition, isSelect);
	}

	public NewListBoxElementDisplayComponent withIsSelect(boolean isSelect) {
		return new NewListBoxElementDisplayComponent(isStale, isVisible, bound, containerPosition, isSelect);
	}
}
