package mattjohns.common.immutable.userinterface.listbox;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;

public final class ListBoxElementDisplayComponent extends DisplayComponent<ListBoxElementDisplayComponent> {
	public final String text;
	public final boolean isSelect;

	public ListBoxElementDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition, String text, boolean isSelect) {
		super(isStale, isVisible, bound, containerPosition);

		this.text = text;
		this.isSelect = isSelect;

		assert this.text != null;
	}

	@Override
	protected ListBoxElementDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected ListBoxElementDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new ListBoxElementDisplayComponent(isStale, isVisible, bound, containerPosition, text, isSelect);
	}

	public static ListBoxElementDisplayComponent of() {
		return new ListBoxElementDisplayComponent(true, true, DisplayBound.of(), DisplayPosition.Zero, "", false);
	}

	public ListBoxElementDisplayComponent withText(String text) {
		return new ListBoxElementDisplayComponent(isStale, isVisible, bound, containerPosition, text, isSelect);
	}

	public ListBoxElementDisplayComponent withIsSelect(boolean isSelect) {
		return new ListBoxElementDisplayComponent(isStale, isVisible, bound, containerPosition, text, isSelect);
	}
}
