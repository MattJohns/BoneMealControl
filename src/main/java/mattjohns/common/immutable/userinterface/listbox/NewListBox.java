package mattjohns.common.immutable.userinterface.listbox;

import java.util.Optional;

import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

public final class NewListBox {
	protected final NewListBoxElementList elementList;
	protected final int scrollIndex;
	protected final Optional<Integer> selectId;
	public final boolean isAutomaticSelect;

	public final DisplaySize size;
	public final int itemSizeY;
	public final NewListBoxDisplayComponent displayComponent;

	protected NewListBox(NewListBoxElementList elementList, int scrollIndex, Optional<Integer> selectId,
			boolean isAutomaticSelect, DisplaySize size, int itemSizeY, NewListBoxDisplayComponent displayComponent) {
		this.elementList = elementList;
		this.scrollIndex = scrollIndex;
		this.selectId = selectId;
		this.isAutomaticSelect = isAutomaticSelect;

		this.size = size;
		this.itemSizeY = itemSizeY;
		this.displayComponent = displayComponent;

		assert this.elementList != null;
		assert this.selectId != null;
		assert this.size != null;
		assert this.displayComponent != null;

		assert this.itemSizeY >= 1;

		assert this.size.y >= this.itemSizeY : "Not large enough for a single item.";

		if (isEmpty()) {
			assert this.scrollIndex == 0 : "Scroll not possible for empty list.";
			assert !this.selectId.isPresent() : "Selection not possible for empty list.";
		}
		else {
			assert scrollIndexCheck(this.scrollIndex);

			if (this.selectId.isPresent()) {
				assert elementIndexCheck(this.selectId.get());
			}
		}
	}

	public static NewListBox of(NewListBoxElementList itemList, DisplaySize size, int itemSizeY,
			boolean isAutomaticSelect) {
		int scrollIndex = 0;
		Optional<Integer> selectId = Optional.empty();

		NewListBoxDisplayComponent displayComponent = NewListBoxDisplayComponent.of(size);

		return new NewListBox(itemList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent);
	}

	public NewListBox withClear() {
		return new NewListBox(NewListBoxElementList.of(), 0, Optional.empty(), isAutomaticSelect, size, itemSizeY,
				displayComponent);
	}

