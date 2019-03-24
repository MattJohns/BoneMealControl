package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;

/**
 * A document with display information such as line height, font, and other
 * sizes. Also provides automatic wrapping.
 */
final class ViewportDocument extends Immutable<ViewportDocument> {
	/**
	 * Holds a wrapped version of the master (i.e. non-wrapped) document.
	 * 
	 * Wrap is automatically derived when the master document changes.
	 */
	public final ViewportLineList lineList;

	/**
	 * 
	 */
	public final int wrapPaddingX;

	public final int sizeX;

	/**
	 * Height of each line.
	 */
	public final int lineSizeY;

	public final boolean isWrap;

	protected ViewportDocument(ViewportLineList lineList, int wrapPaddingX, int sizeX, int lineSizeY, boolean isWrap) {
		this.lineList = lineList;
		this.wrapPaddingX = wrapPaddingX;
		this.sizeX = sizeX;
		this.lineSizeY = lineSizeY;
		this.isWrap = isWrap;

		assert this.lineList != null;
		assert this.lineList.size() >= 1 : "Documents must always have at least one line.";

		assert this.wrapPaddingX > 0;
		assert this.wrapPaddingX < sizeX : "Wrap padding leaves less than one pixel for actual text.";

		assert this.sizeX > 0;
		assert this.lineSizeY > 0;
	}

	public static ViewportDocument of(Document masterDocument, int wrapPaddingX, int sizeX, int lineSizeY, Font font,
			boolean isWrap) {
		// auto wrap
		int wrapSizeX = sizeX - wrapPaddingX;
		ViewportLineList lineList;
		if (isWrap) {
			lineList = ViewportLineList.ofWrap(masterDocument, wrapSizeX, font);
		}
		else {
			lineList = ViewportLineList.ofNoWrap(masterDocument, font);
		}

		return new ViewportDocument(lineList, wrapPaddingX, sizeX, lineSizeY, isWrap);
	}

	@Override
	protected ViewportDocument concreteCopy(Immutable<?> source) {
		return new ViewportDocument(lineList, wrapPaddingX, sizeX, lineSizeY, isWrap);
	}

	protected ViewportDocument withDeriveFromMaster(Document masterDocument, Font font) {
		ViewportLineList lineList;
		if (isWrap) {
			lineList = ViewportLineList.ofWrap(masterDocument, wrapSizeX(), font);
		}
		else {
			lineList = ViewportLineList.ofNoWrap(masterDocument, font);
		}

		return new ViewportDocument(lineList, wrapPaddingX, sizeX, lineSizeY, isWrap);
	}

	public ViewportDocument withResize(Document masterDocument, int sizeX, Font font) {
		// just re-wrap
		int wrapSizeX = sizeX - wrapPaddingX;
		ViewportLineList lineList = ViewportLineList.ofWrap(masterDocument, wrapSizeX, font);

		return new ViewportDocument(lineList, wrapPaddingX, sizeX, lineSizeY, isWrap);
	}

	public int wrapSizeX() {
		return sizeX - wrapPaddingX;
	}

	public boolean isEmpty() {
		// An empty viewport document always has a single empty line.
		//
		// Note that a document which contains only styles and no text
		// will be classed as not empty. Although currently styles
		// are attached to characters so that situation is not possible.
		return lineListSize() == 1 && lineList.start()
				.isEmpty();
	}

	public int lineListSize() {
		return lineList.size();
	}

	public ViewportLine lineGet(int index) {
		return lineList.get(index);
	}

	public boolean lineIndexCheck(int index) {
		return lineList.indexCheck(index);
	}

	public int lineIndexCap(int index) {
		return lineList.indexCap(index);
	}

	public ViewportLineList lineSubset(RangeIntegerPositive indexRange) {
		return lineList.withSubset(indexRange.start, indexRange.end);
	}

	public int lineGetEndLocation(int viewportLine) {
		return lineGet(viewportLine).locationEnd();
	}

	public boolean symbolCheck(ViewportLocation location) {
		return lineList.symbolCheck(location);
	}

	public ViewportSymbol symbolGet(ViewportLocation location) {
		return lineList.symbolGet(location);
	}

	public boolean locationCheck(ViewportLocation location) {
		return lineList.locationCheck(location);
	}

