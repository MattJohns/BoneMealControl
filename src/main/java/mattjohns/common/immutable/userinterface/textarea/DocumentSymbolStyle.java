package mattjohns.common.immutable.userinterface.textarea;

import java.util.Optional;

import mattjohns.common.immutable.Immutable;

/**
 * A style code used to set the color and properties of text. The style code
 * might be a HTML tag or some other formatting code.
 * 
 * Minecraft uses these kinds of style codes where each character may be
 * prefixed by a code consisting of 2 characters. The left character is a marker
 * (ASCII 167) and the right character is a letter that signifies either a color
 * or bold etc..
 */
final class DocumentSymbolStyle extends Immutable<DocumentSymbolStyle> {
	public final Optional<String> code;

	protected DocumentSymbolStyle(Optional<String> code) {
		this.code = code;

		assert this.code != null;
	}

	public static DocumentSymbolStyle of() {
		return new DocumentSymbolStyle(Optional.empty());
	}

	public static DocumentSymbolStyle of(String code) {
		return new DocumentSymbolStyle(Optional.of(code));
	}
	
	@Override
	protected DocumentSymbolStyle concreteCopy(Immutable<?> source) {
		return new DocumentSymbolStyle(code);
	}

	public DocumentSymbolStyle withEmpty() {
		return new DocumentSymbolStyle(Optional.empty());
	}

	public DocumentSymbolStyle withCode(String code) {
		return new DocumentSymbolStyle(Optional.of(code));
	}
	
	public boolean isEmpty() {
		return !code.isPresent();
	}

	/**
	 * Empty string if no code.
	 */
	@Override
	public String toString() {
		if (code.isPresent()) {
			return code.get();
		}
		else {
			return "";
		}
	}
}
