package mattjohns.common.immutable.userinterface.textarea;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.ResultBase;
import mattjohns.common.immutable.list.ListImmutable;
import mattjohns.common.immutable.list.ListImmutableBase;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;
import mattjohns.common.text.TextUtility;

final class DocumentLineList extends ListImmutableBase<DocumentLine, DocumentLineList> {
	protected DocumentLineList(ImmutableList<DocumentLine> internalList) {
		super(internalList);
	}

	public static DocumentLineList of() {
		return new DocumentLineList(ImmutableList.of());
	}

	public static DocumentLineList ofLine(DocumentLine item) {
		return new DocumentLineList(ImmutableList.of(item));
	}

	public static DocumentLineList ofNoStyle(String documentText) {
		ListImmutable<String> splitDocument = TextUtility.splitEnter(documentText);

		DocumentLineList.Builder lineBuilder = new DocumentLineList.Builder();

		for (String lineText : splitDocument) {
			lineBuilder.add(DocumentLine.ofNoStyle(lineText));
		}

		return lineBuilder.build();
	}

	@Override
	protected final DocumentLineList copy(ImmutableList<DocumentLine> internalList) {
		return new DocumentLineList(internalList);
	}

	@Override
	protected DocumentLineList concreteCopy(Immutable<?> source) {
		return copy(internalList);
	}

	@Override
	public String toString() {
		if (size() == 0) {
			return "Empty";
		}

		return size() + " lines. First line: " + start().toString();
	}

	public DocumentLineList appendTo(DocumentLine start) {
		assert start != null;

		if (start.isEmpty()) {
			return this;
		}

		if (isEmpty()) {
			return DocumentLineList.ofLine(start);
		}

		DocumentLine newFirstLine = start.withJoin(start());

		return withReplaceIndexWithItem(0, newFirstLine);
	}

	public DocumentLineList prefixTo(DocumentLine end) {
		assert end != null;

		if (end.isEmpty()) {
			return this;
		}

		if (isEmpty()) {
			return DocumentLineList.ofLine(end);
		}

		DocumentLine newLastLine = end().withJoin(end);

		return withReplaceIndexWithItem(endIndex(), newLastLine);
	}

	// gives back 0, 0 if empty document
	protected DocumentLocation endDocumentLocation() {
		if (isEmpty()) {
			return DocumentLocation.of(0, 0);
		}

		int line = endIndex();
		int symbol = get(line).endLocation();

		DocumentLocation result = DocumentLocation.of(line, symbol);
		assert locationCheck(result);
		return result;
	}

	/**
	 * Inserts text into a location. Doesn't create a new line for the text, it
	 * inserts directly into the location without any CRs added.
	 * 
	 * @param insertLocation
	 * May be end of line.
	 * 
	 * @param insertText
	 * May contain CRs. Must not be empty.
	 * 
	 * @return The new document and information about the locations involved
	 * with the insert.
	 */
	public ResultInsert withInsert(DocumentLocation insertLocation, String insertText) {
		assert insertLocation != null;
		assert locationCheck(insertLocation);

		assert insertText != null;
		assert insertText.length() > 0;

		DocumentLine originalLine = get(insertLocation.line);

		// split the line at insert point
		DocumentLine leftOriginalLine = originalLine.withSplitBefore(insertLocation.symbol);
		DocumentLine rightOriginalLine = originalLine.withSplitAfter(insertLocation.symbol);

		// convert insert text into line (may be multiple lines if it contains
		// CRs)
		DocumentLineList insertTextLineList = ofNoStyle(insertText);

		// just add the left to the new text so the end location can be
		// calculated
		DocumentLineList partialNewLine = insertTextLineList.appendTo(leftOriginalLine);

		// record location of insert
		DocumentLocation resultLocationStart = insertLocation;

		// end location = insert line index + insert text line size
		DocumentLocation resultLocationEnd = partialNewLine.endDocumentLocation()
				// actual offset of partial new line
				.withLineAdd(insertLocation.line);

		DocumentLocationRange resultLocationRange = DocumentLocationRange.of(resultLocationStart, resultLocationEnd);

		// now safe append the right line
		DocumentLineList newLine = partialNewLine.prefixTo(rightOriginalLine);

		// replace old line with new line in document
		DocumentLineList newFullLineList = withReplaceIndexWithList(insertLocation.line, newLine);

		// caller can update the screen just for the lines that changed rather
		// than everything
		int numberOfLineChange = newLine.size();
		assert numberOfLineChange > 0 : "Impossible to insert into a document line without changing any lines.";

		RangeIntegerPositive resultLineChange = RangeIntegerPositive.of(insertLocation.line,
				insertLocation.line + numberOfLineChange);

		return ResultInsert.of(newFullLineList, resultLocationRange, resultLineChange);
	}