	protected NewListBox withDisplayComponent(NewListBoxDisplayComponent displayComponent) {
		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent);
	}

	protected NewListBox withSelectId(Optional<Integer> selectId) {
		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent);
	}

	protected NewListBox withSelectClear() {
		return new NewListBox(elementList, scrollIndex, Optional.empty(), isAutomaticSelect, size, itemSizeY,
				displayComponent);
	}

	public NewListBox withTrySelectFirst() {
		if (isEmpty()) {
			return withSelectClear();
		}

		int firstId = elementList.start().id;

		return new NewListBox(elementList, scrollIndex, Optional.of(firstId), isAutomaticSelect, size, itemSizeY,
				displayComponent);
	}

	protected NewListBox withAutomaticSelect(boolean isAutomaticSelect) {
		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent);
	}

	public NewListBox withDisplayComponentUpdate() {
		if (!displayComponent.isStale) {
			return this;
		}

		NewListBoxElementList newElementList = elementDisplayComponentUpdate();

		NewListBox newListBox = withElementList(newElementList);

		newListBox = newListBox.withDisplayComponent(displayComponent.withBound(DisplayBound.ofSize(size))

				// position not used
				.withContainerPosition(DisplayPosition.Zero)

				// get visible elements
				.withDisplayList(newListBox.displayList())

				.withIsVisible(true)
				.withIsStale(false));

		// child components
		return newListBox;
	}

	protected NewListBoxElementList elementDisplayComponentUpdate() {
		NewListBoxElementList.Builder builder = NewListBoxElementList.Builder.of();

		RangeIntegerPositive displayListIndexRange = displayListIndexRange();

		for (int i = 0; i < elementListSize(); i++) {
			NewListBoxElement element = elementList.get(i);

			boolean isVisible = displayListIndexRange.isContain(i);

			DisplayPosition elementPosition;
			if (isVisible) {
				int elementPositionY = elementPositionYContent(i) - scrollPositionY();
				elementPosition = DisplayPosition.of(0, elementPositionY);
			}
			else {
				// not visible so doesn't matter
				elementPosition = DisplayPosition.of();
			}

			DisplayBound elementBound = DisplayBound.of(elementPosition, elementSize());

			boolean isSelect = selectId.isPresent() ? (selectId.get() == element.id) : false;

			NewListBoxElement newElement = element.withDisplayComponentUpdate(DisplayPosition.Zero, isVisible,
					elementBound, isSelect);

			builder.add(newElement);
		}

		return builder.build();
	}

	public NewListBox withElementListSet(NewListBoxElementList elementList) {
		int scrollIndex = 0;

		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent)
				.withDisplaySetStale()
				.withElementSetStale();
	}

	protected DisplaySize elementSize() {
		return DisplaySize.of(size.x, itemSizeY);
	}

	public int sizeX() {
		return size.x;
	}

	protected NewListBox withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}

	protected NewListBox withElementSetStale() {
		NewListBoxElementList.Builder builder = NewListBoxElementList.Builder.of();

		for (NewListBoxElement element : elementList) {
			builder.add(element.withDisplaySetStale());
		}

		return withElementList(builder.build());
	}

	public NewListBox withElementEnable(int id, boolean state) {
		NewListBoxElement element = elementGetById(id);

		element = element.withEnable(state);

		return withElementSetById(element);
	}

	protected NewListBox withScrollIndex(int scrollIndex) {
		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent);
	}

	public NewListBox withScrollDeltaCap(int delta) {
		if (isEmpty()) {
			return this;
		}

		int proposedScrollIndex = scrollIndex + delta;

		int capIndex = scrollIndexCap(proposedScrollIndex);

		return withScrollIndex(capIndex).withDisplaySetStale()
				.withElementSetStale();
	}

	public NewListBox withScrollUp() {
		return withScrollDeltaCap(-1);
	}

	public NewListBox withScrollDown() {
		return withScrollDeltaCap(+1);
	}

	public NewListBox withPageUp() {
		return withScrollDeltaCap(elementPerPage() * -1);
	}

	public NewListBox withPageDown() {
		return withScrollDeltaCap(elementPerPage() * +1);
	}

	public NewListBox withScrollIndexSetCap(int newIndex) {
		if (isEmpty()) {
			return this;
		}

		assert scrollIndexCheck(newIndex);

		int capIndex = scrollIndexCap(newIndex);

		return withScrollIndex(capIndex).withDisplaySetStale()
				.withElementSetStale();
	}

	public NewListBox withSelectScrollIntoView() {
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
		}
		else {
			// below
			int proposedScrollIndex = (selectIndex - elementPerPage()) + 1;
			return withScrollIndexSetCap(proposedScrollIndex);
		}
	}

	public NewListBox withResize(DisplaySize size) {
		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent)
				.withDisplaySetStale()
				.withElementSetStale();
	}

	public DisplaySize minimumSize(DisplayPadding elementPadding) {
		return DisplaySize.Tiny;///////
	}

	public NewListBox withClick(DisplayPosition clickPosition) {
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

		return withSelectId(Optional.of(selectId)).withDisplaySetStale()
				.withElementSetStale();
	}

	public NewListBox withActionSelect(int elementId) {
		assert elementList.idCheck(elementId);

		return withSelectId(Optional.of(elementId)).withDisplaySetStale()
				.withElementSetStale();
	}

	public NewListBox withElementAdd(NewListBoxElement element) {
		// need to build new list and also fix bad scroll index before creating
		// new object
		NewListBoxElementList newElementList = elementList.withJoinItem(element);

		return new NewListBox(newElementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY,
				displayComponent).withDisplaySetStale()
						.withElementSetStale();
	}

	protected NewListBox withElementList(NewListBoxElementList elementList) {
		return new NewListBox(elementList, scrollIndex, selectId, isAutomaticSelect, size, itemSizeY, displayComponent);
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

	public NewListBoxElement elementGetById(int elementId) {
		assert isContainElementId(elementId);

		int index = elementIdToIndex(elementId);

		return elementGet(index);
	}

	public NewListBox withElementSetById(NewListBoxElement element) {
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
		return DisplayBound.ofSize(size)
				.contains(item);
	}

	protected boolean contentPositionCheck(DisplayPosition item) {
		return DisplayBound.ofSize(contentSize())
				.contains(item);
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

	protected NewListBoxElementList displayList() {
		RangeIntegerPositive indexRange = displayListIndexRange();

		NewListBoxElementList result = elementList.withSubset(indexRange);

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

	public NewListBoxElement elementGet(int index) {
		return elementList.get(index);
	}

	public boolean isSelectActive() {
		return selectId.isPresent();
	}

	public int selectId() {
		assert isSelectActive();

		return selectId.get();
	}

	public int elementIdToIndex(int elementId) {
		assert elementList.idCheck(elementId);

		return elementList.idToIndex(elementId)
				.get();
	}

	public int elementIndexToId(int index) {
		assert elementList.indexCheck(index);

		return elementList.get(index).id;
	}
}