package mattjohns.common.immutable.userinterface.listbox;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

public final class NewListBoxDisplayComponent extends DisplayComponent<NewListBoxDisplayComponent> {
	public final NewListBoxElementList displayList;

	public NewListBoxDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition, NewListBoxElementList displayList) {
		super(isStale, isVisible, bound, containerPosition);

		this.displayList = displayList;

		assert this.displayList != null;
	}

	@Override
	protected NewListBoxDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected NewListBoxDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new NewListBoxDisplayComponent(isStale, isVisible, bound, containerPosition, displayList);
	}

	public static NewListBoxDisplayComponent of(DisplaySize size) {
		return new NewListBoxDisplayComponent(true, true, DisplayBound.ofSize(size), DisplayPosition.Zero,
				NewListBoxElementList.of());
	}

	public NewListBoxDisplayComponent withDisplayList(NewListBoxElementList displayList) {
		return new NewListBoxDisplayComponent(isStale, isVisible, bound, containerPosition, displayList);
	}
}