	public ResultDelete withDelete(DocumentLocationRange location) {
		assert location != null;
		assert locationCheck(location.start);
		assert locationCheck(location.end);

		// don't need to touch lines that are outside the delete range
		DocumentLineList topLineExclude = withSubset(0, location.start.line);
		DocumentLineList bottomLineExclude = withSubset(location.end.line + 1, size());

		// keep everything to left of start location
		DocumentLine leftKeep = get(location.start.line).withSplitBefore(location.start.symbol);

		// and everything right of end location
		DocumentLine rightKeep = get(location.end.line).withSplitAfter(location.end.symbol);

		// splice those parts together to form a new line with the delete text
		// removed
		DocumentLine newLine = leftKeep.withJoin(rightKeep);

		// assemble together with the untouched lines
		DocumentLineList newLineList = topLineExclude.withJoinItem(newLine)
				.withJoinList(bottomLineExclude);

		// inform the caller which lines changed so they can update screen
		// appropriately
		int numberOfLineDelete = location.end.line - location.start.line;
		assert numberOfLineDelete >= 0;

		DocumentLocation resultLocation = location.start;

		return ResultDelete.of(newLineList, resultLocation, numberOfLineDelete);
	}

	public boolean locationCheck(DocumentLocation location) {
		assert location != null;

		if (!indexCheck(location.line)) {
			return false;
		}

		return get(location.line).locationCheck(location.symbol);
	}

	public String textNoStyle() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < size(); i++) {
			String lineText = get(i).textNoStyle();
			builder.append(lineText);

			if (i == size() - 1) {
				// last item, no cr
			}
			else {
				// add cr
				builder.append(TextUtility.CARRIAGE_RETURN);
			}
		}

		return builder.toString();
	}

	public String textStyle() {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < size(); i++) {
			String lineText = get(i).textStyle();
			builder.append(lineText);

			if (i == size() - 1) {
				// last item, no cr
			}
			else {
				// add cr
				builder.append(TextUtility.CARRIAGE_RETURN);
			}
		}

		return builder.toString();
	}

	public static final class Builder extends ListImmutableBase.Builder<DocumentLine, DocumentLineList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected DocumentLineList upcastList(ImmutableList<DocumentLine> baseList) {
			return new DocumentLineList(baseList);
		}
	}

	public static final class ResultInsert extends ResultBase<DocumentLineList, ResultInsert> {
		public final DocumentLocationRange location;
		public final RangeIntegerPositive lineChange;

		protected ResultInsert(DocumentLineList self, boolean isChange, DocumentLocationRange location,
				RangeIntegerPositive lineChange) {
			super(self, isChange);
			this.location = location;
			this.lineChange = lineChange;
		}

		public static ResultInsert of(DocumentLineList self, DocumentLocationRange location,
				RangeIntegerPositive lineChange) {
			return new ResultInsert(self, true, location, lineChange);
		}

		@Override
		protected ResultInsert concreteThis() {
			return this;
		}

		@Override
		protected final ResultInsert copy(DocumentLineList self, boolean isChange) {
			return new ResultInsert(self, isChange, location, lineChange);
		}
	}

	public static final class ResultDelete extends ResultBase<DocumentLineList, ResultDelete> {
		public final DocumentLocation location;
		public int numberOfLineDelete;

		protected ResultDelete(DocumentLineList self, boolean isChange, DocumentLocation location,
				int numberOfLineDelete) {
			super(self, isChange);
			this.location = location;
			this.numberOfLineDelete = numberOfLineDelete;
		}

		public static ResultDelete of(DocumentLineList self, DocumentLocation location, int numberOfLineDelete) {
			return new ResultDelete(self, true, location, numberOfLineDelete);
		}

		@Override
		protected ResultDelete concreteThis() {
			return this;
		}

		@Override
		protected final ResultDelete copy(DocumentLineList self, boolean isChange) {
			return new ResultDelete(self, isChange, location, numberOfLineDelete);
		}
	}
}
