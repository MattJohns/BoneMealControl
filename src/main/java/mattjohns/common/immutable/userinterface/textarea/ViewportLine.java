package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;

/**
 * A wrapped line in the viewport.
 * 
 * A viewport line maps 1-to-1 to a segment of a (unwrapped) 'master' line.
 * 
 * The end *location* of the document line is also mapped. So wrapped lines do
 * not have a 'one past end' location. For example the cursor is only allowed to
 * sit on the end of unwrapped viewport lines, never on the end of a wrapped
 * viewport line.
 * 
 * The mapping from viewport to document location is done using
 * masterLocationStart and text.size() . The mapping allows you to translate
 * between wrapped and unwrapped locations (i.e. ViewportLocation and
 * DocumentLocation).
 */
public final class ViewportLine extends Immutable<ViewportLine> {
	/**
	 * The is the start of the document line segment that this viewport line
	 * maps to. It is inclusive.
	 */
	public final DocumentLocation masterLocationStart;

	/**
	 * The symbols that map 1-to-1 with the document line segment. The size of
	 * this is used to determine the length of the segment in the master
	 * document.
	 * 
	 * Note the segment never spans across multiple master lines.
	 */
	public final ViewportSymbolList text;

	/**
	 * The master doesn't fit in this wrapped line. There will be other viewport
	 * lines after this one that hold the rest of the master line.
	 * 
	 * This flag also reflects whether this line segment holds the end part of
	 * the master line. If it's false then it is the end part, otherwise it's
	 * some segment in the middle of the master line.
	 */
	public final boolean isWrap;

	protected ViewportLine(DocumentLocation masterLocationStart, ViewportSymbolList text, boolean isWrap) {
		this.masterLocationStart = masterLocationStart;
		this.text = text;
		this.isWrap = isWrap;

		assert this.masterLocationStart != null;
		assert this.text != null;

		assert !this.isWrap || !this.text.isEmpty() : "Should be impossible for an empty line to need wrapping.";
	}

	/**
	 * Empty and not wrapped.
	 */
	public static ViewportLine of() {
		return new ViewportLine(DocumentLocation.of(0, 0), ViewportSymbolList.of(), false);
	}

	public static ViewportLine of(DocumentLocation masterLocationStart, ViewportSymbolList text, boolean isWrap) {
		return new ViewportLine(masterLocationStart, text, isWrap);
	}

	@Override
	protected ViewportLine concreteCopy(Immutable<?> source) {
		return new ViewportLine(masterLocationStart, text, isWrap);
	}

	public ViewportLine withMasterLocationStart(DocumentLocation masterLocationStart) {
		return new ViewportLine(masterLocationStart, text, isWrap);
	}

	public ViewportLine withText(ViewportSymbolList text) {
		return new ViewportLine(masterLocationStart, text, isWrap);
	}

	public ViewportLine withIsWrap(boolean isWrap) {
		return new ViewportLine(masterLocationStart, text, isWrap);
	}

	/**
	 * Useful for updating the mapping when you shift lines up or down in the
	 * source document (without doing anything else to them).
	 * 
	 * Ensure the new master location is valid.
	 */
	public ViewportLine withDeltaMasterLineIndex(int deltaLineIndex) {
		return withMasterLocationStart(masterLocationStart.withLineAdd(deltaLineIndex));
	}

	@Override
	public String toString() {
		return textStyle();
	}

	/**
	 * The exclusive end location of the mapping from viewport line to document
	 * line.
	 * 
	 * If the this line segment is the end part of the master line, then this
	 * also maps the end of line location. So for a line "abc", the end of line
	 * location is 3. And so this method will return a location of 4 (recall the
	 * end location is exclusive, so the 3 characters plus the end of line
	 * location are mapped).
	 */
	public DocumentLocation masterLocationEnd() {
		int masterLine = masterLocationStart.line;
		int masterSymbol = masterLocationStart.symbol + locationEnd() + 1;

		return DocumentLocation.of(masterLine, masterSymbol);
	}

	public DocumentLocationRange masterLocationRange() {
		return DocumentLocationRange.of(masterLocationStart, masterLocationEnd());
	}

	public boolean isEmpty() {
		return text.isEmpty();
	}

	/**
	 * End of line location is only valid when isWrap == false.
	 */
	public boolean locationCheck(int item) {
		return item >= 0 && item <= locationEnd();
	}

	/**
	 * One to the right of the last symbol, except for wrapped lines where the
	 * cursor must always be over a valid symbol.
	 * 
	 * Empty line always allows end location of 0 (empty lines are never wrapped
	 * anyway).
	 */
	public int locationEnd() {
		if (isWrap) {
			// don't allow end of line if wrapped

			// already know this in constructor
			assert !isEmpty();

			return symbolListSize() - 1;
		}
		else {
			// if not wrapped then allow on end of line
			return symbolListSize();
		}
	}

	/**
	 * Does not include end of line location. So empty line is 0.
	 */
	public int sizeX() {
		return text.displaySizeX();
	}

	/**
	 * @return Left pixel of symbol, relative to start of this line.
	 * 
	 * If at end of line then returns the farthest right pixel position of the
	 * whole line, plus 1 to the right. So effectively returns the left pixel of
	 * the imaginary end location symbol.
	 * 
	 * Must be a valid location.
	 */
	public int locationToPosition(int location) {
		assert locationCheck(location);

		if (symbolIndexCheck(location)) {
			// valid symbol index
			//
			// get combined width of all symbols up to this location
			int size = text.withSubset(0, location)
					.displaySizeX();

			// Position now points to next pixel after previous symbol's
			// right hand edge
			int position = size;

			assert positionCheck(position);
			return position;
		}
		else {
			// not a valid symbol, must be end of line location
			assert location == symbolListSize();

			// Left pixel of imaginary end location
			int position = sizeX();

			return position;
		}
	}

	/**
	 * Must be valid position within the bounds of the line's text.
	 */
	protected int positionToIndex(int positionX) {
		assert positionCheck(positionX);

		int currentSizeX = 0;

		int i;
		for (i = 0; i < symbolListSize(); i++) {
			ViewportSymbol symbol = text.get(i);

			currentSizeX += symbol.sizeX();

			// careful to compare a position to a size (< not <=)
			if (positionX < currentSizeX) {
				// inside the symbol at this index
				return i;
			}
		}

		assert false : "Position too far right despite being checked";
		return -1;
	}

	/**
	 * Left pixel of symbol under given position. If too far right then one
	 * pixel past last symbol's right side is returned.
	 */
	public int positionToLocationNearest(int positionX) {
		if (positionX < 0) {
			// too far left
			return 0;
		}
		else {
			if (positionCheck(positionX)) {
				// within the line (and not at end of line location)
				return positionToIndex(positionX);
			}
			else {
				// too far right, pull to end location
				return locationEnd();
			}
		}
	}

	/**
	 * Position must be over a symbol, end of line is not allowed.
	 */
	public boolean positionCheck(int positionX) {
		// be careful with off-by-one errors when comparing a position to a size
		return (positionX >= 0) && (positionX < sizeX());
	}

	public boolean symbolIndexCheck(int item) {
		return text.indexCheck(item);
	}

	public int symbolListSize() {
		return text.size();
	}

	public ViewportSymbol symbolGet(int index) {
		return text.get(index);
	}

	public String textNoStyle() {
		return text.textNoStyle();
	}

	public String textStyle() {
		return text.textStyle();
	}
}