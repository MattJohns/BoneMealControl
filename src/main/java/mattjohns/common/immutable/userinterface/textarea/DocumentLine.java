package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;

final class DocumentLine extends Immutable<DocumentLine> {
	protected final DocumentSymbolList symbolList;

	protected DocumentLine(DocumentSymbolList symbolList) {
		this.symbolList = symbolList;

		assert this.symbolList != null;

		// can be empty, e.g. a line with just enter on it
		assert this.symbolList.size() >= 0;
	}

	public static DocumentLine of() {
		return new DocumentLine(DocumentSymbolList.of());
	}

	public static DocumentLine ofNoStyle(String text) {
		DocumentSymbolList symbolList = DocumentSymbolList.ofNoStyle(text);
		return new DocumentLine(symbolList);
	}

	@Override
	protected DocumentLine concreteCopy(Immutable<?> source) {
		return new DocumentLine(symbolList);
	}

	protected DocumentLine withSymbolList(DocumentSymbolList symbolList) {
		return new DocumentLine(symbolList);
	}

	public DocumentLine withSubset(int locationStart, int locationEnd) {
		return withSymbolList(symbolList.withSubset(locationStart, locationEnd));
	}

	public DocumentLine withSplitBefore(int location) {
		return withSymbolList(symbolList.withSplitBefore(location));
	}

	public DocumentLine withSplitAfter(int location) {
		return withSymbolList(symbolList.withSplitAfter(location));
	}

	public DocumentLine withJoin(DocumentLine end) {
		return withSymbolList(symbolList.withJoinList(end.symbolList));
	}

	public int symbolListSize() {
		return symbolList.size();
	}

	public DocumentSymbol symbolGet(int index) {
		return symbolList.get(index);
	}

	public boolean locationCheck(int item) {
		return symbolList.locationCheck(item);
	}

	public boolean indexCheck(int item) {
		return symbolList.indexCheck(item);
	}

	public int endIndex() {
		return symbolList.endIndex();
	}

	public int endLocation() {
		return symbolList.endLocation();
	}

	public boolean isEmpty() {
		return symbolList.isEmpty();
	}

	public String textNoStyle() {
		return symbolList.textNoStyle();
	}

	public String textStyle() {
		return symbolList.textStyle();
	}

	@Override
	public String toString() {
		return textStyle();
	}
}
