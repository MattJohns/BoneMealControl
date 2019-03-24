package mattjohns.common.immutable.userinterface.textarea;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;
import mattjohns.common.immutable.userinterface.font.Font;

/**
 * Wrapped lines that are derived from a 'master' (unwrapped) line list.
 * 
 * A mapping of locations is stored in each line that allows traversal between
 * viewport and normal document locations.
 */
public final class ViewportLineList extends ListImmutableBase<ViewportLine, ViewportLineList> {
	protected ViewportLineList(ImmutableList<ViewportLine> internalList) {
		super(internalList);
	}

	public static ViewportLineList of() {
		return new ViewportLineList(ImmutableList.of());
	}

	/**
	 * Automatically wraps the given lines.
	 */
	public static ViewportLineList ofWrap(Document masterDocument, int wrapSizeX, Font font) {
		return of().withWrap(0, masterDocument, wrapSizeX, font);
	}

	public static ViewportLineList ofNoWrap(Document masterDocument, Font font) {
		return of().withNoWrap(0, masterDocument, font);
	}

	/**
	 * Wraps the given master lines.
	 * 
	 * @param baseMasterLineIndex
	 * Allows the mapping to be modified. If the given master lines were in the
	 * middle of the document then you would need to tell this method which line
	 * index they start at. That way the mapping from viewport to master is
	 * correct.
	 * 
	 * Use 0 if wrapping an entire master document.
	 */
	protected ViewportLineList withWrap(int baseMasterLineIndex, Document masterDocument, int wrapSizeX, Font font) {
		Builder builder = Builder.of();

		// wrap each master line one by one
		for (int masterLineRelativeIndex = 0; masterLineRelativeIndex < masterDocument
				.lineSize(); masterLineRelativeIndex++) {
			DocumentLine masterLine = masterDocument.lineGet(masterLineRelativeIndex);

			// offset mapping by given base index
			int absoluteMasterLineIndex = baseMasterLineIndex + masterLineRelativeIndex;

			// wrap master line with updated mapping
			ViewportLineList wrappedLine = wrap(masterLine, absoluteMasterLineIndex, wrapSizeX, font);
			builder.add(wrappedLine);
		}

		return builder.build();
	}

	protected ViewportLineList withNoWrap(int baseMasterLineIndex, Document masterDocument, Font font) {
		Builder builder = Builder.of();

		// wrap each master line one by one
		for (int masterLineRelativeIndex = 0; masterLineRelativeIndex < masterDocument
				.lineSize(); masterLineRelativeIndex++) {
			DocumentLine masterLine = masterDocument.lineGet(masterLineRelativeIndex);

			// offset mapping by given base index
			int absoluteMasterLineIndex = baseMasterLineIndex + masterLineRelativeIndex;

			DocumentLocation startLocation = DocumentLocation.of(absoluteMasterLineIndex, 0);

			ViewportSymbolList.Builder symbolBuilder = new ViewportSymbolList.Builder();

			for (int masterSymbolIndex = 0; masterSymbolIndex < masterLine.symbolListSize(); masterSymbolIndex++) {
				DocumentSymbol masterSymbol = masterLine.symbolGet(masterSymbolIndex);

				ViewportSymbol newViewportSymbol = ViewportSymbol.of(masterSymbol, font);

				symbolBuilder.add(newViewportSymbol);
			}

			ViewportSymbolList symbolList = symbolBuilder.build();

			ViewportLine line = ViewportLine.of(startLocation, symbolList, false);

			builder.add(line);
		}

		return builder.build();
	}

	@Override
	protected final ViewportLineList copy(ImmutableList<ViewportLine> internalList) {
		return new ViewportLineList(internalList);
	}

