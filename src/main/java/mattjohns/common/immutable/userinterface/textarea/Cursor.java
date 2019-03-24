package mattjohns.common.immutable.userinterface.textarea;

import java.util.Optional;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

/**
 * This text cursor is different than a mouse cursor. A text cursor sits at a
 * certain location in the document. Arrow keys move the cursor regardless of
 * what the viewport scroll offset is.
 * 
 * The cursor is able to be over any symbol and can also sit at the end of a
 * line, after all the characters. But that is only true if the line is not
 * wrapped, in which case the cursor must sit on the last character, never the
 * end of line.
 */
public class Cursor extends Immutable<Cursor> {
	/**
	 * Any viewport location that maps to a valid document location. So either
	 * over a symbol, or on end of line if the viewport line holds the last
	 * segment of the document line (i.e. ViewportLine.isWrap == false).
	 */
	public final ViewportLocation location;

	/**
	 * If up or down arrow is pressed, the x position is stored so that future
	 * vertical movements know where to go. Automatically emptied whenever a
	 * non-vertical cursor movement is performed.
	 * 
	 * Without this the cursor moves wrongly if you go up and down between
	 * lines. The cursor will slowly drift to the left.
	 * 
	 * This position is relative to the start of the line, not the start of the
	 * whole text area control.
	 */
	protected Optional<Integer> verticalDisplayPositionX;

	public final CursorDisplayComponent displayComponent;

	protected Cursor(ViewportLocation location, Optional<Integer> verticalDisplayPositionX,
			CursorDisplayComponent displayComponent) {
		this.location = location;
		this.verticalDisplayPositionX = verticalDisplayPositionX;
		this.displayComponent = displayComponent;

		assert this.location != null;

		assert this.verticalDisplayPositionX != null;

		if (this.verticalDisplayPositionX.isPresent()) {
			assert this.verticalDisplayPositionX.get() >= 0;
		}

		assert this.displayComponent != null;
	}

	public static Cursor of(DisplaySize emptySymbolSize) {
		return new Cursor(ViewportLocation.of(), Optional.empty(), CursorDisplayComponent.of(emptySymbolSize));
	}

	@Override
	protected Cursor concreteCopy(Immutable<?> source) {
		return new Cursor(location, verticalDisplayPositionX, displayComponent);
	}

	public Cursor withLocation(ViewportLocation location) {
		return new Cursor(location, verticalDisplayPositionX, displayComponent);
	}

	public Cursor withDisplayComponent(CursorDisplayComponent displayComponent) {
		return new Cursor(location, verticalDisplayPositionX, displayComponent);
	}

	protected Cursor withVerticalDisplayPositionX(int verticalDisplayPositionX) {
		return new Cursor(location, Optional.of(verticalDisplayPositionX), displayComponent);
	}

	protected Cursor withVerticalDisplayPositionXClear() {
		return new Cursor(location, Optional.empty(), displayComponent);
	}

	public Cursor withMoveLeft(ViewportDocument viewportDocument) {
		Optional<ViewportLocation> newCursorLocation = locationLeft(location, viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionXClear();
		}
		else {
			return this;
		}
	}

	public Cursor withMoveRight(ViewportDocument viewportDocument) {
		Optional<ViewportLocation> newCursorLocation = locationRight(location, viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionXClear();
		}
		else {
			return this;
		}
	}

	public int lineDisplayPositionX(ViewportDocument viewportDocument) {
		if (verticalDisplayPositionX.isPresent()) {
			return verticalDisplayPositionX.get();
		}
		else {
			ViewportLine sourceLine = viewportDocument.lineGet(location.line);
			assert sourceLine != null;

			return sourceLine.locationToPosition(location.symbol);
		}
	}

	public Cursor withMoveUp(ViewportDocument viewportDocument) {
		int positionX = lineDisplayPositionX(viewportDocument);

		Optional<ViewportLocation> newCursorLocation = locationVertical(location, -1, positionX, viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionX(positionX);
		}
		else {
			return this;
		}
	}

