package mattjohns.common.immutable.userinterface.textarea;

import java.util.Optional;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;
import mattjohns.common.immutable.userinterface.font.FontSymbol;
import mattjohns.common.text.TextUtility;

public final class TextArea extends Immutable<TextArea> {
	public final Viewport viewport;
	public final Cursor cursor;
	public final TextAreaDisplayComponent displayComponent;

	protected TextArea(Viewport viewport, Cursor cursor, TextAreaDisplayComponent displayComponent) {
		this.viewport = viewport;
		this.cursor = cursor;
		this.displayComponent = displayComponent;

		assert this.viewport != null;
		assert this.cursor != null;
		assert this.displayComponent != null;

		assert locationCheck(this.cursor.location);
	}

	public static TextArea of(Font font, DisplaySize size, int lineSizeY, DisplayPadding contentPadding,
			boolean isReadOnly, Optional<Integer> maximumLineSize, boolean isWrap, boolean isSizeXLimit) {
		FontSymbol cursorEmptySymbol = font.symbolGetByCharacter(' ');
		assert cursorEmptySymbol != null : "Font does not contain 'space' character.";

		// padding on the right of text area so cursor can sit on end of line.";
		Cursor cursor = Cursor.of(cursorEmptySymbol.size);

		TextAreaDisplayComponent displayComponent = TextAreaDisplayComponent.of(size, lineSizeY);

		// allow cursor to sit on end of line and still be within content bound
		int wrapPaddingX = cursorEmptySymbol.size.x;

		Viewport viewport = Viewport.of(font, size, lineSizeY, wrapPaddingX, contentPadding, isReadOnly,
				maximumLineSize, isWrap, isSizeXLimit);

		return new TextArea(viewport, cursor, displayComponent);
	}

	@Override
	protected TextArea concreteCopy(Immutable<?> source) {
		return new TextArea(viewport, cursor, displayComponent);
	}

	protected TextArea withViewportDirect(Viewport viewport) {
		assert viewport != null;
		assert viewport.locationCheck(cursor.location);

		return new TextArea(viewport.withDisplaySetStale(), cursor.withDisplaySetStale(), displayComponent)
				.withDisplaySetStale();
	}

	/**
	 * Cursor must still be valid for new viewport.
	 */
	protected TextArea withViewportNoCursorChangeStale(Viewport viewport) {
		assert viewport != null;
		assert viewport.locationCheck(cursor.location);

		return new TextArea(viewport.withDisplaySetStale(), cursor.withDisplaySetStale(), displayComponent)
				.withDisplaySetStale();
	}

	protected TextArea withViewportStale(Viewport viewport, ViewportLocation cursorLocation) {
		assert viewport != null;
		assert cursorLocation != null;

		Cursor cursor = this.cursor.withLocation(cursorLocation);

		return new TextArea(viewport.withDisplaySetStale(), cursor.withDisplaySetStale(), displayComponent)
				.withDisplaySetStale();
	}

	protected TextArea withCursorDirect(Cursor cursor) {
		return new TextArea(viewport, cursor, displayComponent);
	}

	protected TextArea withCursorStale(Cursor cursor) {
		return new TextArea(viewport, cursor.withDisplaySetStale(), displayComponent);
	}

	protected TextArea withDisplayComponent(TextAreaDisplayComponent displayComponent) {
		return new TextArea(viewport, cursor, displayComponent);
	}

	protected TextArea withPosition(DisplayPosition position) {
		return new TextArea(viewport, cursor, displayComponent).withDisplaySetStale();
	}

	public TextArea withResize(DisplayPosition position, DisplaySize desiredSize) {
		DisplaySize effectiveSize = desiredSize.withMaximum(minimumSize());

		Viewport newViewport = this.viewport.withDisplayResize(effectiveSize);

		ViewportLocation newCursorLocation = cursorFixIfBad(newViewport);

		return withPosition(position).withViewportStale(newViewport, newCursorLocation);
	}