	/**
	 * Wraps the given line, potentially into multiple viewport lines.
	 * 
	 * @param masterLine
	 * The line to wrap.
	 * 
	 * @param masterLineIndex
	 * The line index, used for mapping viewport to document lines.
	 * 
	 * @param wrapSizeX
	 * If all characters fit within this size then no wrap occurs.
	 */
	protected static ViewportLineList wrap(DocumentLine masterLine, int masterLineIndex, int wrapSizeX, Font font) {
		assert masterLine != null;
		assert masterLineIndex >= 0;
		assert wrapSizeX > 0;
		assert font != null;

		Builder builder = Builder.of();

		if (masterLine.isEmpty()) {
			// empty line
			builder.add(ViewportLine.of()
					.withMasterLocationStart(DocumentLocation.of(masterLineIndex, 0)));
		}
		else {
			// create a new line at each wrap point
			ViewportSymbolList.Builder symbolBuilder = new ViewportSymbolList.Builder();

			int currentStartSymbol = 0;
			int currentDisplaySizeX = 0;

			for (int masterSymbolIndex = 0; masterSymbolIndex < masterLine.symbolListSize(); masterSymbolIndex++) {
				DocumentSymbol masterSymbol = masterLine.symbolGet(masterSymbolIndex);

				ViewportSymbol newViewportSymbol = ViewportSymbol.of(masterSymbol, font);

				// check potential wrap if this character is added
				int proposedDisplaySizeX = currentDisplaySizeX + newViewportSymbol.sizeX();
				if (proposedDisplaySizeX > wrapSizeX) {
					// hit right edge, wrap current text then start a new line
					assert masterSymbolIndex > 0 : "Somehow wrapping on first character in document line.";

					// get current batch of symbols
					ViewportSymbolList segmentSymbolList = symbolBuilder.build();
					assert segmentSymbolList.size() > 0 : "Wrap width is not large enough to fit a single character.";

					// double check segment size
					int expectedSegmentSize = masterSymbolIndex - currentStartSymbol;
					assert expectedSegmentSize == segmentSymbolList.size() : "Unexpected line segment size.";

					DocumentLocation segmentLocation = DocumentLocation.of(masterLineIndex, currentStartSymbol);

					// create wrapped line
					ViewportLine newViewportLine = ViewportLine.of(segmentLocation, segmentSymbolList, true);
					builder.add(newViewportLine);

					// start on the next segment in the master line
					symbolBuilder = new ViewportSymbolList.Builder();
					currentStartSymbol = masterSymbolIndex;
					currentDisplaySizeX = 0;
				}

				// current symbol still hasn't been added even if there was a
				// wrap
				symbolBuilder.add(newViewportSymbol);
				currentDisplaySizeX += newViewportSymbol.sizeX();
			}

			// final segment
			ViewportSymbolList finalSegmentSymbolList = symbolBuilder.build();
			if (finalSegmentSymbolList.size() > 0) {
				int expectedSegmentSize = (masterLine.endIndex() - currentStartSymbol) + 1;
				assert expectedSegmentSize == finalSegmentSymbolList.size() : "Unexpected line segment size.";

				DocumentLocation segmentLocation = DocumentLocation.of(masterLineIndex, currentStartSymbol);

				// never a wrap for this final batch, otherwise it would've been
				// picked up by the above loop
				ViewportLine finalViewportLine = ViewportLine.of(segmentLocation, finalSegmentSymbolList, false);
				builder.add(finalViewportLine);
			}
		}

		return builder.build();
	}

	@Override
	public String toString() {
		if (size() == 0) {
			return "[Empty]";
		}

		// just show first line in case there's lots of them
		return size() + " lines. First line: " + start().toString();
	}

	public boolean locationCheck(ViewportLocation viewportLocation) {
		assert viewportLocation != null;

		if (!indexCheck(viewportLocation.line)) {
			// not a line
			return false;
		}

		return get(viewportLocation.line).locationCheck(viewportLocation.symbol);
	}

	/**
	 * Checks if a symbol is at the given location. Must be a valid location.
	 */
	public boolean symbolCheck(ViewportLocation viewportLocation) {
		assert locationCheck(viewportLocation);

		return get(viewportLocation.line).symbolIndexCheck(viewportLocation.symbol);
	}

	/**
	 * Must be a valid symbol at the given location (i.e. not end of line).
	 */
	public ViewportSymbol symbolGet(ViewportLocation viewportLocation) {
		assert symbolCheck(viewportLocation);

		return get(viewportLocation.line).symbolGet(viewportLocation.symbol);
	}

	public static final class Builder extends ListImmutableBase.Builder<ViewportLine, ViewportLineList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected ViewportLineList upcastList(ImmutableList<ViewportLine> baseList) {
			return new ViewportLineList(baseList);
		}
	}
}
