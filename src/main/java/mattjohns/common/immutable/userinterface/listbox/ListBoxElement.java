package mattjohns.common.immutable.userinterface.listbox;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;

public final class ListBoxElement {
	public final int id;
	public final String text;
	public final ListBoxElementDisplayComponent displayComponent;
	public final boolean isEnable;

	protected ListBoxElement(int id, String text, ListBoxElementDisplayComponent displayComponent, boolean isEnable) {
		this.id = id;
		this.text = text;
		this.displayComponent = displayComponent;
		this.isEnable = isEnable;

		assert this.id >= 0;
		assert this.text != null;
		assert this.displayComponent != null;
	}

	public static ListBoxElement of(int id, String text) {
		return new ListBoxElement(id, text, ListBoxElementDisplayComponent.of(), true);
	}

	protected ListBoxElement withDisplayComponent(ListBoxElementDisplayComponent displayComponent) {
		return new ListBoxElement(id, text, displayComponent, isEnable);
	}

	public ListBoxElement withDisplayComponentUpdate(DisplayPosition containerPosition, boolean isVisible,
			DisplayBound boundRelative, boolean isSelect) {
		if (!displayComponent.isStale) {
			return this;
		}

		ListBoxElementDisplayComponent newDisplayComponent = displayComponent;
		if (isVisible) {
			// only set bound if visible
			newDisplayComponent = newDisplayComponent.withBound(boundRelative);
		}

		newDisplayComponent = newDisplayComponent.withContainerPosition(containerPosition)

				// get visible elements
				.withText(text)

				.withIsSelect(isSelect)

				.withIsVisible(isVisible)

				.withIsStale(false);

		return withDisplayComponent(newDisplayComponent);
	}
	
	public ListBoxElement withEnable(boolean isEnable) {
		return new ListBoxElement(id, text, displayComponent, isEnable);
	}

	protected ListBoxElement withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}
}