	// fixes bad location when text changes, should only happen during file
	// load or maybe after a large selection delete
	protected ViewportLocation cursorFixIfBad(Viewport viewport) {
		if (!viewport.locationCheck(cursor.location)) {
			return ViewportLocation.of(0, 0);
		} else {
			return cursor.location;
		}
	}

	public TextArea withMasterDocument(Document masterDocument) {
		Viewport newViewport = this.viewport.withMasterDocument(masterDocument);

		ViewportLocation newCursorLocation = cursorFixIfBad(newViewport);

		return withViewportStale(newViewport, newCursorLocation);
	}

	public TextArea withReadOnlySet(boolean isReadOnly) {
		Viewport newViewport = this.viewport.withReadOnlySet(isReadOnly);

		return withViewportNoCursorChangeStale(newViewport);
	}

	public Document masterDocument() {
		return viewport.masterDocument;
	}

	public Font font() {
		return viewport.font;
	}

	public int lineSizeY() {
		return viewport.lineSizeY();
	}

	public TextArea withDisplayComponentUpdate() {
		TextArea newTextArea;

		if (displayComponent.isStale) {
			DisplayBound bound = DisplayBound.ofSize(size());

			// no container for this root display component (TextArea class)
			newTextArea = withDisplayComponent(displayComponent.withBound(bound)
					.withContainerPosition(DisplayPosition.Zero).withIsVisible(true).withIsStale(false));
		} else {
			// not stale so the above is already done
			newTextArea = this;
		}

		DisplayPosition newTextAreaPosition = newTextArea.displayComponent.bound.topLeft;

		// update child components
		Viewport newViewport = viewport.withDisplayComponentUpdate(newTextAreaPosition);

		Cursor newCursor = cursor.withDisplayComponentUpdate(viewport, newTextAreaPosition);

		return newTextArea.withViewportDirect(newViewport).withCursorDirect(newCursor);
	}