	public Cursor withMoveDown(ViewportDocument viewportDocument) {
		int positionX = lineDisplayPositionX(viewportDocument);

		Optional<ViewportLocation> newCursorLocation = locationVertical(location, 1, positionX, viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionX(positionX);
		}
		else {
			return this;
		}
	}

	public Cursor withMoveHome(boolean isDocument, ViewportDocument viewportDocument) {
		Optional<ViewportLocation> newCursorLocation = locationHome(location, isDocument, viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionXClear();
		}
		else {
			return this;
		}
	}

	public Cursor withMoveEnd(boolean isDocument, ViewportDocument viewportDocument) {
		Optional<ViewportLocation> newCursorLocation = locationEnd(location, isDocument, viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionXClear();
		}
		else {
			return this;
		}
	}

	public Cursor withMovePageUp(int pageLineSize, ViewportDocument viewportDocument) {
		int positionX = lineDisplayPositionX(viewportDocument);

		Optional<ViewportLocation> newCursorLocation = locationVertical(location, pageLineSize * -1, positionX,
				viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionX(positionX);
		}
		else {
			return this;
		}
	}

	public Cursor withMovePageDown(int pageLineSize, ViewportDocument viewportDocument) {
		int positionX = lineDisplayPositionX(viewportDocument);

		Optional<ViewportLocation> newCursorLocation = locationVertical(location, pageLineSize * 1, positionX,
				viewportDocument);
		if (newCursorLocation.isPresent()) {
			return withLocation(newCursorLocation.get()).withVerticalDisplayPositionX(positionX);
		}
		else {
			return this;
		}
	}

	@Override
	public String toString() {
		return location.toString();
	}

	/**
	 * Empty location if not valid location to move to.
	 */
	public Optional<ViewportLocation> locationLeft(ViewportLocation sourceCursorLocation,
			ViewportDocument viewportDocument) {
		assert viewportDocument.locationCheck(sourceCursorLocation);

		if (sourceCursorLocation.symbol > 0) {
			// middle of line, just go left
			ViewportLocation left = sourceCursorLocation.withSymbol(sourceCursorLocation.symbol - 1);
			assert viewportDocument.locationCheck(left);

			return Optional.of(left);
		}

		// start of line, go to end of line above
		if (sourceCursorLocation.line == 0) {
			// nothing above, give up
			return Optional.empty();
		}

		int aboveLineIndex = sourceCursorLocation.line - 1;

		int aboveSymbolLocation = viewportDocument.lineGetEndLocation(aboveLineIndex);

		ViewportLocation aboveLocation = sourceCursorLocation.withLocation(aboveLineIndex, aboveSymbolLocation);

		assert viewportDocument.locationCheck(aboveLocation);

		return Optional.of(aboveLocation);
	}

	/**
	 * Empty location if not valid location to move to.
	 */
	public Optional<ViewportLocation> locationRight(ViewportLocation sourceCursorLocation,
			ViewportDocument viewportDocument) {
		assert sourceCursorLocation != null;
		assert viewportDocument.locationCheck(sourceCursorLocation);

		int lastValidSymbolLocation = viewportDocument.lineGetEndLocation(sourceCursorLocation.line);

		ViewportLocation newLocation;
		if (sourceCursorLocation.symbol >= lastValidSymbolLocation) {
			// at end of line, go down
			if (sourceCursorLocation.line == viewportDocument.lineListSize() - 1) {
				// at bottom of document
				return Optional.empty();
			}
			else {
				// still lines below
				newLocation = sourceCursorLocation.withLocation(sourceCursorLocation.line + 1, 0);
			}
		}
		else {
			// valid locations to the right, just move
			newLocation = sourceCursorLocation.withSymbol(sourceCursorLocation.symbol + 1);
			assert viewportDocument.locationCheck(newLocation);
		}

		return Optional.of(newLocation);
	}

