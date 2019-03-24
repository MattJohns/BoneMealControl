package mattjohns.common.immutable.userinterface.listbox;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;

public final class NewListBoxElement {
	public final int id;
	public final ListBoxElementDisplayComponent displayComponent;
	public final boolean isEnable;

	protected NewListBoxElement(int id, ListBoxElementDisplayComponent displayComponent, boolean isEnable) {
		this.id = id;
		this.displayComponent = displayComponent;
		this.isEnable = isEnable;

		assert this.id >= 0;
		assert this.displayComponent != null;
	}

	public static NewListBoxElement of(int id) {
		return new NewListBoxElement(id, ListBoxElementDisplayComponent.of(), true);
	}

	protected NewListBoxElement withDisplayComponent(ListBoxElementDisplayComponent displayComponent) {
		return new NewListBoxElement(id, displayComponent, isEnable);
	}

	public NewListBoxElement withDisplayComponentUpdate(DisplayPosition containerPosition, boolean isVisible,
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
				.withIsSelect(isSelect)
				.withIsVisible(isVisible)
				.withIsStale(false);

		return withDisplayComponent(newDisplayComponent);
	}

	public NewListBoxElement withEnable(boolean isEnable) {
		return new NewListBoxElement(id, displayComponent, isEnable);
	}

	protected NewListBoxElement withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}
}
