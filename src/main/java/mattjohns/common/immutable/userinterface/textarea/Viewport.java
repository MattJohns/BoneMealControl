package mattjohns.common.immutable.userinterface.textarea;

import java.util.Optional;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.ResultBase;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;
import mattjohns.common.immutable.userinterface.display.DisplayBound;
import mattjohns.common.immutable.userinterface.display.DisplayPadding;
import mattjohns.common.immutable.userinterface.display.DisplayPosition;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;

/**
 * The actual text area but without things like cursor, selection box, scroll
 * bar.
 * 
 * Allows padding for the rectangle that text is drawn in, referred to as
 * 'content'.
 */
public final class Viewport extends Immutable<Viewport> {
	/**
	 * Main document that stores the text and style information. All operations
	 * are first performed on the master document, then the wrapped document
	 * (ViewportDocument) is automatically derived from it.
	 */
	public final Document masterDocument;

	/**
	 * The document displayed by this viewport. Contains a wrapped version of
	 * master document and display-specific information like width and height.
	 */
	public final ViewportDocument viewportDocument;

	// position is always 0, 0
	public final DisplaySize size;

	/**
	 * Scroll position. This is the line index that is at the top of the
	 * viewport.
	 * 
	 * Note that lines a the bottom of a document can never be valid scroll
	 * indexes, because documents can only be scrolled down a certain amount.
	 */
	public final int scrollIndex;

	public final DisplayPadding contentPadding;

	public final Font font;

	public final boolean isModify;

	public final ViewportDisplayComponent displayComponent;

	public final boolean isReadOnly;

	public final boolean isSizeXLimit;

	protected Viewport(Document masterDocument, ViewportDocument viewportDocument, DisplaySize size, int scrollIndex,
			DisplayPadding contentPadding, Font font, boolean isModify, ViewportDisplayComponent displayComponent,
			boolean isReadOnly, boolean isSizeXLimit) {
		this.masterDocument = masterDocument;
		this.viewportDocument = viewportDocument;
		this.size = size;
		this.scrollIndex = scrollIndex;
		this.contentPadding = contentPadding;
		this.font = font;
		this.isModify = isModify;
		this.displayComponent = displayComponent;
		this.isReadOnly = isReadOnly;
		this.isSizeXLimit = isSizeXLimit;

		assert this.masterDocument != null;
		assert this.viewportDocument != null;

		assert this.viewportDocument.lineListSize() >= this.masterDocument
				.lineSize() : "Impossible for wrap line list to be smaller than its master line list.";

		assert this.size != null;

		assert this.viewportDocument.wrapSizeX() >= this.font.symbolGetByCharacter(
				'W').size.x : "Wrap display size isn't even large enough to fit a single character ('W' in this case).";

		assert scrollIndexCheck(this.scrollIndex);

		assert this.font != null;

		assert this.viewportDocument.lineSizeY >= this.font.sizeY : "Font size too high to fit in a viewport line.";

		assert this.contentPadding != null;

		assert this.displayComponent != null;
	}