	protected Optional<ViewportLocation> locationVertical(ViewportLocation sourceCursorLocation, int deltaLineIndex,
			int desiredPositionX, ViewportDocument viewportDocument) {
		assert sourceCursorLocation != null;
		assert viewportDocument.locationCheck(sourceCursorLocation);

		int destinationIndex = sourceCursorLocation.line + deltaLineIndex;
		if (!viewportDocument.lineIndexCheck(destinationIndex)) {
			// trying to move above or below valid lines
			destinationIndex = viewportDocument.lineIndexCap(destinationIndex);
			// return Optional.empty();
		}

		ViewportLine destinationLine = viewportDocument.lineGet(destinationIndex);

		// check against full document width, ignoring individual line widths
		assert viewportDocument.positionXCheck(desiredPositionX);

		int symbolLocation = destinationLine.positionToLocationNearest(desiredPositionX);

		ViewportLocation newLocation = sourceCursorLocation.withLocation(destinationIndex, symbolLocation);

		return Optional.of(newLocation);
	}

	protected Optional<ViewportLocation> locationHome(ViewportLocation sourceCursorLocation, boolean isDocument,
			ViewportDocument viewportDocument) {
		assert sourceCursorLocation != null;

		ViewportLocation newLocation;
		if (isDocument) {
			// top left
			newLocation = ViewportLocation.of();
		}
		else {
			// left of line
			newLocation = sourceCursorLocation.withSymbol(0);
		}

		return Optional.of(newLocation);
	}

	protected Optional<ViewportLocation> locationEnd(ViewportLocation sourceCursorLocation, boolean isDocument,
			ViewportDocument viewportDocument) {
		assert sourceCursorLocation != null;

		int newLineIndex;
		if (isDocument) {
			// end of document
			newLineIndex = viewportDocument.lineListSize() - 1;
		}
		else {
			// left of line
			newLineIndex = sourceCursorLocation.line;
		}

		assert viewportDocument.lineIndexCheck(newLineIndex);

		int newSymbolIndex = viewportDocument.lineGetEndLocation(newLineIndex);

		ViewportLocation newLocation = new ViewportLocation(newLineIndex, newSymbolIndex);

		return Optional.of(newLocation);
	}

	public Cursor withDisplayComponentUpdate(Viewport viewport, DisplayPosition containerPosition) {
		if (!displayComponent.isStale) {
			return this;
		}

		boolean cursorIsVisible = cursorCheckIfInView(viewport, location);

		// If cursor is scrolled off-screen, don't attempt to convert it to a
		// display position.
		DisplayPosition cursorPosition;
		if (cursorIsVisible) {
			cursorPosition = viewport.locationToControlPosition(location);
		}
		else {
			cursorPosition = DisplayPosition.Zero;
		}

		DisplaySize cursorSize;
		if (viewport.symbolCheck(location)) {
			// cursor is on a symbol
			cursorSize = viewport.symbolGet(location).fontSymbol.size;
		}
		else {
			// cursor is at end of line
			cursorSize = displayComponent.emptySymbolSize;
		}

		DisplayBound bound = DisplayBound.of(cursorPosition, cursorSize);

		return withDisplayComponent(displayComponent.withBound(bound)
				.withContainerPosition(containerPosition)
				.withIsVisible(cursorIsVisible)
				.withIsStale(false));
	}

	protected boolean cursorCheckIfInView(Viewport viewport, ViewportLocation cursorLocation) {
		assert cursorLocation != null;

		if (!viewport.locationCheck(cursorLocation)) {
			return false;
		}

		RangeIntegerPositive displayRange = viewport.lineDisplayListRange();

		if (!displayRange.isContain(cursorLocation.line)) {
			return false;
		}

		return true;
	}

	public Cursor withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}
}
