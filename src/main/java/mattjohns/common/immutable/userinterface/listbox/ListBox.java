package mattjohns.common.immutable.userinterface.listbox;

import java.util.Optional;

import mattjohns.common.immutable.list.ListImmutableString;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;

public final class ListBox {
	protected final ListBoxElementList elementList;
	protected final int scrollIndex;
	protected final Optional<Integer> selectId;
	public final boolean isAutomaticSelect;

	public final DisplaySize size;
	public final int itemSizeY;
	public final Font font;
	public final ListBoxDisplayComponent displayComponent;

	protected ListBox(ListBoxElementList elementList, int scrollIndex, Optional<Integer> selectId,
			boolean isAutomaticSelect, DisplaySize size, int itemSizeY, Font font,
			ListBoxDisplayComponent displayComponent) {
		this.elementList = elementList;
		this.scrollIndex = scrollIndex;
		this.selectId = selectId;
		this.isAutomaticSelect = isAutomaticSelect;

		this.size = size;
		this.itemSizeY = itemSizeY;
		this.font = font;
		this.displayComponent = displayComponent;

		assert this.elementList != null;
		assert this.selectId != null;
		assert this.size != null;
		assert this.font != null;
		assert this.displayComponent != null;

		assert this.itemSizeY >= 1;

		assert this.size.y >= this.itemSizeY : "Not large enough for a single item.";

		if (isEmpty()) {
			assert this.scrollIndex == 0 : "Scroll not possible for empty list.";

			assert !this.selectId.isPresent() : "Selection not possible for empty list.";
		} else {
			// assert this.scrollIndex.isPresent();
			assert scrollIndexCheck(this.scrollIndex);

			if (this.selectId.isPresent()) {
				assert elementIndexCheck(this.selectId.get());
			}
		}
	}

	public static ListBox of(ListBoxElementList itemList, DisplaySize size, int itemSizeY, Font font,
			boolean isAutomaticSelect) {
		int scrollIndex = 0;
		Optional<Integer> selectId = Optional.empty();

		ListBoxDisplayComponent displayComponent = ListBoxDisplayComponent.of(size);

		return new ListBox(itemList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font, displayComponent);
	}