	protected int sizeY() {
		return lineSizeY * lineListSize();
	}

	/**
	 * Dynamically calculated depending on number of lines.
	 */
	public DisplaySize size() {
		return DisplaySize.of(sizeX, sizeY());
	}

	/**
	 * Must be over a line and within document width (i.e. individual line width
	 * is ignored, but height isn't).
	 */
	public boolean positionCheck(DisplayPosition item) {
		assert item != null;

		return positionXCheck(item.x) && positionYCheck(item.y);
	}

	public boolean positionXCheck(int item) {
		// careful comparing position to size
		return (item >= 0) && (item < sizeX);
	}

	public boolean positionYCheck(int item) {
		// careful comparing position to size
		return (item >= 0) && (item < sizeY());
	}

	/**
	 * Position must be within document bounds.
	 * 
	 * If to the right of a line then the end of line location is returned. Must
	 * be on a valid line vertically though.
	 */
	public ViewportLocation positionToLocationCap(DisplayPosition position) {
		assert positionCheck(position);

		int lineIndex = positionYToLineIndex(position.y);
		ViewportLine line = lineGet(lineIndex);

		// symbol might not be
		int symbolLocation = line.positionToLocationNearest(position.x);

		ViewportLocation location = ViewportLocation.of(lineIndex, symbolLocation);
		assert locationCheck(location);
		return location;
	}

	/**
	 * Must be over a valid line.
	 */
	protected int positionYToLineIndex(int positionY) {
		assert positionYCheck(positionY);

		int line = positionY / lineSizeY;
		assert lineIndexCheck(line);
		return line;
	}

	/**
	 * Top left pixel of symbol.
	 */
	public DisplayPosition locationToPosition(ViewportLocation location) {
		return DisplayPosition.of(locationToPositionX(location), locationToPositionY(location.line));
	}

	/**
	 * Left pixel of symbol.
	 */
	protected int locationToPositionX(ViewportLocation location) {
		assert locationCheck(location);

		int positionX = lineGet(location.line).locationToPosition(location.symbol);
		assert positionXCheck(positionX);
		return positionX;
	}

	/**
	 * Top pixel of line.
	 */
	protected int locationToPositionY(int line) {
		assert lineIndexCheck(line);

		int positionY = lineSizeY * line;
		assert positionYCheck(positionY);
		return positionY;
	}

	protected int masterLocationToViewportLineIndex(DocumentLocation sourceLocation) {
		for (int i = 0; i < lineListSize(); i++) {
			/// use binary search
			if (lineGet(i).masterLocationRange()
					.contains(sourceLocation)) {
				return i;
			}
		}

		assert false : "Each master document location must have a single corresponding viewport location.";
		return -1;
	}

	public ViewportLocation masterLocationToViewport(DocumentLocation sourceLocation) {
		int viewportLineIndex = masterLocationToViewportLineIndex(sourceLocation);

		ViewportLine viewportLine = lineGet(viewportLineIndex);

		int viewportSymbolLocation = sourceLocation.symbol - viewportLine.masterLocationStart.symbol;
		assert viewportLine.locationCheck(viewportSymbolLocation);

		return ViewportLocation.of(viewportLineIndex, viewportSymbolLocation);
	}

	public DocumentLocation viewportLocationToMaster(ViewportLocation viewportLocation) {
		assert locationCheck(viewportLocation);

		ViewportLine viewportLine = lineGet(viewportLocation.line);

		int sourceLineIndex = viewportLine.masterLocationStart.line;

		int sourceSymbolLocation = viewportLine.masterLocationStart.symbol + viewportLocation.symbol;

		return DocumentLocation.of(sourceLineIndex, sourceSymbolLocation);
	}

	public DocumentLocationRange viewportLocationToMaster(ViewportLocationRange viewportLocationRange) {
		assert locationCheck(viewportLocationRange.start);
		assert locationCheck(viewportLocationRange.end);

		DocumentLocation masterStart = viewportLocationToMaster(viewportLocationRange.start);
		DocumentLocation masterEnd = viewportLocationToMaster(viewportLocationRange.end);

		return DocumentLocationRange.of(masterStart, masterEnd);
	}
}