	public static Viewport of(Font font, DisplaySize size, int lineSizeY, int wrapPaddingX,
			DisplayPadding contentPadding, boolean isReadOnly, Optional<Integer> maximumLineSize, boolean isWrap,
			boolean isSizeXLimit) {
		int scrollLineOffset = 0;

		Document masterDocument = Document.ofNoStyle("").withMaximumLineSize(maximumLineSize);

		DisplayBound contentBound = contentBoundRelative(size, contentPadding);

		// auto wrap
		ViewportDocument viewportDocument = ViewportDocument.of(masterDocument, wrapPaddingX, contentBound.size().x,
				lineSizeY, font, isWrap);

		boolean isModify = false;

		ViewportDisplayComponent displayComponent = ViewportDisplayComponent.of(lineSizeY);

		return new Viewport(masterDocument, viewportDocument, size, scrollLineOffset, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	@Override
	protected Viewport concreteCopy(Immutable<?> source) {
		return new Viewport(masterDocument, viewportDocument, size, scrollIndex, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	/**
	 * Auto wraps. Resets modify flag.
	 */
	public Viewport withMasterDocument(Document masterDocument) {
		// auto wrap
		ViewportDocument viewportDocument = this.viewportDocument.withDeriveFromMaster(masterDocument, font);

		// reset scroll any time a new document gets loaded
		int scrollIndex = 0;

		boolean isModify = false;

		return new Viewport(masterDocument, viewportDocument, size, scrollIndex, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	/**
	 * Caller must wrap.
	 */
	protected Viewport withMasterDocument(Document masterDocument, ViewportDocument viewportDocument) {
		assert masterDocument != null;
		assert viewportDocument != null;

		assert viewportDocument.lineSizeY == this.viewportDocument.lineSizeY : "Not allowed to change line height for existing viewport.";

		int scrollIndexCap = scrollLineOffsetCap(this.scrollIndex, viewportDocument.lineListSize(), pageLineSize(size));

		return new Viewport(masterDocument, viewportDocument, size, scrollIndexCap, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	protected Viewport withIsModify(boolean isModify) {
		return new Viewport(masterDocument, viewportDocument, size, scrollIndex, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	protected Viewport withDisplayComponent(ViewportDisplayComponent displayComponent) {
		return new Viewport(masterDocument, viewportDocument, size, scrollIndex, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	public Viewport withReadOnlySet(boolean isReadOnly) {
		return new Viewport(masterDocument, viewportDocument, size, scrollIndex, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	/**
	 * Auto re-wraps.
	 */
	public Viewport withDisplayResize(DisplaySize size) {
		assert size != null;

		int viewportSizeX = contentBoundRelative(size, contentPadding).sizeX();

		ViewportDocument viewportDocument = this.viewportDocument.withResize(masterDocument, viewportSizeX, font);

		//// instead look at current line index and translate that to newly
		//// resized viewport document
		// int scrollIndex = 0;
		int scrollIndexCap = scrollLineOffsetCap(this.scrollIndex, viewportDocument.lineListSize(), pageLineSize(size));

		/// might need to set display to stale

		return new Viewport(masterDocument, viewportDocument, size, scrollIndexCap, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	public Viewport withScrollIndex(int scrollIndex) {
		return new Viewport(masterDocument, viewportDocument, size, scrollIndex, contentPadding, font, isModify,
				displayComponent, isReadOnly, isSizeXLimit);
	}

	protected Viewport withScrollIndexCap() {
		return withScrollIndex(scrollLineOffsetCap(scrollIndex));
	}

	protected int scrollLineOffsetCap(int uncapped) {
		return scrollLineOffsetCap(uncapped, lineListSize(), pageLineSize());
	}

	protected int scrollLineOffsetCap(int uncapped, int lineListSize, int pageLineSize) {
		if (uncapped < 0) {
			return 0;
		}

		return Math.min(uncapped, maximumScrollOffset(lineListSize, pageLineSize));
	}

	protected int maximumScrollOffset() {
		return maximumScrollOffset(lineListSize(), pageLineSize());
	}

	/**
	 * Inclusive.
	 */
	protected int maximumScrollOffset(int lineListSize, int pageLineSize) {
		if (lineListSize <= pageLineSize) {
			// less than a page of content, no scrolling possible
			return 0;
		}

		int maximum = (lineListSize - pageLineSize) - 0;
		assert maximum > 0;
		return maximum;
	}

	/**
	 * Old scroll offset may be invalid. For example if a document section gets
	 * deleted the the old offset may point past the end of the document. This
	 * handles those situations.
	 */
	public Viewport withScrollIntoView(int lineIndex) {
		RangeIntegerPositive displayListRange = lineDisplayListRange();

		if (displayListRange.isContain(lineIndex)) {
			// line already visible
			return this;
		}

		int newScrollOffset;
		if (lineIndex < displayListRange.start) {
			// scroll up to line
			newScrollOffset = lineIndex;
		} else {
			// scroll down to line
			int delta = (lineIndex - (displayListRange.end - 1)) + 0;

			newScrollOffset = this.scrollIndex + delta;
		}

		return withScrollIndex(newScrollOffset);
	}

	public Viewport withScrollDeltaCap(int delta) {
		int proposedScrollOffset = scrollIndex + delta;

		int capOffset = scrollLineOffsetCap(proposedScrollOffset);

		return withScrollIndex(capOffset);
	}

	protected boolean scrollIndexCheck(int item) {
		return (item >= 0) && (item <= maximumScrollOffset());
	}

	protected int scrollPositionY() {
		int result = scrollIndex * lineSizeY();
		assert viewportDocument.positionYCheck(result);
		return result;
	}

	public boolean lineIndexCheck(int item) {
		return viewportDocument.lineIndexCheck(item);
	}

	public int lineListSize() {
		return viewportDocument.lineListSize();
	}

	public ViewportLine lineGet(int index) {
		return viewportDocument.lineGet(index);
	}

	/**
	 * Number of lines that could fit on the screen in theory.
	 */
	public int pageLineSize() {
		return pageLineSize(size);
	}

	protected int pageLineSize(DisplaySize size) {
		// careful that it gets rounded down and not up, so it never extends
		// down past display border
		int result = size.y / lineSizeY();
		assert result > 0;
		return result;
	}

	/**
	 * Lines that are currently visible starting from the top of the viewport
	 * bounds.
	 */
	public ViewportLineList lineDisplayList() {
		return viewportDocument.lineSubset(lineDisplayListRange());
	}

	/**
	 * Visible line index range.
	 */
	public RangeIntegerPositive lineDisplayListRange() {
		int indexStart = scrollIndex;

		int proposedIndexEnd = indexStart + pageLineSize();

		// If document not long enough to reach bottom of viewport just return
		// the rest of the lines.
		int indexEnd = Math.min(proposedIndexEnd, lineListSize());

		RangeIntegerPositive result = RangeIntegerPositive.of(indexStart, indexEnd);

		assert result.size() >= 0 : "Scrolled too far down.  Must always display at least one line.";

		// note that start does not have to be a valid index (e.g. if you are
		// working on an empty line both start and end are always invalid
		// indexes)
		assert viewportDocument.lineList.locationCheck(result.start);
		assert viewportDocument.lineList.locationCheck(result.end);

		return result;
	}

	public boolean symbolCheck(ViewportLocation location) {
		return viewportDocument.symbolCheck(location);
	}

	public ViewportSymbol symbolGet(ViewportLocation location) {
		return viewportDocument.symbolGet(location);
	}

	public boolean locationCheck(ViewportLocation location) {
		return viewportDocument.locationCheck(location);
	}

	protected boolean contentPositionCheck(DisplayPosition item) {
		assert item != null;

		return contentPositionXCheck(item.x) && contentPositionYCheck(item.y);
	}

	protected boolean contentPositionXCheck(int item) {
		// careful checking position against size
		return (item >= 0) && (item < contentBoundRelative().size().x);
	}

	protected boolean contentPositionYCheck(int item) {
		return (item >= 0) && (item < contentBoundRelative().size().y);
	}

	protected ViewportLocation contentPositionToLocationCap(DisplayPosition position) {
		assert contentPositionCheck(position);

		DisplayPosition viewportDocumentPosition = contentPositionToDocumentCap(position);

		ViewportLocation location = viewportDocument.positionToLocationCap(viewportDocumentPosition);
		assert locationCheck(location);
		return location;
	}

	protected DisplayPosition contentPositionToDocumentCap(DisplayPosition position) {
		assert position != null;

		int x = contentPositionXToDocumentCap(position.x);
		int y = contentPositionYToDocumentCap(position.y);

		return DisplayPosition.of(x, y);
	}

	protected int contentPositionXToDocumentCap(int positionX) {
		assert contentPositionXCheck(positionX);

		// document is always same width as viewport content so do nothing
		return positionX;
	}

	protected int contentPositionYToDocumentCap(int positionY) {
		assert contentPositionYCheck(positionY);

		int documentPositionY = contentPositionYToDocumentBlind(positionY);

		int nearestPositionY;
		if (viewportDocument.positionYCheck(documentPositionY)) {
			// within range
			nearestPositionY = documentPositionY;
		} else {
			// not within document, move it closer
			int documentSizeY = viewportDocument.sizeY();

			if (documentSizeY < 0) {
				/// should not be possible

				// position is above text, bring down to top of first line
				nearestPositionY = 0;
			} else {
				// below last lane, bring up to bottom of last line

				// ensure to subtract 1 to allow converting a size to a position
				nearestPositionY = documentSizeY - 1;
			}
		}

		assert viewportDocument.positionYCheck(nearestPositionY);
		return nearestPositionY;
	}

	/**
	 * Blind because it doesn't check whether it is on a valid line
	 */
	protected int contentPositionYToDocumentBlind(int positionY) {
		assert contentPositionYCheck(positionY);

		return scrollPositionY() + positionY;
	}

	protected DisplayPosition documentPositionToContent(DisplayPosition documentPosition) {
		assert documentPosition != null;

		return DisplayPosition.of(documentPositionXToContent(documentPosition.x),
				documentPositionYToContent(documentPosition.y));
	}

	protected int documentPositionXToContent(int documentPositionX) {
		assert viewportDocument.positionXCheck(documentPositionX);

		int result = documentPositionX;
		assert contentPositionXCheck(result);
		return result;
	}

	protected int documentPositionYToContent(int documentPositionY) {
		assert viewportDocument.positionYCheck(documentPositionY);

		int result = documentPositionY - scrollPositionY();
		assert contentPositionYCheck(result);
		return result;
	}

	protected DisplayPosition locationToContentPosition(ViewportLocation location) {
		assert location != null;

		DisplayPosition documentPosition = viewportDocument.locationToPosition(location);
		return documentPositionToContent(documentPosition);
	}

	public boolean controlPositionCheck(DisplayPosition item) {
		assert item != null;

		return controlPositionXCheck(item.x) && controlPositionYCheck(item.y);
	}

	protected boolean controlPositionXCheck(int item) {
		// careful checking position against size
		return (item >= 0) && (item < size.x);
	}

	protected boolean controlPositionYCheck(int item) {
		return (item >= 0) && (item < size.y);
	}

	protected DisplayPosition controlPositionToContent(DisplayPosition controlPosition) {
		assert controlPosition != null;

		int x = controlPositionXToContent(controlPosition.x);
		int y = controlPositionYToContent(controlPosition.y);

		return DisplayPosition.of(x, y);
	}

	protected int controlPositionXToContent(int controlPositionX) {
		assert controlPositionXCheck(controlPositionX);

		int result = controlPositionX - contentBoundRelative().left();
		assert contentPositionXCheck(result);
		return result;
	}

	protected int controlPositionYToContent(int controlPositionY) {
		assert controlPositionYCheck(controlPositionY);

		int result = controlPositionY - contentBoundRelative().top();
		assert contentPositionYCheck(result);
		return result;
	}

	protected DisplayPosition contentPositionToControl(DisplayPosition contentPosition) {
		assert contentPosition != null;

		int x = contentPositionXToControl(contentPosition.x);
		int y = contentPositionYToControl(contentPosition.y);

		return DisplayPosition.of(x, y);
	}

	protected int contentPositionXToControl(int contentPositionX) {
		assert contentPositionXCheck(contentPositionX);

		int result = contentBoundRelative().left() + contentPositionX;
		assert controlPositionXCheck(result);
		return result;
	}

	protected int contentPositionYToControl(int contentPositionY) {
		assert contentPositionYCheck(contentPositionY);

		int result = contentBoundRelative().top() + contentPositionY;
		assert controlPositionYCheck(result);
		return result;
	}

	protected DisplayPosition controlPositionToContentCap(DisplayPosition controlPosition) {
		assert controlPosition != null;

		int x = controlPositionXToContentCap(controlPosition.x);
		int y = controlPositionYToContentCap(controlPosition.y);

		return DisplayPosition.of(x, y);
	}

	protected int controlPositionXToContentCap(int controlPositionX) {
		assert controlPositionXCheck(controlPositionX);

		int proposedContentPositionX = controlPositionX - contentBoundRelative().left();

		int contentX;
		if (contentPositionXCheck(proposedContentPositionX)) {
			// x in range
			contentX = proposedContentPositionX;
		} else {
			// x out of range
			if (proposedContentPositionX < 0) {
				// position is above content
				contentX = 0;
			} else {
				// below content

				// ensure to subtract 1 to allow converting a size to a position
				contentX = contentBoundRelative().sizeX() - 1;
			}
		}

		assert contentPositionXCheck(contentX);
		return contentX;
	}

	protected int controlPositionYToContentCap(int controlPositionY) {
		assert controlPositionYCheck(controlPositionY);

		int proposedContentPositionY = controlPositionY - contentBoundRelative().top();

		int contentY;
		if (contentPositionYCheck(proposedContentPositionY)) {
			// y in range
			contentY = proposedContentPositionY;
		} else {
			// y out of range
			if (proposedContentPositionY < 0) {
				// position is above content
				contentY = 0;
			} else {
				// below content

				// ensure to subtract 1 to allow converting a size to a position
				contentY = contentBoundRelative().sizeY() - 1;
			}
		}

		assert contentPositionYCheck(contentY);
		return contentY;
	}

	public ViewportLocation controlPositionToLocationCap(DisplayPosition controlPosition) {
		assert controlPositionCheck(controlPosition);

		DisplayPosition contentPosition = controlPositionToContentCap(controlPosition);

		return contentPositionToLocationCap(contentPosition);
	}

	public DisplayPosition locationToControlPosition(ViewportLocation viewportLocation) {
		assert viewportLocation != null;

		DisplayPosition contentPosition = locationToContentPosition(viewportLocation);

		return contentPositionToControl(contentPosition);
	}

	/**
	 * Inserts text at the given location.
	 * 
	 * First the master document is modified, then the new viewport document is
	 * derived from that. But the derivation is optimized. Wrapping is only
	 * recalculated for the lines that changed due to the insert. All viewport
	 * lines above are left unchanged and the lines below are shifted down.
	 * 
	 * @param insertLocation
	 *            The location to insert into. All text at this location and to
	 *            the right is appended (directly, without adding a CR first) to
	 *            the newly inserted text.
	 * 
	 * @param insertText
	 *            The text to insert. May contain carriage returns. Must not be
	 *            empty.
	 * 
	 *            Text is inserted with empty style. Caller should update the
	 *            text afterwards if style is needed.
	 * 
	 * @return A result object containing the updated viewport content and also
	 *         the location range of the inserted text (used by callers for
	 *         setting the cursor to the end of the insert).
	 */
	public ResultInsert withActionInsert(ViewportLocation insertLocation, String insertText) {
		assert !isReadOnly;

		// insert into master document
		DocumentLocation documentInsertLocation = viewportDocument.viewportLocationToMaster(insertLocation);

		Document.ResultInsert documentResult = masterDocument.withActionInsert(documentInsertLocation, insertText);
		Document newMasterDocument = documentResult.self;

		if (!documentResult.isChange) {
			return ResultInsert.of(this, DocumentLocationRange.of(), ViewportLocationRange.of()).withIsChange(false);
		}

		// auto wrap
		ViewportDocument newViewportDocument = this.viewportDocument.withDeriveFromMaster(newMasterDocument, font);
		if (!sizeXLimitCheck(newViewportDocument.lineList)) {
			// text too wide, silently do nothing
			return ResultInsert.of(this, DocumentLocationRange.of(), ViewportLocationRange.of()).withIsChange(false);
		}
		Viewport newViewportContent = withMasterDocument(newMasterDocument, newViewportDocument).withIsModify(true);

		// record start and end locations
		ViewportLocation resultLocationStart = newViewportDocument
				.masterLocationToViewport(documentResult.location.start);
		ViewportLocation resultLocationEnd = newViewportDocument.masterLocationToViewport(documentResult.location.end);
		ViewportLocationRange resultLocation = ViewportLocationRange.of(resultLocationStart, resultLocationEnd);

		return ResultInsert.of(newViewportContent, documentResult.location, resultLocation);
	}

	protected boolean sizeXLimitCheck(ViewportLineList lineList) {
		if (!isSizeXLimit) {
			// no limit
			return true;
		}

		// allows for end of line cursor too
		int maximumTextSizeX = viewportDocument.sizeX - this.font.symbolGetByCharacter('W').size.x; ////
		for (ViewportLine line : lineList) {
			if (line.sizeX() > maximumTextSizeX) {
				// line is too long
				return false;
			}
		}

		return true;
	}

	public ResultDelete withActionDelete(ViewportLocationRange deleteLocationRange) {
		assert !isReadOnly;

		// delete in master document
		DocumentLocationRange documentDeleteRange = viewportDocument.viewportLocationToMaster(deleteLocationRange);

		Document.ResultDelete documentResult = masterDocument.withDelete(documentDeleteRange);
		Document newMasterDocument = documentResult.self;

		// auto wrap
		ViewportDocument newViewportDocument = this.viewportDocument.withDeriveFromMaster(newMasterDocument, font);

		//// scroll index might be incorrect, need to set it at same time as
		//// document

		Viewport newViewportContent = withMasterDocument(newMasterDocument, newViewportDocument).withScrollIndexCap()
				.withIsModify(true);

		// record location
		ViewportLocation resultViewportLocation = newViewportDocument.masterLocationToViewport(documentResult.location);

		return ResultDelete.of(newViewportContent, documentResult.location, resultViewportLocation);
	}

	public int lineSizeY() {
		return viewportDocument.lineSizeY;
	}

	protected DisplayBound contentBoundRelative() {
		return contentBoundRelative(size, contentPadding);
	}

	protected static DisplayBound contentBoundRelative(DisplaySize size, DisplayPadding contentPadding) {
		DisplayBound contentBound = contentPadding.bound(size);

		DisplayBound viewportBound = DisplayBound.ofSize(size);
		assert contentBound.isInsideOrEqualTo(viewportBound) : "Content extends outside viewport bound.";

		return contentBound;
	}

	public Viewport withDisplayComponentUpdate(DisplayPosition containerPosition) {
		if (!displayComponent.isStale) {
			return this;
		}

		ViewportLineList visibleLineList = lineDisplayList();

		return withDisplayComponent(
				displayComponent.withBound(contentBoundRelative()).withContainerPosition(containerPosition)
						.withVisibleLineList(visibleLineList).withIsVisible(true).withIsStale(false));
	}

	public Viewport withDisplaySetStale() {
		return withDisplayComponent(displayComponent.withSetStale());
	}

	/**
	 * Contains the new viewport document with inserted text, and location
	 * information for the insert.
	 */
	public static final class ResultInsert extends ResultBase<Viewport, ResultInsert> {
		/**
		 * Insertion range in master coordinates.
		 */
		public final DocumentLocationRange masterLocation;

		/**
		 * Insertion range in viewport coordinates.
		 */
		public final ViewportLocationRange viewportLocation;

		protected ResultInsert(Viewport self, boolean isChange, DocumentLocationRange masterLocation,
				ViewportLocationRange viewportLocation) {
			super(self, isChange);
			this.masterLocation = masterLocation;
			this.viewportLocation = viewportLocation;
		}

		public static ResultInsert of(Viewport self, DocumentLocationRange masterLocation,
				ViewportLocationRange viewportLocation) {
			return new ResultInsert(self, true, masterLocation, viewportLocation);
		}

		@Override
		protected ResultInsert concreteThis() {
			return this;
		}

		@Override
		protected final ResultInsert copy(Viewport self, boolean isChange) {
			return new ResultInsert(self, isChange, masterLocation, viewportLocation);
		}
	}

	/**
	 * Contains the new viewport document with deleted location range, and cut
	 * location information for the caller (always the same as delete location
	 * range start).
	 */
	public static final class ResultDelete extends ResultBase<Viewport, ResultDelete> {
		/**
		 * Delete cut location in master coordinates.
		 */
		public final DocumentLocation masterLocation;

		/**
		 * Delete cut location in viewport coordinates.
		 */
		public final ViewportLocation viewportLocation;

		protected ResultDelete(Viewport self, boolean isChange, DocumentLocation masterLocation,
				ViewportLocation viewportLocation) {
			super(self, isChange);
			this.masterLocation = masterLocation;
			this.viewportLocation = viewportLocation;
		}

		public static ResultDelete of(Viewport self, DocumentLocation masterLocation,
				ViewportLocation viewportLocation) {
			return new ResultDelete(self, true, masterLocation, viewportLocation);
		}

		@Override
		protected ResultDelete concreteThis() {
			return this;
		}

		@Override
		protected final ResultDelete copy(Viewport self, boolean isChange) {
			return new ResultDelete(self, isChange, masterLocation, viewportLocation);
		}
	}
}
