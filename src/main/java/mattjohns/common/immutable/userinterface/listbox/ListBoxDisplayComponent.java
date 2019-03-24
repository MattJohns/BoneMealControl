package mattjohns.common.immutable.userinterface.listbox;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

public final class ListBoxDisplayComponent extends DisplayComponent<ListBoxDisplayComponent> {
	public final ListBoxElementList displayList;

	public ListBoxDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition, ListBoxElementList displayList) {
		super(isStale, isVisible, bound, containerPosition);

		this.displayList = displayList;

		assert this.displayList != null;
	}

	@Override
	protected ListBoxDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected ListBoxDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new ListBoxDisplayComponent(isStale, isVisible, bound, containerPosition, displayList);
	}

	public static ListBoxDisplayComponent of(DisplaySize size) {
		return new ListBoxDisplayComponent(true, true, DisplayBound.ofSize(size), DisplayPosition.Zero,
				ListBoxElementList.of());
	}
	
	public ListBoxDisplayComponent withDisplayList(ListBoxElementList displayList) {
		return new ListBoxDisplayComponent(isStale, isVisible, bound, containerPosition, displayList);
	}
}
