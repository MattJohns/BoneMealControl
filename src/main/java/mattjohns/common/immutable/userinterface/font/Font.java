package mattjohns.common.immutable.userinterface.font;

import java.util.Optional;

import mattjohns.common.immutable.Immutable;
import mattjohns.common.immutable.userinterface.display.DisplaySize;

/**
 * A simple font representation. Each character has a unique fixed size
 * regardless of context. Made to represent Minecraft default font which is also
 * 'uniquely' fixed size.
 * 
 * Doesn't support Unicode of any kind (currently, Aug 2017).
 */
public final class Font extends Immutable<Font> {
	protected final FontSymbolList symbolList;
	public final int sizeY;

	protected Font(FontSymbolList symbolList, int sizeY) {
		this.symbolList = symbolList;
		this.sizeY = sizeY;

		assert this.symbolList != null;
		assert sizeY > 0;
	}

	public static Font of(FontSymbolList symbolList, int sizeY) {
		return new Font(symbolList, sizeY);
	}

	@Override
	protected Font concreteCopy(Immutable<?> source) {
		return new Font(symbolList, sizeY);
	}

	protected Font withSymbolList(FontSymbolList symbolList) {
		return new Font(symbolList, sizeY);
	}

	protected Font withSizeY(int sizeY) {
		return new Font(symbolList, sizeY);
	}

	protected Optional<FontSymbol> trySymbolGetByCharacter(char character) {
		return symbolList.stream()
				.filter(symbol -> symbol.character == character)
				.findFirst();
	}

	/**
	 * Checks if the given character exists in this font.
	 */
	public boolean symbolCheckByCharacter(char character) {
		return trySymbolGetByCharacter(character) != null;
	}

	public FontSymbol symbolGetByCharacter(char character) {
		Optional<FontSymbol> trySymbol = trySymbolGetByCharacter(character);
		assert trySymbol != null;
		return trySymbol.get();
	}

	public Optional<DisplaySize> textSize(String text) {
		if (text == null) {
			return Optional.empty();
		}

		int tallestCharacter = 0;
		int totalSizeX = 0;

		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);

			Optional<DisplaySize> characterSize = characterSize(character);

			if (characterSize.isPresent()) {
				if (characterSize.get().y > tallestCharacter) {
					tallestCharacter = characterSize.get().y;
				}

				totalSizeX += characterSize.get().x;
			}
		}

		if (tallestCharacter == 0 || totalSizeX == 0) {
			// either empty text or font characters somehow have no height
			return Optional.empty();

		}

		return Optional.of(DisplaySize.of(totalSizeX, tallestCharacter));
	}

	public Optional<DisplaySize> characterSize(char character) {
		if (!symbolCheckByCharacter(character)) {
			// character not in font
			return Optional.empty();
		}

		DisplaySize result = symbolGetByCharacter(character).size;

		return Optional.of(result);
	}
}