	public TextArea withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}

	protected boolean isReadOnly() {
		return viewport.isReadOnly;
	}

	public TextArea withActionCharacter(char character) {
		if (isReadOnly()) {
			return this;
		}

		assert locationCheck(cursor.location);

		String documentText = Character.toString(character);

		Viewport.ResultInsert viewportResult = viewport.withActionInsert(cursor.location, documentText);
		if (!viewportResult.isChange) {
			// unable to insert
			return this;
		}

		ViewportLocation newCursorLocation = viewportResult.viewportLocation.end;

		return withViewportStale(viewportResult.self, newCursorLocation).withCursorScrollIntoView();
	}

	public TextArea withActionTab() {
		if (isReadOnly()) {
			return this;
		}

		return withActionCharacter(TextUtility.TAB);
	}

	public TextArea withActionEnter() {
		if (isReadOnly()) {
			return this;
		}

		return withActionCharacter(TextUtility.CARRIAGE_RETURN);
	}

	public TextArea withActionDelete() {
		if (isReadOnly()) {
			return this;
		}

		assert locationCheck(cursor.location);

		Optional<ViewportLocation> deleteEndLocation = cursor.locationRight(cursor.location, viewportDocument());
		if (!deleteEndLocation.isPresent()) {
			// backspace blocked
			return this;
		}

		ViewportLocationRange deleteRange = ViewportLocationRange.of(cursor.location, deleteEndLocation.get());

		Viewport.ResultDelete viewportResult = viewport.withActionDelete(deleteRange);

		ViewportLocation newCursorLocation = viewportResult.viewportLocation;

		return withViewportStale(viewportResult.self, newCursorLocation);
	}

	public TextArea withActionBackspace() {
		if (isReadOnly()) {
			return this;
		}

		assert locationCheck(cursor.location);

		Optional<ViewportLocation> deleteLocation = cursor.locationLeft(cursor.location, viewportDocument());
		if (!deleteLocation.isPresent()) {
			// backspace blocked
			return this;
		}

		ViewportLocationRange deleteRange = ViewportLocationRange.of(deleteLocation.get(), cursor.location);

		Viewport.ResultDelete viewportResult = viewport.withActionDelete(deleteRange);

		ViewportLocation newCursorLocation = viewportResult.viewportLocation;

		return withViewportStale(viewportResult.self, newCursorLocation);
	}

	public TextArea withActionScrollDelta(int deltaLine) {
		return withViewportNoCursorChangeStale(viewport.withScrollDeltaCap(deltaLine)).withCursorStale(cursor);
	}

	public TextArea withActionScrollUp() {
		return withViewportNoCursorChangeStale(viewport.withScrollDeltaCap(-1)).withCursorStale(cursor);
	}

	public TextArea withActionScrollDown() {
		return withViewportNoCursorChangeStale(viewport.withScrollDeltaCap(1)).withCursorStale(cursor);
	}

	public TextArea withActionScrollPageUp() {
		int delta = pageLineSize() * -1;
		return withViewportNoCursorChangeStale(viewport.withScrollDeltaCap(delta)).withCursorStale(cursor);
	}

	public TextArea withActionScrollPageDown() {
		int delta = pageLineSize();

		return withViewportNoCursorChangeStale(viewport.withScrollDeltaCap(delta)).withCursorStale(cursor);
	}

	public TextArea withActionCursorMoveLeft() {
		return withCursorStale(cursor.withMoveLeft(viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionCursorMoveRight() {
		return withCursorStale(cursor.withMoveRight(viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionCursorMoveUp() {
		return withCursorStale(cursor.withMoveUp(viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionCursorMoveDown() {
		return withCursorStale(cursor.withMoveDown(viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionCursorMoveHome(boolean isDocument) {
		return withCursorStale(cursor.withMoveHome(isDocument, viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionCursorMoveEnd(boolean isDocument) {
		return withCursorStale(cursor.withMoveEnd(isDocument, viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionCursorMovePageUp() {
		return withCursorStale(cursor.withMovePageUp(pageLineSize(), viewportDocument())).withCursorScrollIntoView();
	}

	/// need to keep cursor at the same screen position
	public TextArea withActionCursorMovePageDown() {
		return withCursorStale(cursor.withMovePageDown(pageLineSize(), viewportDocument())).withCursorScrollIntoView();
	}

	public TextArea withActionScrollToIndex(int index) {
		return withViewportNoCursorChangeStale(viewport.withScrollIndex(index));
	}

	protected TextArea withCursorScrollIntoView() {
		return withViewportNoCursorChangeStale(viewport.withScrollIntoView(cursor.location.line));
	}

	public TextArea withUserActionClick(DisplayPosition clickPosition) {
		assert clickPosition != null;

		if (!viewport.controlPositionCheck(clickPosition)) {
			// click outside control
			return this;
		}

		// set cursor to nearest location
		ViewportLocation newCursorLocation = viewport.controlPositionToLocationCap(clickPosition);
		assert locationCheck(newCursorLocation);

		return withCursorStale(cursor.withLocation(newCursorLocation));
	}

	public ViewportLine lineGet(int index) {
		return viewport.lineGet(index);
	}

	public DisplaySize size() {
		// text area is always same as viewport
		return viewport.size;
	}

	public boolean locationCheck(ViewportLocation viewLocation) {
		return viewport.locationCheck(viewLocation);
	}

	@Override
	public String toString() {
		String result = "";

		result += viewport.toString();

		result += ", Cursor: " + cursor.toString();

		return result;
	}

	public ViewportDocument viewportDocument() {
		return viewport.viewportDocument;
	}

	public int lineListSize() {
		return viewport.lineListSize();
	}

	public int scrollIndex() {
		return viewport.scrollIndex;
	}

	public int pageLineSize() {
		return viewport.pageLineSize();
	}

	public DisplayPadding contentPadding() {
		return viewport.contentPadding;
	}

	public boolean isModify() {
		return viewport.isModify;
	}

	public DisplaySize minimumSize() {
		// at least a single line with a wide character on it
		int largeCharacterSizeX = font().characterSize('W').get().x + viewport.viewportDocument.wrapPaddingX;

		return DisplaySize.of(largeCharacterSizeX + contentPadding().xTotal(), lineSizeY() + contentPadding().yTotal());
	}

	protected enum EnumDisplayComponent {
		TextArea, ViewportContent, Cursor,
	}
}