package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;

/**
 * A character within a document.
 * 
 * Also stores style codes that represent styles to apply both before and after
 * the character. They are effectively hidden control characters in a document.
 */
final class DocumentSymbol extends Immutable<DocumentSymbol> {
	public final char character;
	/**
	 * Style that will be applied before the character is displayed.
	 */
	public final DocumentSymbolStyle styleStart;

	/**
	 * Style that is applied after the character is displayed. This is mostly
	 * used for resetting text to default style, after a style was used in the
	 * preceding text.
	 */
	public final DocumentSymbolStyle styleEnd;

	protected DocumentSymbol(char character, DocumentSymbolStyle styleStart, DocumentSymbolStyle styleEnd) {
		this.character = character;
		this.styleStart = styleStart;
		this.styleEnd = styleEnd;

		assert this.styleStart != null;
		assert this.styleEnd != null;
	}

	public static DocumentSymbol of(char character) {
		return new DocumentSymbol(character, DocumentSymbolStyle.of(), DocumentSymbolStyle.of());
	}

	@Override
	protected DocumentSymbol concreteCopy(Immutable<?> source) {
		return new DocumentSymbol(character, styleStart, styleEnd);
	}

	public DocumentSymbol withStyleStart(DocumentSymbolStyle styleStart) {
		return new DocumentSymbol(character, styleStart, styleEnd);
	}

	public DocumentSymbol withStyleEnd(DocumentSymbolStyle styleEnd) {
		return new DocumentSymbol(character, styleStart, styleEnd);
	}

	public DocumentSymbol withStyle(DocumentSymbolStyle styleStart, DocumentSymbolStyle styleEnd) {
		return withStyleStart(styleStart).withStyleEnd(styleEnd);
	}

	public String textNoStyle() {
		return Character.toString(character);
	}

	public String textStyle() {
		return textStyle(textNoStyle());
	}

	public String textStyle(String noStyle) {
		return styleStart.toString() + noStyle + styleEnd.toString();
	}

	@Override
	public String toString() {
		return textStyle();
	}
}