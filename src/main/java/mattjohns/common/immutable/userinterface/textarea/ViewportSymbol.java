package mattjohns.common.immutable.userinterface.textarea;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplaySize;
import mattjohns.common.immutable.userinterface.font.Font;
import mattjohns.common.immutable.userinterface.font.FontSymbol;

final class ViewportSymbol extends Immutable<ViewportSymbol> {
	public final DocumentSymbol documentSymbol;
	public final FontSymbol fontSymbol;

	protected ViewportSymbol(DocumentSymbol documentSymbol, FontSymbol fontSymbol) {
		this.documentSymbol = documentSymbol;
		this.fontSymbol = fontSymbol;

		assert this.documentSymbol != null;
		assert this.fontSymbol != null;

		assert this.documentSymbol.character == this.fontSymbol.character;
	}

	public static ViewportSymbol of(DocumentSymbol documentSymbol, Font font) {
		FontSymbol fontSymbol = font.symbolGetByCharacter(documentSymbol.character);
		assert fontSymbol != null;

		return new ViewportSymbol(documentSymbol, fontSymbol);
	}
	
	@Override
	protected ViewportSymbol concreteCopy(Immutable<?> source) {
		return new ViewportSymbol(documentSymbol, fontSymbol);
	}

	/**
	 * Ensure document and font symbols don't get out of sync (i.e. have
	 * different characters).
	 */
	protected ViewportSymbol withSymbol(DocumentSymbol documentSymbol, FontSymbol fontSymbol) {
		return new ViewportSymbol(documentSymbol, fontSymbol);
	}

	public ViewportSymbol withStyleStart(DocumentSymbolStyle styleStart) {
		return withSymbol(documentSymbol.withStyleStart(styleStart), fontSymbol);
	}

	public ViewportSymbol withStyleEnd(DocumentSymbolStyle styleEnd) {
		return withSymbol(documentSymbol.withStyleEnd(styleEnd), fontSymbol);
	}

	public ViewportSymbol withStyle(DocumentSymbolStyle styleStart, DocumentSymbolStyle styleEnd) {
		return withSymbol(documentSymbol.withStyle(styleStart, styleEnd), fontSymbol);
	}

	public int sizeX() {
		return fontSymbol.size.x;
	}

	public int sizeY() {
		return fontSymbol.size.y;
	}

	public DisplaySize size() {
		return DisplaySize.of(sizeX(), sizeY());
	}

	public String textStyle() {
		// ensure to use our own text method, not DocumentSymbol.textNoStyle()
		return documentSymbol.textStyle(textNoStyle());
	}

	/**
	 * Note this is different than the document symbol because font characters
	 * are allowed to display text that is totally different to their main
	 * character.
	 * 
	 * For example with 'tab' symbol, ViewportSymbol.textNoStyle() gives space
	 * character, yet the DocumentSymbol.textNoStyle() gives '\t' character.
	 */
	public String textNoStyle() {
		return fontSymbol.displayText;
	}

	/**
	 * Non display character. Should be the actual character this symbol
	 * represents. e.g. Tab should have '\t' here and it can have space as the
	 * display character for rendering.
	 */
	public char character() {
		return fontSymbol.character;
	}

	@Override
	public String toString() {
		return textStyle();
	}
}