	public ListBox withClear() {
		return new ListBox(ListBoxElementList.of(), 0, Optional.empty(), isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	protected ListBox withDisplayComponent(ListBoxDisplayComponent displayComponent) {
		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	protected ListBox withSelectId(Optional<Integer> selectId) {
		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	protected ListBox withSelectClear() {
		return new ListBox(elementList, scrollIndex, Optional.empty(), isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	public ListBox withTrySelectFirst() {
		if (isEmpty()) {
			return withSelectClear();
		}

		int firstId = elementList.start().id;

		return new ListBox(elementList, scrollIndex, Optional.of(firstId), isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	protected ListBox withAutomaticSelect(boolean isAutomaticSelect) {
		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	public ListBox withDisplayComponentUpdate() {
		if (!displayComponent.isStale) {
			return this;
		}

		ListBoxElementList newElementList = elementDisplayComponentUpdate();

		ListBox newListBox = withElementList(newElementList);

		newListBox = newListBox.withDisplayComponent(displayComponent.withBound(DisplayBound.ofSize(size))

				// position not used
				.withContainerPosition(DisplayPosition.Zero)

				// get visible elements
				.withDisplayList(newListBox.displayList())

				.withIsVisible(true).withIsStale(false));

		// child components
		return newListBox;
	}

	protected ListBoxElementList elementDisplayComponentUpdate() {
		ListBoxElementList.Builder builder = ListBoxElementList.Builder.of();

		RangeIntegerPositive displayListIndexRange = displayListIndexRange();

		for (int i = 0; i < elementListSize(); i++) {
			ListBoxElement element = elementList.get(i);

			boolean isVisible = displayListIndexRange.isContain(i);

			DisplayPosition elementPosition;
			if (isVisible) {
				int elementPositionY = elementPositionYContent(i) - scrollPositionY();
				elementPosition = DisplayPosition.of(0, elementPositionY);
			} else {
				// not visible so doesn't matter
				elementPosition = DisplayPosition.of();
			}

			DisplayBound elementBound = DisplayBound.of(elementPosition, elementSize());

			boolean isSelect = selectId.isPresent() ? (selectId.get() == element.id) : false;

			ListBoxElement newElement = element.withDisplayComponentUpdate(DisplayPosition.Zero, isVisible,
					elementBound, isSelect);

			builder.add(newElement);
		}

		return builder.build();
	}

	public ListBox withElementListSet(ListBoxElementList elementList) {
		int scrollIndex = 0;

		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent).withDisplaySetStale().withElementSetStale();
	}

	protected DisplaySize elementSize() {
		return DisplaySize.of(size.x, itemSizeY);
	}

	public int sizeX() {
		return size.x;
	}

	protected ListBox withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}

	protected ListBox withElementSetStale() {
		ListBoxElementList.Builder builder = ListBoxElementList.Builder.of();

		for (ListBoxElement element : elementList) {
			builder.add(element.withDisplaySetStale());
		}

		return withElementList(builder.build());
	}

	public ListBox withElementEnable(int id, boolean state) {
		ListBoxElement element = elementGetById(id);

		element = element.withEnable(state);

		return withElementSetById(element);
	}

	protected ListBox withScrollIndex(int scrollIndex) {
		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	public ListBox withScrollDeltaCap(int delta) {
		if (isEmpty()) {
			return this;
		}

		int proposedScrollIndex = scrollIndex + delta;

		int capIndex = scrollIndexCap(proposedScrollIndex);

		return withScrollIndex(capIndex).withDisplaySetStale().withElementSetStale();
	}

	public ListBox withScrollUp() {
		return withScrollDeltaCap(-1);
	}

	public ListBox withScrollDown() {
		return withScrollDeltaCap(+1);
	}

	public ListBox withPageUp() {
		return withScrollDeltaCap(elementPerPage() * -1);
	}

	public ListBox withPageDown() {
		return withScrollDeltaCap(elementPerPage() * +1);
	}

	public ListBox withScrollIndexSetCap(int newIndex) {
		if (isEmpty()) {
			return this;
		}

		assert scrollIndexCheck(newIndex);

		int capIndex = scrollIndexCap(newIndex);

		return withScrollIndex(capIndex).withDisplaySetStale().withElementSetStale();
	}

	public ListBox withSelectScrollIntoView() {
		if (isEmpty()) {
			return this;
		}

		if (!isSelectActive()) {
			return this;
		}

		int selectIndex = elementIdToIndex(selectId());

		RangeIntegerPositive displayRange = displayListIndexRange();
		if (displayRange.isContain(selectIndex)) {
			// already visible
			return this;
		}

		if (selectIndex < displayRange.start) {
			// item is above, scroll up to it
			return withScrollIndexSetCap(selectIndex);
		} else {
			// below
			int proposedScrollIndex = (selectIndex - elementPerPage()) + 1;
			return withScrollIndexSetCap(proposedScrollIndex);
		}
	}

	public ListBox withResize(DisplaySize size) {
		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent).withDisplaySetStale().withElementSetStale();
	}

	public DisplaySize minimumSize(DisplayPadding elementPadding) {
		// at least a single line with a wide character on it
		int x = font.characterSize('W').get().x + elementPadding.xTotal();

		int y = font.characterSize('W').get().y + elementPadding.yTotal();

		y = Math.max(y, itemSizeY);

		return DisplaySize.of(x, y);
	}

	public ListBox withClick(DisplayPosition clickPosition) {
		if (!isAutomaticSelect) {
			return this;
		}

		assert clickPosition != null;

		if (!controlPositionCheck(clickPosition)) {
			// click outside control
			return this;
		}

		// check if an element is under mouse
		Optional<Integer> clickIndex = controlPositionToIndex(clickPosition);
		if (!clickIndex.isPresent()) {
			return this;
		}

		int selectId = elementList.get(clickIndex.get()).id;

		return withSelectId(Optional.of(selectId)).withDisplaySetStale().withElementSetStale();
	}

	public ListBox withActionSelect(int elementId) {
		assert elementList.idCheck(elementId);

		return withSelectId(Optional.of(elementId)).withDisplaySetStale().withElementSetStale();
	}

	public ListBox withElementAdd(ListBoxElement element) {
		// need to build new list and also fix bad scroll index before creating
		// new object
		ListBoxElementList newElementList = elementList.withJoinItem(element);

		return new ListBox(newElementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent).withDisplaySetStale().withElementSetStale();
	}

	protected ListBox withElementList(ListBoxElementList elementList) {
		return new ListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, font,
				displayComponent);
	}

	public boolean scrollIndexCheck(int item) {
		if (isEmpty()) {
			return false;
		}

		return (item >= 0) && (item <= scrollIndexMaximum());
	}

	protected int scrollIndexCap(int uncapped) {
		assert !isEmpty();

		if (uncapped < 0) {
			return 0;
		}

		return Math.min(uncapped, scrollIndexMaximum());
	}

	public int elementPerPage() {
		// ensure it rounds down
		return size.y / itemSizeY;
	}

	protected int scrollIndexMaximum() {
		assert !isEmpty();

		if (elementListSize() <= elementPerPage()) {
			return 0;
		}

		int result = elementListSize() - elementPerPage();
		assert elementIndexCheck(result);

		return result;
	}

	public boolean elementIndexCheck(int item) {
		return elementList.indexCheck(item);
	}

	public boolean isContainElementId(int elementId) {
		return elementList.idCheck(elementId);
	}

	public ListBoxElement elementGetById(int elementId) {
		assert isContainElementId(elementId);

		int index = elementIdToIndex(elementId);

		return elementGet(index);
	}

	public ListBox withElementSetById(ListBoxElement element) {
		assert isContainElementId(element.id);

		int index = elementIdToIndex(element.id);

		return withElementList(elementList.withReplaceIndexWithItem(index, element));
	}

	public int elementListSize() {
		return elementList.size();
	}

	protected boolean controlPositionYCheck(int item) {
		return (item >= 0) && (item < size.y);
	}

	public boolean controlPositionCheck(DisplayPosition item) {
		return DisplayBound.ofSize(size).contains(item);
	}

	protected boolean contentPositionCheck(DisplayPosition item) {
		return DisplayBound.ofSize(contentSize()).contains(item);
	}

	protected boolean contentPositionYCheck(int item) {
		return (item >= 0) && (item < contentSize().y);
	}

	public DisplaySize contentSize() {
		assert !isEmpty();

		int sizeY = elementListSize() * itemSizeY;

		return DisplaySize.of(size.x, sizeY);
	}

	/**
	 * Empty if no element under mouse.
	 */
	public Optional<Integer> controlPositionToIndex(DisplayPosition controlPosition) {
		assert controlPositionCheck(controlPosition);

		if (isEmpty()) {
			return Optional.empty();
		}

		int contentPositionY = controlPosition.y + scrollPositionY();

		if (contentPositionY < 0) {
			// above all elements
			return Optional.empty();
		}

		if (contentPositionY >= contentSize().y) {
			// below
			return Optional.empty();
		}

		return Optional.of(contentPositionYToIndex(contentPositionY));
	}

	protected int contentPositionYToIndex(int contentPositionY) {
		assert contentPositionYCheck(contentPositionY);
		assert !isEmpty();

		// round down
		int index = contentPositionY / itemSizeY;
		assert elementIndexCheck(index);
		return index;
	}

	protected DisplayPosition controlPositionToContent(DisplayPosition controlPosition) {
		DisplayPosition contentPosition = DisplayPosition.of(controlPosition.x,
				controlPositionYToContent(controlPosition.y));
		assert contentPositionCheck(contentPosition);
		return contentPosition;
	}

	protected int controlPositionYToContent(int controlPositionY) {
		assert controlPositionYCheck(controlPositionY);

		int contentPositionY = controlPositionY + scrollPositionY();
		assert contentPositionYCheck(contentPositionY);
		return contentPositionY;
	}

	protected DisplayPosition contentPositionToControl(DisplayPosition contentPosition) {
		///// sometimes negative

		DisplayPosition controlPosition = DisplayPosition.of(contentPosition.x,
				contentPositionYToControl(contentPosition.y));
		assert controlPositionCheck(controlPosition);
		return controlPosition;
	}

	protected int contentPositionYToControl(int contentPositionY) {
		assert contentPositionYCheck(contentPositionY);

		int controlPositionY = contentPositionY - scrollPositionY();
		assert controlPositionYCheck(controlPositionY);
		return controlPositionY;
	}

	protected int elementPositionYContent(int index) {
		return itemSizeY * index;
	}

	protected int scrollPositionY() {
		return itemSizeY * scrollIndex;
	}

	public boolean isEmpty() {
		return elementList.isEmpty();
	}

	protected ListBoxElementList displayList() {
		RangeIntegerPositive indexRange = displayListIndexRange();

		ListBoxElementList result = elementList.withSubset(indexRange);

		assert result.size() <= elementPerPage();

		return result;
	}

	protected RangeIntegerPositive displayListIndexRange() {
		if (isEmpty()) {
			return RangeIntegerPositive.of(0, 0);
		}

		int start = scrollIndex;

		int endExclusive = Math.min(start + elementPerPage(), elementListSize());

		return RangeIntegerPositive.of(start, endExclusive);
	}

	public int scrollIndex() {
		return scrollIndex;
	}

	public ListBoxElement elementGet(int index) {
		return elementList.get(index);
	}

	public boolean isSelectActive() {
		return selectId.isPresent();
	}

	public int selectId() {
		assert isSelectActive();

		return selectId.get();
	}

	public ListImmutableString elementText() {
		ListImmutableString.Builder builder = ListImmutableString.Builder.of();

		for (ListBoxElement element : elementList) {
			builder.add(element.text);
		}

		return builder.build();
	}

	public int elementIdToIndex(int elementId) {
		assert elementList.idCheck(elementId);

		return elementList.idToIndex(elementId).get();
	}

	public int elementIndexToId(int index) {
		assert elementList.indexCheck(index);

		return elementList.get(index).id;
	}
}