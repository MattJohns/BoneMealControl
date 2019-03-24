package mattjohns.common.immutable.userinterface.textarea;

import java.util.Optional;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.ResultBase;
import mattjohns.common.immutable.math.geometry.dimension1.RangeIntegerPositive;

/**
 * Contains lines of text with an implied carriage return between them.
 * 
 * ViewportDocument holds display information such as sizes and fonts.
 * 
 * Styles are part of Document though because they need to be saved to disk by
 * the user, whereas display information is recreated dynamically.
 */
public final class Document extends Immutable<Document> {
	protected final DocumentLineList lineList;

	public Optional<Integer> maximumLineSize;

	protected Document(DocumentLineList lineList, Optional<Integer> maximumLineSize) {
		this.lineList = lineList;
		this.maximumLineSize = maximumLineSize;

		// Just so you never have to deal with 0 lines versus 1 empty line,
		// which are effectively the same thing.
		assert lineSize() >= 1 : "Documents must always have at least one line.";

		if (this.maximumLineSize.isPresent()) {
			assert this.maximumLineSize.get() >= 1;
		}
	}

	public static Document ofNoStyle(String text) {
		DocumentLineList lineList = DocumentLineList.ofNoStyle(text);

		return new Document(lineList, Optional.empty());
	}

	@Override
	protected Document concreteCopy(Immutable<?> source) {
		return new Document(lineList, maximumLineSize);
	}

	protected Document withLineList(DocumentLineList lineList) {
		return new Document(lineList, maximumLineSize);
	}

	public Document withTextNoStyle(String textNoStyle) {
		DocumentLineList lineList = DocumentLineList.ofNoStyle(textNoStyle);

		return new Document(lineList, maximumLineSize);
	}

	public Document withMaximumLineSize(Optional<Integer> maximumLineSize) {
		return new Document(lineList, maximumLineSize);
	}

	public ResultInsert withActionInsert(DocumentLocation insertLocation, String insertText) {
		DocumentLineList.ResultInsert listResult = lineList.withInsert(insertLocation, insertText);

		if (maximumLineSize.isPresent()) {
			if (listResult.self.size() > maximumLineSize.get()) {
				// too large to insert, silently refuse it
				return ResultInsert.of(this, listResult)
						.withIsChange(false);
			}
		}

		return ResultInsert.of(withLineList(listResult.self), listResult);
	}

	public ResultDelete withDelete(DocumentLocationRange deleteLocationRange) {
		DocumentLineList.ResultDelete listResult = lineList.withDelete(deleteLocationRange);

		return ResultDelete.of(withLineList(listResult.self), listResult);
	}

	public int lineSize() {
		return lineList.size();
	}

	public boolean lineIndexCheck(int item) {
		return lineList.indexCheck(item);
	}

	public DocumentLine lineGet(int index) {
		return lineList.get(index);
	}

	public DocumentSymbol symbolGet(DocumentLocation index) {
		return lineGet(index.line).symbolGet(index.symbol);
	}

	public boolean locationCheck(DocumentLocation location) {
		assert location != null;

		if (!lineIndexCheck(location.line)) {
			return false;
		}

		return lineGet(location.line).locationCheck(location.symbol);
	}

	@Override
	public String toString() {
		return textStyle();
	}

	public String textNoStyle() {
		return lineList.textNoStyle();
	}

	public String textStyle() {
		return lineList.textStyle();
	}

	public boolean isEmpty() {
		return lineSize() == 1 && lineList.start()
				.isEmpty();
	}

	public static final class ResultInsert extends ResultBase<Document, ResultInsert> {
		public final DocumentLocationRange location;
		public final RangeIntegerPositive lineChange;

		protected ResultInsert(Document self, boolean isChange, DocumentLocationRange location,
				RangeIntegerPositive lineChange) {
			super(self, isChange);
			this.location = location;
			this.lineChange = lineChange;
		}

		public static ResultInsert of(Document self, DocumentLineList.ResultInsert listResult) {
			return new ResultInsert(self, listResult.isChange, listResult.location, listResult.lineChange);
		}

		public static ResultInsert of(Document self, DocumentLocationRange location, RangeIntegerPositive lineChange) {
			return new ResultInsert(self, true, location, lineChange);
		}

		@Override
		protected ResultInsert concreteThis() {
			return this;
		}

		@Override
		protected final ResultInsert copy(Document self, boolean isChange) {
			return new ResultInsert(self, isChange, location, lineChange);
		}
	}

	public static final class ResultDelete extends ResultBase<Document, ResultDelete> {
		public final DocumentLocation location;
		public int numberOfLineDelete;

		protected ResultDelete(Document self, boolean isChange, DocumentLocation location, int numberOfLineDelete) {
			super(self, isChange);
			this.location = location;
			this.numberOfLineDelete = numberOfLineDelete;
		}

		public static ResultDelete of(Document self, DocumentLineList.ResultDelete listResult) {
			return new ResultDelete(self, listResult.isChange, listResult.location, listResult.numberOfLineDelete);
		}

		public static ResultDelete of(Document self, DocumentLocation location, int numberOfLineDelete) {
			return new ResultDelete(self, true, location, numberOfLineDelete);
		}

		@Override
		protected ResultDelete concreteThis() {
			return this;
		}

		@Override
		protected final ResultDelete copy(Document self, boolean isChange) {
			return new ResultDelete(self, isChange, location, numberOfLineDelete);
		}
	}
}
