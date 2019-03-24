package mattjohns.common.immutable.userinterface.textarea;

import com.google.common.collect.ImmutableList;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.list.ListImmutableBase;

final class DocumentSymbolList extends ListImmutableBase<DocumentSymbol, DocumentSymbolList> {
	protected DocumentSymbolList(ImmutableList<DocumentSymbol> internalList) {
		super(internalList);
	}

	public static DocumentSymbolList of() {
		return new DocumentSymbolList(ImmutableList.of());
	}

	public static DocumentSymbolList ofNoStyle(String text) {
		assert text != null;

		Builder builder = new Builder();

		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);

			DocumentSymbol newSymbol = DocumentSymbol.of(character);

			builder.add(newSymbol);
		}

		return builder.build();
	}

	@Override
	protected final DocumentSymbolList copy(ImmutableList<DocumentSymbol> internalList) {
		return new DocumentSymbolList(internalList);
	}

	public String textNoStyle() {
		StringBuilder builder = new StringBuilder();

		forEach(viewportSymbol -> builder.append(viewportSymbol.textNoStyle()));

		return builder.toString();
	}

	public String textStyle() {
		StringBuilder builder = new StringBuilder();

		forEach(viewportSymbol -> builder.append(viewportSymbol.textStyle()));

		return builder.toString();
	}

	@Override
	public String toString() {
		return textStyle();
	}

	public static final class Builder extends ListImmutableBase.Builder<DocumentSymbol, DocumentSymbolList, Builder> {
		public static Builder of() {
			return new Builder();
		}

		@Override
		protected Builder concreteCopy(Immutable<?> source) {
			return new Builder();
		}

		@Override
		protected DocumentSymbolList upcastList(ImmutableList<DocumentSymbol> baseList) {
			return new DocumentSymbolList(baseList);
		}
	}
}
