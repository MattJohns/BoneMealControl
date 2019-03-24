package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayComponent;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;

public final class ViewportDisplayComponent extends DisplayComponent<ViewportDisplayComponent> {
	public final ViewportLineList visibleLineList;
	public final int lineSizeY;

	public ViewportDisplayComponent(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition, ViewportLineList visibleLineList,
			int lineSizeY) {
		super(isStale, isVisible, bound, containerPosition);

		this.visibleLineList = visibleLineList;
		this.lineSizeY = lineSizeY;

		assert this.visibleLineList != null;
		assert this.lineSizeY > 0;
	}

	@Override
	protected ViewportDisplayComponent concreteThis() {
		return this;
	}

	@Override
	protected ViewportDisplayComponent copy(boolean isStale, boolean isVisible, DisplayBound bound,
			DisplayPosition containerPosition) {
		return new ViewportDisplayComponent(isStale, isVisible, bound, containerPosition,
				visibleLineList, lineSizeY);
	}

	public static ViewportDisplayComponent of(int lineSizeY) {
		// values don't matter because it's stale
		return new ViewportDisplayComponent(true, false, DisplayBound.of(),
				DisplayPosition.Zero, ViewportLineList.of(), lineSizeY);
	}

	public ViewportDisplayComponent withVisibleLineList(ViewportLineList visibleLineList) {
		return new ViewportDisplayComponent(isStale, isVisible, bound, containerPosition,
				visibleLineList, lineSizeY);
	}
}
